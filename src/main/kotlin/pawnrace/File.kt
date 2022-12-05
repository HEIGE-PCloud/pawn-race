package pawnrace

class File(pos: Char) {
  val rank = pos.uppercaseChar().code - 'A'.code

  override fun toString(): String =
    (rank + 'A'.code).toChar().toString()
}
