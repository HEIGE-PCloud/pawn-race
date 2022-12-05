package pawnrace

class File(col: Int) {
  constructor(pos: Char) : this(pos.lowercaseChar().code - 'a'.code)

  val value = col

  override fun toString(): String =
    (value + 'a'.code).toChar().toString()

  override fun equals(other: Any?): Boolean = when (other) {
    is File -> other.value == value
    else -> false
  }

  override fun hashCode(): Int = value.hashCode()
}
