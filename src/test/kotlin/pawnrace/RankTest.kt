package pawnrace

import kotlin.test.Test
import kotlin.test.assertEquals

class RankTest {
  @Test
  fun `test init`() {
    assertEquals(0, Rank('1').rank)
    assertEquals(1, Rank('2').rank)
    assertEquals(2, Rank('3').rank)
    assertEquals(3, Rank('4').rank)
    assertEquals(4, Rank('5').rank)
    assertEquals(5, Rank('6').rank)
    assertEquals(6, Rank('7').rank)
    assertEquals(7, Rank('8').rank)
  }

  @Test
  fun `test toString`() {
    assertEquals("1", Rank('1').toString())
    assertEquals("2", Rank('2').toString())
    assertEquals("3", Rank('3').toString())
    assertEquals("4", Rank('4').toString())
    assertEquals("5", Rank('5').toString())
    assertEquals("6", Rank('6').toString())
    assertEquals("7", Rank('7').toString())
    assertEquals("8", Rank('8').toString())
  }
}
