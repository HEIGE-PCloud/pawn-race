package pawnrace

data class File(val value: Int) {
  constructor(pos: Char) : this(pos.lowercaseChar().code - 'a'.code)

  override fun toString(): String =
    (value + 'a'.code).toChar().toString()
}
