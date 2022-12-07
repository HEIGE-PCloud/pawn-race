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
    game.moves(piece).sortedByDescending { it.type.ordinal + (piece.direction()) * it.to.rank.value }


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
    return positions.fold(0) { acc, pos ->
      acc + abs(pos.rank.value - piece.startingRank()) + 5 * (BOARD_SIZE - passedPawn(game.board, pos))
    }
  }

  private fun negamax(game: Game, depth: Int, a: Int, b: Int, colour: Int, runMove: Int): Int {
    var alpha = a
    var beta = b

    if (runMove != runningMove.get()) return 0
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
      return colour * evaluate(game)
    }
    if (game.over()) {
      // lower depth is better
      val score = (100 - depth) * 10000
      return when (game.winner()) {
        null -> 0
        this -> score
        else -> -score
      }
    }
    val games = getAllValidMoves(game).map { game.applyMove(it) }
    var value = INT_MIN
    for (nextGame in games) {
      if (runningMove.get() != runMove) return 0
      value = max(value, -negamax(nextGame, depth - 1, -beta, -alpha, -colour, runMove))
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
    val maxDepth = 50
    val moves = getAllValidMoves(game)
    val bestScore = AtomicInteger(INT_MIN)
    val bestMove: AtomicReference<Move?> = AtomicReference(null)
    var maxSearchDepth = 0
    for (depth in 0..maxDepth) {
      for (currentMove in moves) {
        val currentGame = game.applyMove(currentMove)
        val runMove = runningMove.get()
        executor.submit {
          val score = negamax(currentGame, depth, INT_MIN, INT_MAX, 1, runMove)
          if (runMove == runningMove.get() && score > bestScore.get()) {
            bestScore.set(score)
            bestMove.set(currentMove)
          }
          if (runningMove.get() == runMove) {
//            println("[DEBUG] RunningMove ${runningMove.get()} runMove ${runMove}")
//            println("[DEBUG] Search depth $depth with move $currentMove completed, score $score, bestScore ${bestScore.get()}," +
//              " " +
//              "bestMove ${bestMove.get()}")
            maxSearchDepth = max(maxSearchDepth, depth)

          }
        }
      }
    }
    Thread.sleep(4500)
    runningMove.set(runningMove.get() + 1)
//    println("[DEBUG] runningMove has been updated to ${runningMove}")
    println("[INFO] Search Depth $maxSearchDepth")
    println("[INFO] Best move ${bestMove.get()}")
    return bestMove.get() ?: randomMove(game)
  }

  override fun toString(): String {
    return "Player $piece"
  }

}
