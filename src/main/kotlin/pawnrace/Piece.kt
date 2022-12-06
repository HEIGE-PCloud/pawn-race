package pawnrace

fun Piece(c: Char): Piece = when(c) {
  'W' -> Piece.WHITE
  else -> Piece.BLACK
}

enum class Piece {
  BLACK, WHITE;

  /**
   * Return the opposite Piece
   */
  fun opposite() = when(this) {
    BLACK -> WHITE
    WHITE -> BLACK
  }

  /**
   * Return the direction of the current piece,
   * white piece should move upwards (+1)
   * black piece should move downwards (-1)
   */
  fun direction() = when(this) {
    BLACK -> -1
    WHITE -> 1
  }

  /**
   * Return the rank in which the piece will get promoted
   * white piece need to reach the BOARD_SIZE - 1 rank
   * black piece need to reach the 0 rank
   */
  fun promotionRank() = when(this) {
    BLACK -> 0
    WHITE -> BOARD_SIZE - 1
  }

  fun startingRank() = when(this) {
    BLACK -> BLACK_START_RANK
    WHITE -> WHITE_START_RANK
  }
}
