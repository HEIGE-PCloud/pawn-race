package pawnrace

import org.junit.Test
import kotlin.test.assertEquals

class PieceTest {
  private val black = Piece.BLACK
  private val white = Piece.WHITE

  @Test
  fun `test string representations`() {
    assertEquals("BLACK", black.toString())
    assertEquals("WHITE", white.toString())
  }

  @Test
  fun `test opposite`() {
    assertEquals(Piece.WHITE, black.opposite())
    assertEquals(Piece.BLACK, white.opposite())
  }
}
