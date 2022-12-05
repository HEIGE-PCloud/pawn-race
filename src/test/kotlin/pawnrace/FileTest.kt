package pawnrace

import kotlin.test.Test
import kotlin.test.assertEquals

class FileTest {
  @Test
  fun `test init`() {
    assertEquals(0, File('A').value)
    assertEquals(1, File('B').value)
    assertEquals(2, File('C').value)
    assertEquals(3, File('D').value)
    assertEquals(4, File('E').value)
    assertEquals(5, File('F').value)
    assertEquals(6, File('G').value)
    assertEquals(7, File('H').value)

    assertEquals(0, File('a').value)
    assertEquals(1, File('b').value)
    assertEquals(2, File('c').value)
    assertEquals(3, File('d').value)
    assertEquals(4, File('e').value)
    assertEquals(5, File('f').value)
    assertEquals(6, File('g').value)
    assertEquals(7, File('h').value)
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
