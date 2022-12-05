package pawnrace

class File(pos: Char) {
  constructor(col: Int) : this((col + 'a'.code).toChar())

  val value = pos.lowercaseChar().code - 'a'.code

  override fun toString(): String =
    (value + 'a'.code).toChar().toString()

  override fun equals(other: Any?): Boolean = when (other) {
    is File -> other.value == value
    else -> false
  }

  override fun hashCode(): Int = value.hashCode()
}
