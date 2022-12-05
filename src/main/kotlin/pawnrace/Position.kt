package pawnrace

class Position(pos: String) {
  val file = File(pos[0])
  val rank = Rank(pos[1])

  override fun toString(): String =
    file.toString() + rank.toString()

  override fun equals(other: Any?): Boolean = when (other) {
    is Position -> file == other.file && rank == other.rank
    else -> false
  }

  override fun hashCode(): Int = 31 * file.hashCode() + rank.hashCode()
}
