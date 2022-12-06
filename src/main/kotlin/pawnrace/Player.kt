package pawnrace

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.max

class Player(val piece: Piece, var opponent: Player? = null) {
  private val evaluateCache: ConcurrentHashMap<Game, Int> = ConcurrentHashMap()
  private fun getAllPawns(board: Board): List<Position> = board.positionsOf(piece)
  private fun getAllValidMoves(game: Game): List<Move> = game.moves(piece)

  fun isPassedPawn(board: Board, pos: Position): Int {
    val startRow = pos.rank.value + piece.direction()
    val endRow = piece.promotionRank()
    val col = pos.file.value
    val leftCol = col - 1
    val rightCol = col + 1
//    for (i in startRow..endRow) {
//      if (board.pieceAt(Position(i, col)) != null) return 0
//      if (leftCol >= 0 && board.pieceAt(Position(i, leftCol)) == piece.opposite()) return 0
//      if (rightCol >= 0 && board.pieceAt(Position(i, rightCol)) == piece.opposite()) return 0
//    }
    return 0
  }

  private fun evaluate(game: Game): Int {
    val cache = evaluateCache[game]
    if (cache != null) return cache

    val positions = getAllPawns(game.board)
    if (positions.find { it.rank.value == piece.promotionRank() } != null) return 1000000000


    val moves = getAllValidMoves(game)
    val value = positions.fold(0) { acc, pos ->
      val dist = abs(pos.rank.value - piece.startingRank())
      acc + dist * dist + dist * dist * isPassedPawn(game.board, pos)
    }
    evaluateCache[game] = value
    return value
  }

  private fun quiesce(game: Game, a: Int, beta: Int): Int {
    var alpha = a
    val stand_pat = evaluate(game)
    if (stand_pat >= beta)
      return beta
    if (alpha < stand_pat)
      alpha = stand_pat
    val captures = getAllValidMoves(game).filter { it.type == MoveType.CAPTURE || it.type == MoveType.EN_PASSANT }
    for (capture in captures) {
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
      val score = -alphaBeta(game, -beta, -alpha, depth - 1)
      if (score >= beta)
        return beta
      alpha = max(alpha, score)
    }
    return alpha
  }

  private fun negaMax(game: Game, depth: Int): Int {
    if (depth == 0) return evaluate(game)
    var maxScore = Int.MIN_VALUE
    val allMoves = getAllValidMoves(game)
    for (move in allMoves) {
      maxScore = max(-1 * alphaBeta(game, Int.MIN_VALUE, Int.MAX_VALUE, depth - 1), maxScore)
    }
    return maxScore
  }

  private fun randomMove(game: Game): Move = getAllValidMoves(game).random()
  fun makeMove(game: Game): Move {
    val moves = getAllValidMoves(game)
    val games = moves.map { game.applyMove(it) }
    val scores = games.map { negaMax(it, 8) }
    val maxScore = scores.max()
    val maxIndex = scores.indexOf(maxScore)
    return moves[maxIndex]
  }
  override fun toString(): String {
    return "Player $piece"
  }

}
