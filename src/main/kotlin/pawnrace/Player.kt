package pawnrace

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

  fun makeMove(game: Game): Move = getAllValidMoves(game).random()

  override fun toString(): String {
    return "Player $piece"
  }

}
