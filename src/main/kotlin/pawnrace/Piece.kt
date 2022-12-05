package pawnrace

enum class Piece {
  BLACK, WHITE;

  fun opposite() = when(this) {
    BLACK -> WHITE
    WHITE -> BLACK
  }

  fun direction() = when(this) {
    BLACK -> -1
    WHITE -> 1
  }

  fun endRow() = when(this) {
    BLACK -> 0
    WHITE -> size - 1
  }
}
