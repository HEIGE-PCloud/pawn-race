package pawnrace

import java.util.concurrent.Executors
import kotlin.test.Test

class PlayerTest {
  @Test
  fun `test player`() {
    val executor = Executors.newFixedThreadPool(10)

    val player1 = Player(Piece.WHITE)
    val player2 = Player(Piece.BLACK, player1)
    player1.opponent = player2
    val board = Board(
      arrayOf(
        "........",
        "BBBBBBB.",
        "........",
        "........",
        "........",
        "........",
        ".WWWWWWW",
        "........",
      )
    )
    val game = Game(board, player1)
    player1.makeMove(game, executor, System.nanoTime() / 1000000, 5000)
  }
}
