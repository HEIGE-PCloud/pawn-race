package pawnrace

import kotlin.math.abs
import kotlin.math.max

class Player(val piece: Piece, var opponent: Player? = null) {
  fun getAllPawns(board: Board): List<Position> = board.positionsOf(piece)
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

  fun evaluate(game: Game): Double {
    val positions = getAllPawns(game.board)
    return positions.fold(0.0) { acc, pos ->
      acc + abs(pos.rank.value - piece.startingRank())
    }
  }

  fun negaMax(game: Game, depth: Int): Double {
    if (depth == 0) return evaluate(game)
    var maxScore = Double.MIN_VALUE
    val allMoves = getAllValidMoves(game)
    for (move in allMoves) {
      maxScore = max(-1 * negaMax(game, depth - 1), maxScore)
    }
    return maxScore;
  }

  fun makeMove(game: Game): Move {
    val moves = getAllValidMoves(game)
    val games = moves.map { game.applyMove(it) }
    val scores = games.map { negaMax(it, 5) }
    val maxScore = scores.max()
    val maxIndex = scores.indexOf(maxScore)
    return moves[maxIndex]
  }



  override fun toString(): String {
    return "Player $piece"
  }

}
