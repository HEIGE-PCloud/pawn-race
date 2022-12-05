package pawnrace

class File(pos: Char) {
  val rank = pos.lowercaseChar().code - 'a'.code

  override fun toString(): String =
    (rank + 'a'.code).toChar().toString()

  override fun equals(other: Any?): Boolean = when (other) {
    is File -> other.rank == rank
    else -> false
  }

  override fun hashCode(): Int = rank.hashCode()
}
