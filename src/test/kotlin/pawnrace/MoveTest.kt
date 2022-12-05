package pawnrace

import kotlin.test.Test
import kotlin.test.assertEquals

class MoveTest {
  @Test
  fun `test toString`() {
    assertEquals(
      "b4",
      Move(
        Piece.WHITE,
        Position("b2"),
        Position("b4"),
        MoveType.PEACEFUL
      ).toString()
    )

    assertEquals(
      "bxc6",
      Move(
        Piece.WHITE,
        Position("b5"),
        Position("c6"),
        MoveType.CAPTURE
      ).toString()
    )
  }
}
