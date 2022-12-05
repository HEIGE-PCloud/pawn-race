package pawnrace

class Rank(pos: Char) {
  val rank = pos.digitToInt() - 1

  override fun toString(): String =
    (rank + 1).toString()

  override fun equals(other: Any?): Boolean = when(other) {
    is Rank -> other.rank == rank
    else -> false
  }

  override fun hashCode(): Int = rank.hashCode()
}
