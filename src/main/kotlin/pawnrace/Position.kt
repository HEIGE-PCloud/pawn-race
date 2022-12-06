package pawnrace

class Position(row: Int, col: Int) {
  val rank = Rank(row)
  val file = File(col)

  /**
   * The constructor takes this parameter and initialises a position
   * with the given coordinates in the short algebraic notation,
   * for example A4 or a4.
   */
  constructor(pos: String) : this(Rank(pos[1]).value, File(pos[0]).value)

  fun leftBack(): Position? = when {
    rank.value > 0 && file.value > 0  -> Position(rank.value - 1, file.value - 1)
    else -> null
  }
  fun leftFront(): Position? = when {
    rank.value < BOARD_SIZE - 1 && file.value > 0 -> Position(rank.value + 1, file.value - 1)
    else -> null
  }
  fun rightBack(): Position? = when {
    rank.value > 0 && file.value < BOARD_SIZE - 1 -> Position(rank.value - 1, file.value + 1)
    else -> null
  }
  fun rightFront(): Position? = when {
    rank.value < BOARD_SIZE - 1 && file.value < BOARD_SIZE - 1 -> Position(rank.value + 1, file.value + 1)
    else -> null
  }

  /**
   * Returns the short algebraic notation of this position.
   */
  override fun toString(): String =
    file.toString() + rank.toString()

  override fun equals(other: Any?): Boolean = when (other) {
    is Position -> file == other.file && rank == other.rank
    else -> false
  }

  override fun hashCode(): Int = 31 * file.hashCode() + rank.hashCode()
}
