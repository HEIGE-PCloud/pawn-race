package pawnrace

class Position(pos: String) {
  val file = File(pos[0])
  val rank = Rank(pos[1])

  override fun toString(): String =
    file.toString() + rank.toString()
}
