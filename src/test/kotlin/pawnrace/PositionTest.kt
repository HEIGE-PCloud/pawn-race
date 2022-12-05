package pawnrace

import kotlin.test.Test
import kotlin.test.assertEquals

class PositionTest {
  @Test
  fun `test init`() {
    assertEquals(File('a'), Position("A1").file)
    assertEquals(Rank('1'), Position("A1").rank)
  }

  @Test
  fun `test toString`() {
    assertEquals("a1", Position("A1").toString())
    assertEquals("a1", Position("a1").toString())
    assertEquals("a2", Position("A2").toString())
    assertEquals("a2", Position("a2").toString())
  }
}
