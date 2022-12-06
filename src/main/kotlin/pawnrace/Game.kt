package pawnrace

class Game(board: Board, val me: Player, val opponent: Player) {
  private val historyBoards = mutableListOf<Board>()
  private val historyMoves = mutableListOf<Move>()
  var board = board
    private set

  fun applyMove(move: Move) {
    historyMoves.add(move)
    historyBoards.add(board)
    board = board.move(move)
  }

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
      me.piece, pos, Position(
        pos.rank.value + me.piece.direction() * step, pos.file.value
      ), MoveType.PEACEFUL
    )
    return if (board.isValidMove(move, historyMoves.lastOrNull())) move else null
  }

  private fun moveDiagonalBy(
    pos: Position, isLeft: Boolean, piece: Piece, type: MoveType
  ): Move? {
    val move = Move(
      me.piece, pos, Position(
        pos.rank.value + me.piece.direction(),
        if (isLeft) pos.file.value - me.piece.direction()
        else pos.file.value + me.piece.direction()
      ), type
    )
    return if (board.isValidMove(move, historyMoves.lastOrNull())) move else null
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
    var winnerPiece: Piece? = null
    for (i in 0..7) {
      if (board.pieceAt(Position(7, i)) == Piece.WHITE) winnerPiece =
        Piece.WHITE
      if (board.pieceAt(Position(7, i)) == Piece.BLACK) winnerPiece =
        Piece.BLACK
    }
    if (winnerPiece != null) {
      return pieceToPlayer(winnerPiece)
    }
    val blackMoves = moves(Piece.BLACK)
    val whiteMoves = moves(Piece.WHITE)

    if (blackMoves.isEmpty() && whiteMoves.isEmpty()) return null

    if (blackMoves.isEmpty()) return pieceToPlayer(Piece.WHITE)
    return pieceToPlayer(Piece.BLACK)
  }

  fun parseMove(piece: Piece, moveString: String): Move? {
    if (moveString.length == 2) {
      val file = File(moveString[0])
      val posTo = Position(moveString)
      for (i in 0 until BOARD_SIZE) {
        val curPos = Position(i, file.value)
        if (board.pieceAt(curPos) == piece) {
          val move = Move(piece, curPos, posTo, MoveType.PEACEFUL)
          if (board.isValidMove(move)) {
            return move
          }
        }
      }
    }
    else if (moveString.length == 4) {
      val (fileString, posToString) = moveString.split('x')
      val file = File(fileString[0])
      val posTo = Position(posToString)
      for (i in 0 until BOARD_SIZE) {
        val curPos = Position(i, file.value)
        if (board.pieceAt(curPos) == piece) {
          val moveCapture = Move(piece, curPos, posTo, MoveType.CAPTURE)
          val moveEnPassant = Move(piece, curPos, posTo, MoveType.EN_PASSANT)
          if (board.isValidMove(moveCapture)) {
            return moveCapture
          }
          if (board.isValidMove(moveEnPassant, historyMoves.lastOrNull())) {
            return moveEnPassant
          }
        }
      }
    }
    else if (moveString.length == 5) {
      if (moveString.contains('-')) {
        val (fromString, toString) = moveString.split('-')
        return Move(
          piece,
          Position(fromString),
          Position(toString),
          MoveType.PEACEFUL
        )
      } else {
        val (fromString, toString) = moveString.split('x')
        val from = Position(fromString)
        val to = Position(toString)
        val enPassantMove = Move(piece, from, to, MoveType.EN_PASSANT)
        if (board.isValidMove(enPassantMove))
          return enPassantMove
        return Move(piece, from, to, MoveType.CAPTURE)
      }
    }
    return null
  }

  private fun pieceToPlayer(piece: Piece): Player = when (piece) {
    me.piece -> me
    else -> opponent
  }
}
