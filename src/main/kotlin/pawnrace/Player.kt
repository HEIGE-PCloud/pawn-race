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
const val LOWERBOUND = 1
const val UPPERBOUND = 2

class Player(val piece: Piece, var opponent: Player? = null) {
  private var run = true
  // map a game to (depth, flag, value)
  private val transpositionTable: ConcurrentHashMap<Game, Triple<Int, Int, Int>> = ConcurrentHashMap()
  private fun getAllPawns(board: Board): List<Position> = board.positionsOf(piece)

  /**
   * get all valid moves and sort them in such order that
   * better moves should come first for better alpha beta pruning
   * a capture move with high rank value should be a better move
   */
  private fun getAllValidMoves(game: Game): List<Move> =
    game.moves(piece).sortedByDescending { it.type.ordinal + it.to.rank.value }


  /**
   * Return 0 if the pawn is not a passed pawn
   * Return a positive integer n if it is
   * the n is the distance until it reaches the finishing line
   */
  private fun passedPawn(board: Board, pos: Position): Int {
    if (board.pieceAt(pos) != piece) return 0
    val direction = piece.direction()
    val startRank = pos.rank.value + direction
    val file = pos.file.value
    if (startRank < 0 || startRank >= BOARD_SIZE) return 0
    val endRank = piece.promotionRank()
    var i = startRank
    while (i != endRank) {
      if (board.pieceAt(Position(i, file)) != null) return 0
      if (file > 0 && board.pieceAt(Position(i, file - 1)) != piece.opposite()) return 0
      if (file < BOARD_SIZE - 1 && board.pieceAt(Position(i, file + 1)) != piece.opposite()) return 0
      i += direction
    }
    return abs(endRank - startRank) + 1
  }

  private fun evaluate(game: Game): Int {
    val positions = getAllPawns(game.board)
    if (positions.find { it.rank.value == piece.promotionRank() } != null) return 1000000000
    val moves = getAllValidMoves(game)
    val value = moves.fold(0) { acc, move ->
      var score = move.distance() * 2 // 1 or 2
      if (move.to.rank.value == piece.promotionRank() ||
        move.from.rank.value == piece.promotionRank()
      ) score += 1000
      // if capture +5
      if (move.type != MoveType.PEACEFUL) {
        score += 10
      }
      val pass = passedPawn(game.board, move.to)
      // if it is a passed pawn, add score
      if (pass > 0) {
        score += (BOARD_SIZE - pass) * 5
      }
      acc + score
    }
    return value
  }

  /**
   * Use Quiescence Search to only evaluate quiet moves to avoid the horizon effect
   */
  private fun quiesce(game: Game, a: Int, beta: Int): Int {
    var alpha = a
    val eva = evaluate(game)
    if (eva >= beta)
      return beta
    if (alpha < eva)
      alpha = eva
    val captures = getAllValidMoves(game).filter { it.type == MoveType.CAPTURE || it.type == MoveType.EN_PASSANT }
    for (capture in captures) {
      if (!run) break
      val score = -quiesce(game.applyMove(capture), -beta, -alpha)
      if (score >= beta)
        return beta
      if (score > alpha)
        alpha = score
    }
    return alpha
  }

  private fun negamax(game: Game, depth: Int, a: Int, b: Int): Int {
    var alpha = a
    var beta = b

    val entry = transpositionTable[game]
    if (entry != null) {
      val (enDepth, enFlag, enValue) = entry
      if (enDepth >= depth) {
        if (enFlag == EXACT) {
          return enValue
        } else if (enFlag == LOWERBOUND) {
          alpha = max(alpha, enValue)
        } else if (enFlag == UPPERBOUND) {
          beta = min(beta, enValue)
        }
      }
      if (alpha >= beta) {
        return enValue
      }
    }

    if (depth == 0) {
      return quiesce(game, a, beta)
    }
    if (game.over()) {
      return when (game.winner()) {
        null -> 0
        this -> 10000
        else -> -10000
      }
    }
    val games = getAllValidMoves(game).map { game.applyMove(it) }
    var value = INT_MIN
    for (nextGame in games) {
      if (!run) break
      value = max(value, -negamax(nextGame, depth - 1, -beta, -alpha))
      alpha = max(alpha, value)
      if (alpha >= beta) break
    }

    val enFlag: Int
    if (value <= a) {
      enFlag = UPPERBOUND
    } else if (value >= beta) {
      enFlag = LOWERBOUND
    } else {
      enFlag = EXACT
    }
    transpositionTable[game] = Triple(depth, enFlag, value)
    return value
  }

  private fun randomMove(game: Game): Move = getAllValidMoves(game).random()

  fun makeMove(game: Game, executor: ExecutorService): Move {
    val maxDepth = 30
    val moves = getAllValidMoves(game)
    val games = moves.map { game.applyMove(it) }
    val bestScore = AtomicInteger(INT_MIN)
    val bestMove: AtomicReference<Move?> = AtomicReference(null)
    run = true
    var maxSearchDepth = 0
    for (depth in 1..maxDepth) {
      for (i in moves.indices) {
        val currentMove = moves[i]
        val currentGame = games[i]
        executor.submit {
          val score = negamax(currentGame, INT_MIN, INT_MAX, depth)
          if (score > bestScore.get()) {
            bestScore.set(score)
            bestMove.set(currentMove)
          }
          if (run) maxSearchDepth = max(maxSearchDepth, depth)
        }
      }
    }
    if (!executor.awaitTermination(4500, TimeUnit.MILLISECONDS)) {
      run = false
    }
    println("[INFO] Search Depth $maxSearchDepth")
    return bestMove.get() ?: randomMove(game)
  }

  override fun toString(): String {
    return "Player $piece"
  }

}
