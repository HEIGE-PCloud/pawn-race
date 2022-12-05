package pawnrace

class Board(whiteGap: File, blackGap: File) {
  private val size = 8
  private val whiteStartingFile = 1
  private val blackStartingFile = 6
  private val board : List<List<Piece?>>
  init {
    board = List(size) { i ->
      when (i) {
        whiteStartingFile -> List(size) { j ->
          when (j) {
            whiteGap.value -> null
            else -> Piece.WHITE
          }
        }
        blackStartingFile -> List(size) { j ->
          when (j) {
            blackGap.value -> null
            else -> Piece.BLACK
          }
        }

        else -> List(size) { null }
      }
    }
  }

  fun pieceAt(pos: Position): Piece? = board[pos.file.value][pos.rank.value]
  fun positionsOf(piece: Piece): List<Position> = TODO()
  fun isValidMove(move: Move, lastMove: Move? = null): Boolean = TODO()
  fun move(m: Move): Board = TODO()
  override fun toString(): String {
    val sb = StringBuilder()
    sb.append("  A B C D E F G H  \n")
    for (i in 7 downTo 0) {
      sb.append("${i + 1} ")
      for (j in 0..7) {
        when (board[i][j]) {
          null -> sb.append(". ")
          Piece.WHITE -> sb.append("W ")
          Piece.BLACK -> sb.append("B ")
        }
      }
      sb.append("${i + 1} \n")
    }
    sb.append("  A B C D E F G H  ")
    return sb.toString()
  }
}


fun main() {
  val board = Board(File('h'), File('a'))
  println(board)
}
