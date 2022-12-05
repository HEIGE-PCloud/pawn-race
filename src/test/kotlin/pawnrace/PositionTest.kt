package pawnrace

import kotlin.test.Test
import kotlin.test.assertEquals

class PositionTest {
  @Test
  fun `test toString`() {
    assertEquals("A1", Position("A1").toString())
    assertEquals("A1", Position("a1").toString())
  }
}
