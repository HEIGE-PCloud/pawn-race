package pawnrace

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow

class Player(val piece: Piece, var opponent: Player? = null) {
  private val evaluateCache: ConcurrentHashMap<Game, Double> = ConcurrentHashMap()
  private fun getAllPawns(board: Board): List<Position> = board.positionsOf(piece)
  private fun getAllValidMoves(game: Game): List<Move> = game.moves(piece)

  fun isPassedPawn(board: Board, pos: Position): Boolean {
    if (board.pieceAt(pos) != piece) return false
    val startRow = pos.rank.value + piece.direction()
    val endRow = piece.promotionRank()
    val col = pos.file.value
    val leftCol = col - 1
    val rightCol = col + 1
    for (i in startRow..endRow step piece.direction()) {
      if (board.pieceAt(Position(i, col)) != null) return false
      if (leftCol >= 0 && board.pieceAt(Position(i, leftCol)) == piece.opposite()) return false
      if (rightCol >= 0 && board.pieceAt(Position(i, rightCol)) == piece.opposite()) return false
    }
    return true
  }

  private fun evaluate(game: Game): Double {
    val cache = evaluateCache[game]
    if (cache != null) return cache

    val positions = getAllPawns(game.board)
    if (positions.find { it.rank.value == piece.promotionRank() } != null) return 1e9

    val value = positions.fold(0.0) { acc, pos ->
      acc + (8.0).pow(abs(pos.rank.value - piece.startingRank()))
    }
    evaluateCache[game] = value
    return value
  }

  fun quiesce(game: Game, a: Int, beta: Int): Int {
    var alpha = a
    val stand_pat = evaluate(game)
    if(stand_pat >= beta)
      return beta
    if(alpha < stand_pat)
      alpha = stand_pat.toInt()
    val captures = getAllValidMoves(game).filter { it.type == MoveType.CAPTURE || it.type == MoveType.EN_PASSANT }
    for(capture in captures)  {
      val nextGame = game.applyMove(capture)
      val score = -quiesce(nextGame, -beta, -alpha )
      if( score >= beta )
        return beta
      if( score > alpha )
        alpha = score
    }
    return alpha
  }

  fun alphaBeta(game: Game, a: Int, beta: Int, depthleft: Int ): Int {
    var alpha = a
    if (depthleft == 0) return quiesce(game, alpha, beta)
    val moves = getAllValidMoves(game)
    for (move in moves)  {
      val score = -alphaBeta(game, -beta, -alpha, depthleft - 1 )
      if (score >= beta)
        return beta   //  fail hard beta-cutoff
      if (score > alpha)
        alpha = score // alpha acts like max in MiniMax
    }
    return alpha
  }

  private fun negaMax(game: Game, depth: Int): Double {
    if (depth == 0) return evaluate(game)
    var maxScore = Double.MIN_VALUE
    val allMoves = getAllValidMoves(game)
    for (move in allMoves) {
      maxScore = max(-1 * alphaBeta(game, Int.MIN_VALUE, Int.MAX_VALUE, depth - 1).toDouble(), maxScore)
    }
    return maxScore
  }

  fun makeMove(game: Game): Move {
    val moves = getAllValidMoves(game)
    val games = moves.map { game.applyMove(it) }
    val scores = games.map { negaMax(it, 7) }
    val maxScore = scores.max()
    val maxIndex = scores.indexOf(maxScore)
    return moves[maxIndex]
  }

  override fun toString(): String {
    return "Player $piece"
  }

}
