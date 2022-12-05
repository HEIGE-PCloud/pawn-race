package pawnrace

class Rank(pos: Char) {
  constructor(row: Int) : this((row + 1).digitToChar())
  val value = pos.digitToInt() - 1

  override fun toString(): String =
    (value + 1).toString()

  override fun equals(other: Any?): Boolean = when(other) {
    is Rank -> other.value == value
    else -> false
  }

  override fun hashCode(): Int = value.hashCode()
}
