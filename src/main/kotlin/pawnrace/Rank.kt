package pawnrace

data class Rank(val value: Int) {
  constructor(pos: Char) : this(pos.digitToInt() - 1)

  override fun toString(): String =
    (value + 1).toString()
}
