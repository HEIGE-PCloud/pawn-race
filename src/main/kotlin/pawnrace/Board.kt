package pawnrace

import kotlin.math.abs

const val BOARD_SIZE = 8
const val WHITE_START_FILE = 1
const val BLACK_START_FILE = 6

/**
 * Board is an immutable type representing the current board status
 */
class Board(private val board: List<List<Piece?>>) {
  constructor(whiteGap: File, blackGap: File) : this(List(BOARD_SIZE) { i ->
    when (i) {
      WHITE_START_FILE -> List(BOARD_SIZE) { j ->
        when (j) {
          whiteGap.value -> null
          else -> Piece.WHITE
        }
      }

      BLACK_START_FILE -> List(BOARD_SIZE) { j ->
        when (j) {
          blackGap.value -> null
          else -> Piece.BLACK
        }
      }

      else -> List(BOARD_SIZE) { null }
    }
  })

  fun pieceAt(pos: Position): Piece? = board[pos.rank.value][pos.file.value]

  /**
   * Returns the Positions of a given Piece on the board -
   * that is, all white or black pieces’ positions.
   */
  fun positionsOf(piece: Piece): List<Position> =
    board.flatMapIndexed { i, row ->
      row.mapIndexed { j, col ->
        if (col == piece) Position(i, j)
        else null
      }.filterNotNull()
    }

  private fun withinBoard(i: Int): Boolean = i in 0 until BOARD_SIZE

  fun isValidMove(move: Move, lastMove: Move? = null): Boolean {
    val colFrom = move.from.file.value
    val colTo = move.to.file.value
    val rowFrom = move.from.rank.value
    val rowTo = move.to.rank.value
    if (!withinBoard(colFrom) || !withinBoard(colTo) || !withinBoard(rowFrom) || !withinBoard(rowTo)) return false
    val direction = move.piece.direction()
    val distance = (rowTo - rowFrom) * direction
    if (move.type == MoveType.PEACEFUL) {
      // check if in the same column
      if (colFrom != colTo) return false
      // check distance (1 or 2)
      if (distance != 1 && distance != 2) return false
      // check target position needs to be empty
      if (board[rowTo][colTo] != null) return false
      // check the way to the target position needs to be empty
      if (board[rowFrom + direction][colTo] != null) return false
      // if move dist 2, can only move from the starting position
      if (distance == 2) {
        if (move.piece == Piece.BLACK && rowFrom != BLACK_START_FILE) return false
        if (move.piece == Piece.WHITE && rowFrom != WHITE_START_FILE) return false
      }
    } else if (move.type == MoveType.CAPTURE) {
      // needs to move diag
      if (abs(colFrom - colTo) != 1) return false
      // at most move 1 step forward
      if (distance != 1) return false
      // needs to have an opposite piece
      if (board[rowTo][colTo] != move.piece.opposite()) return false
    } else {
      // validate lastMove
      if (lastMove == null) return false
      if (lastMove.type != MoveType.PEACEFUL) return false
      if (lastMove.piece != move.piece.opposite()) return false

      val lastRowFrom = lastMove.from.rank.value
      val lastRowTo = lastMove.to.rank.value
      val lastColTo = lastMove.to.file.value
      val lastDirection = lastMove.piece.direction()
      val lastDistance = (lastRowTo - lastRowFrom) * lastDirection

      // need to move 2
      if (lastDistance != 2) return false
      // need to end up on the same row with the current piece
      if (lastRowTo != rowFrom) return false
      // need to next to the current piece
      if (abs(lastColTo - colFrom) != 1) return false
      // the current piece needs to end up at the same column
      if (colTo != lastColTo) return false
      // the current piece needs to move forward 1 rank
      if (rowTo != rowFrom + direction) return false
    }
    return true
  }

  fun move(m: Move): Board = Board(List(BOARD_SIZE) { i ->
    when (i) {
      m.from.rank.value -> List(BOARD_SIZE) { j ->
        when (j) {
          m.from.file.value -> null
          m.to.file.value -> when (m.type) {
            MoveType.EN_PASSANT -> null
            else -> board[i][j]
          }

          else -> board[i][j]
        }
      }

      m.to.rank.value -> List(BOARD_SIZE) { j ->
        when (j) {
          m.to.file.value -> m.piece
          else -> board[i][j]
        }
      }

      else -> List(BOARD_SIZE) { j ->
        board[i][j]
      }
    }
  })

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
  val board = Board(File(0), File(0))
  println(board)
}
