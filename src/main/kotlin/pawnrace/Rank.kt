package pawnrace

class Rank(pos: Char) {
  val rank = pos.digitToInt() - 1

  override fun toString(): String =
    (rank + 1).toString()
}
