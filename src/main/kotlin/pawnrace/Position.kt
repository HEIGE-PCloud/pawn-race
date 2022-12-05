package pawnrace

class Position(row: Int, col: Int) {
  val rank = Rank(row)
  val file = File(col)

  constructor(pos: String) : this(Rank(pos[1]).value, File(pos[0]).value)

  override fun toString(): String =
    file.toString() + rank.toString()

  override fun equals(other: Any?): Boolean = when (other) {
    is Position -> file == other.file && rank == other.rank
    else -> false
  }

  override fun hashCode(): Int = 31 * file.hashCode() + rank.hashCode()
}
