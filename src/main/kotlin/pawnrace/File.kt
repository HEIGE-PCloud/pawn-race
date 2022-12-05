package pawnrace

class File(pos: Char) {
  val rank = pos.uppercaseChar().code - 'A'.code

  override fun toString(): String =
    (rank + 'A'.code).toChar().toString()

  override fun equals(other: Any?): Boolean = when (other) {
    is File -> other.rank == rank
    else -> false
  }

  override fun hashCode(): Int = rank.hashCode()
}
