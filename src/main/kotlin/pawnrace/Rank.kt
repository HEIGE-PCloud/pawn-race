package pawnrace

class Rank(row: Int) {
  constructor(pos: Char) : this(pos.digitToInt() - 1)
  val value = row

  override fun toString(): String =
    (value + 1).toString()

  override fun equals(other: Any?): Boolean = when(other) {
    is Rank -> other.value == value
    else -> false
  }

  override fun hashCode(): Int = value.hashCode()
}
