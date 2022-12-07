package pawnrace

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.abs
import kotlin.math.max


// Use a much smaller INT_MAX to avoid overflow
const val INT_MAX = 100000000
const val INT_MIN = -100000000

class Player(val piece: Piece, var opponent: Player? = null) {
  private var run = true
  private val evaluateCache: ConcurrentHashMap<Game, Int> = ConcurrentHashMap()
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
    val cache = evaluateCache[game]
    if (cache != null) return cache
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
      // finally, add score if a pawn chain is formed
      if (game.board.pieceAt(move.to.leftBack()) == piece) {
        score += 2
      } else if (game.board.pieceAt(move.to.leftFront()) == piece) {
        score += 2
      }
      if (game.board.pieceAt(move.to.rightBack()) == piece) {
        score += 2
      } else if (game.board.pieceAt(move.to.rightFront()) == piece) {
        score += 2
      }

      acc + score
    }
    evaluateCache[game] = value
    return value
  }

  /**
   * Use Quiescence Search to only evaluate quiet moves to avoid the horizon effect
   */
  private fun quiesce(game: Game, a: Int, beta: Int): Int {
    var alpha = a
    val standPat = evaluate(game)
    if (standPat >= beta)
      return beta
    if (alpha < standPat)
      alpha = standPat
    val captures = getAllValidMoves(game).filter { it.type == MoveType.CAPTURE || it.type == MoveType.EN_PASSANT }
    for (capture in captures) {
      if (!run) break
      val nextGame = game.applyMove(capture)
      val score = -quiesce(nextGame, -beta, -alpha)
      if (score >= beta)
        return beta
      if (score > alpha)
        alpha = score
    }
    return alpha
  }

  private fun alphaBeta(game: Game, a: Int, beta: Int, depth: Int): Int {
    var alpha = a
    if (depth == 0) return quiesce(game, alpha, beta)
    val moves = getAllValidMoves(game)
    for (move in moves) {
      if (!run) break
      val score = -alphaBeta(game, -beta, -alpha, depth - 1)
      if (score >= beta)
        return beta
      alpha = max(alpha, score)
    }
    return alpha
  }

  private fun randomMove(game: Game): Move = getAllValidMoves(game).random()

  fun makeMove(game: Game, executor: ExecutorService): Move {
    val maxDepth = 30
    val moves = getAllValidMoves(game)
    val games = moves.map { game.applyMove(it) }
    val bestScore = AtomicInteger(INT_MIN)
    val bestMove: AtomicReference<Move?> = AtomicReference(null)
    run = true
    for (depth in 1..maxDepth) {
      for (i in moves.indices) {
        val currentMove = moves[i]
        val currentGame = games[i]
        executor.submit {
          val score = alphaBeta(currentGame, INT_MIN, INT_MAX, depth)
          if (score > bestScore.get()) {
            bestScore.set(score)
            bestMove.set(currentMove)
          }
        }
      }
    }
    if (!executor.awaitTermination(4500, TimeUnit.MILLISECONDS)) {
      run = false
    }

    return bestMove.get() ?: randomMove(game)
  }

  override fun toString(): String {
    return "Player $piece"
  }

}
