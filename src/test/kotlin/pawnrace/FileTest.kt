package pawnrace

import kotlin.test.Test
import kotlin.test.assertEquals

class FileTest {
  @Test
  fun `test init`() {
    assertEquals(0, File('A').rank)
    assertEquals(1, File('B').rank)
    assertEquals(2, File('C').rank)
    assertEquals(3, File('D').rank)
    assertEquals(4, File('E').rank)
    assertEquals(5, File('F').rank)
    assertEquals(6, File('G').rank)
    assertEquals(7, File('H').rank)

    assertEquals(0, File('a').rank)
    assertEquals(1, File('b').rank)
    assertEquals(2, File('c').rank)
    assertEquals(3, File('d').rank)
    assertEquals(4, File('e').rank)
    assertEquals(5, File('f').rank)
    assertEquals(6, File('g').rank)
    assertEquals(7, File('h').rank)
  }

  @Test
  fun `test toString`() {
    assertEquals("A", File('A').toString())
    assertEquals("B", File('B').toString())
    assertEquals("C", File('C').toString())
    assertEquals("D", File('D').toString())
    assertEquals("E", File('E').toString())
    assertEquals("F", File('F').toString())
    assertEquals("G", File('G').toString())
    assertEquals("H", File('H').toString())
  }
}
