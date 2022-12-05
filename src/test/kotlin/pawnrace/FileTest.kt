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
    assertEquals("a", File('A').toString())
    assertEquals("b", File('B').toString())
    assertEquals("c", File('C').toString())
    assertEquals("d", File('D').toString())
    assertEquals("e", File('E').toString())
    assertEquals("f", File('F').toString())
    assertEquals("g", File('G').toString())
    assertEquals("h", File('H').toString())
  }
}
