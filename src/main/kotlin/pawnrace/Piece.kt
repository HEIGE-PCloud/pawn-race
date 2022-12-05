package pawnrace

enum class Piece {
  BLACK, WHITE;

  fun opposite() = when(this) {
    BLACK -> WHITE
    WHITE -> BLACK
  }
}
