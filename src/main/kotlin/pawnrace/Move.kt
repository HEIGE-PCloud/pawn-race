package pawnrace

data class Move(
  val piece: Piece,
  val from: Position,
  val to: Position,
  val type: MoveType
) {
  /**
   * Returns the short algebraic notation for this move.
   */
  override fun toString(): String = when(type) {
    MoveType.PEACEFUL -> to.toString()
    MoveType.CAPTURE -> "${from.file}x${to}"
    MoveType.EN_PASSANT -> "${from.file}x${to}"
  }
}
