package pawnrace

class Game(val board: Board, val player: Player, val lastMove: Move? = null) {
  fun applyMove(move: Move): Game =
    Game(board.move(move), player.opponent!!, move)

  /**
   * Finds all possible valid moves.
   * This can use two helpers: moveForwardBy and moveDiagonalBy,
   * since a pawn can move either forward or diagonal.
   */
  fun moves(piece: Piece): List<Move> =
    board.positionsOf(piece).flatMap { pos: Position ->
      listOfNotNull(
        moveForwardBy(pos, 1, piece),
        moveForwardBy(pos, 2, piece),
        moveDiagonalBy(pos, true, piece, MoveType.CAPTURE),
        moveDiagonalBy(pos, false, piece, MoveType.CAPTURE),
        moveDiagonalBy(pos, true, piece, MoveType.EN_PASSANT),
        moveDiagonalBy(pos, false, piece, MoveType.EN_PASSANT)
      )
    }

  private fun moveForwardBy(pos: Position, step: Int, piece: Piece): Move? {
    val move = Move(
      player.piece, pos, Position(
        pos.rank.value + player.piece.direction() * step, pos.file.value
      ), MoveType.PEACEFUL
    )
    return if (board.isValidMove(move, lastMove)) move else null
  }

  private fun moveDiagonalBy(
    pos: Position, isLeft: Boolean, piece: Piece, type: MoveType
  ): Move? {
    val move = Move(
      player.piece, pos, Position(
        pos.rank.value + player.piece.direction(),
        if (isLeft)
          pos.file.value - player.piece.direction()
        else
          pos.file.value + player.piece.direction()
      ), type
    )
    return if (board.isValidMove(move, lastMove)) move else null
  }

  private fun isPromoted(): Piece? {
    for (i in 0 until BOARD_SIZE) {
      if (board.pieceAt(Position(BOARD_SIZE - 1, i)) == Piece.WHITE) return Piece.WHITE
      if (board.pieceAt(Position(BOARD_SIZE - 1, i)) == Piece.BLACK) return Piece.BLACK
    }
    return null
  }

  fun over(): Boolean {
    if (isPromoted() != null) return true
    val blackMoves = moves(Piece.BLACK)
    val whiteMoves = moves(Piece.WHITE)
    if (blackMoves.isEmpty() || whiteMoves.isEmpty()) return true
    return false
  }
  fun winner(): Player? {
    require(over())
    var winnerPiece: Piece
    for (i in 0..7) {
      if (board.pieceAt(Position(7, i)) == Piece.WHITE) winnerPiece = Piece.WHITE
      if (board.pieceAt(Position(7, i)) == Piece.BLACK) winnerPiece = Piece.BLACK
    }
    val blackMoves = moves(Piece.BLACK)
    val whiteMoves = moves(Piece.WHITE)
    if (blackMoves.isEmpty() && whiteMoves.isEmpty()) return null
    if (blackMoves.isEmpty()) winnerPiece = Piece.WHITE
    else winnerPiece = Piece.BLACK
    if (winnerPiece == player.piece) return player
    return player.opponent
  }

  fun parseMove(san: String): Move? = TODO()
}
