package pawnrace

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


// Use a much smaller INT_MAX to avoid overflow
const val INT_MAX = 100000000
const val INT_MIN = -100000000
const val EXACT = 0
const val LOWER_BOUND = 1
const val UPPER_BOUND = 2

class Player(val piece: Piece, var opponent: Player? = null) {
  private var runningMove = AtomicInteger(0)

  // map a game to (depth, flag, value)
  private val transpositionTable: ConcurrentHashMap<Game, Triple<Int, Int, Int>> = ConcurrentHashMap()
  private fun getAllPawns(board: Board): List<Position> = board.positionsOf(piece)

  /**
   * get all valid moves and sort them in such order that
   * better moves should come first for better alpha beta pruning
   * a capture move with high rank value should be a better move
   */
  private fun getAllValidMoves(game: Game): List<Move> =
    game.moves(piece).sortedByDescending { it.type.ordinal * 10 + (piece.direction()) * it.to.rank.value }


  /**
   * Return 0 if the pawn is not a passed pawn
   * Return a positive integer n if it is
   * the n is the distance it has travelled
   */
  private fun passedPawn(board: Board, pos: Position, piece: Piece): Int {
    if (board.pieceAt(pos) != piece) return 0
    val direction = piece.direction()
    val startRank = pos.rank.value + direction
    val file = pos.file.value
    if (startRank < 0 || startRank >= BOARD_SIZE) return 0
    val endRank = piece.promotionRank()
    var i = startRank
    while (i != endRank) {
      if (board.pieceAt(Position(i, file)) != null) return 0
      if (file > 0 && board.pieceAt(Position(i, file - 1)) == piece.opposite()) return 0
      if (file < BOARD_SIZE - 1 && board.pieceAt(Position(i, file + 1)) == piece.opposite()) return 0
      i += direction
    }
    return abs(piece.startingRank() - pos.rank.value)
  }

  private fun distanceFromStart(pos: Position, piece: Piece): Int = abs(pos.rank.value - piece.startingRank())

  private fun evaluate(game: Game): Int {
    val myPoss = getAllPawns(game.board)
    val oppoPoss = opponent!!.getAllPawns(game.board)
    val myScore = myPoss.fold(0) { acc, pos ->
      acc +
        distanceFromStart(pos, piece) +
        5 * passedPawn(game.board, pos, piece)
    }
    val opponentScore = oppoPoss.fold(0) { acc, pos ->
      acc +
        distanceFromStart(pos, opponent!!.piece) +
        5 * passedPawn(game.board, pos, opponent!!.piece)
    }
    return myScore - opponentScore
  }

  private fun negamax(game: Game, depth: Int, a: Int, b: Int, colour: Int, runMove: Int): Int {
    var alpha = a
    var beta = b

    if (runMove != runningMove.get()) return 0
    val entry = transpositionTable[game]
    if (entry != null) {
      val (enDepth, enFlag, enValue) = entry
      if (enDepth >= depth) {
        when (enFlag) {
          EXACT -> return enValue
          LOWER_BOUND -> alpha = max(alpha, enValue)
          UPPER_BOUND -> beta = min(beta, enValue)
        }
      }
      if (alpha >= beta) return enValue
    }

    if (game.over()) {
      // lower depth -> win/lose sooner -> is better/worse
      val score = 10000
      return colour * when (game.winner()?.piece) {
        null -> 0
        piece -> score
        else -> -score
      }
    }

    if (depth == 0) return colour * evaluate(game)//quiescence(game, alpha, beta, colour)

    val games = if (colour == 1)
      getAllValidMoves(game).map { game.applyMove(it) }
    else opponent!!.getAllValidMoves(game)
      .map { game.applyMove(it) }

    var value = INT_MIN
    for (nextGame in games) {
      if (runningMove.get() != runMove) return 0
      value = max(value, -negamax(nextGame, depth - 1, -beta, -alpha, -colour, runMove))
      alpha = max(alpha, value)
      if (alpha >= beta) break
    }

    val enFlag: Int = if (value <= a) {
      UPPER_BOUND
    } else if (value >= beta) {
      LOWER_BOUND
    } else {
      EXACT
    }
    transpositionTable[game] = Triple(depth, enFlag, value)
    return value
  }

  private fun randomMove(game: Game): Move = getAllValidMoves(game).random()

  fun makeMove(game: Game, executor: ExecutorService, startTime: Long, timeLimit: Long): Move {
    val maxDepth = 30
    val moves = getAllValidMoves(game)
    val depthBestScores = List(maxDepth + 1) { AtomicInteger(INT_MIN) }
    val depthBestMoves = List<AtomicReference<Move>>(maxDepth + 1) { AtomicReference(null) }
    val depthSearchCount = List(maxDepth + 1) { AtomicInteger(0) }
    for (depth in 0..maxDepth) {
      for (currentMove in moves) {
        val currentGame = game.applyMove(currentMove)
        val runMove = runningMove.get()
        executor.submit {
          val score = -negamax(currentGame, depth, INT_MIN, INT_MAX, -1, runMove)
          if (runMove == runningMove.get()) {
            depthSearchCount[depth].incrementAndGet()
            if (score > depthBestScores[depth].get()) {
              depthBestScores[depth].set(score)
              depthBestMoves[depth].set(currentMove)
            }
            println(
              "[DEBUG] Search depth $depth with move $currentMove completed, score $score, depthBestScore " +
                "${depthBestScores[depth]}," +
                " " +
                "depthBestMove ${depthBestMoves[depth]}"
            )
          }
        }
      }
    }
    Thread.sleep(startTime + timeLimit - System.nanoTime() / 1000000)
    runningMove.incrementAndGet()
    val maxSearchedDepth = depthSearchCount.indexOfLast { it.get() == moves.size }
    val bestMove = depthBestMoves[maxSearchedDepth].get()
    println("[INFO] Search Depth $maxSearchedDepth")
    println("[INFO] Best move $bestMove")
    println("[INFO] Evaluation ${depthBestScores[maxSearchedDepth]}")
    return bestMove ?: randomMove(game)
  }

  override fun toString(): String {
    return "Player $piece"
  }

}
