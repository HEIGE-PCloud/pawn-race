package pawnrace

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class BoardTest {
  private val board1 = Board(File(0), File(0))
  private val board2 = Board(File(0), File(7))

  @Test
  fun `test init`() {
    assertEquals(null, board1.pieceAt(Position(0, 0)))
    assertEquals(null, board1.pieceAt(Position(0, 1)))
    assertEquals(null, board1.pieceAt(Position(0, 2)))
    assertEquals(null, board1.pieceAt(Position(0, 3)))
    assertEquals(null, board1.pieceAt(Position(0, 4)))
    assertEquals(null, board1.pieceAt(Position(0, 5)))
    assertEquals(null, board1.pieceAt(Position(0, 6)))
    assertEquals(null, board1.pieceAt(Position(0, 7)))

    assertEquals(null, board1.pieceAt(Position(1, 0)))
    assertEquals(Piece.WHITE, board1.pieceAt(Position(1, 1)))
    assertEquals(Piece.WHITE, board1.pieceAt(Position(1, 2)))
    assertEquals(Piece.WHITE, board1.pieceAt(Position(1, 3)))
    assertEquals(Piece.WHITE, board1.pieceAt(Position(1, 4)))
    assertEquals(Piece.WHITE, board1.pieceAt(Position(1, 5)))
    assertEquals(Piece.WHITE, board1.pieceAt(Position(1, 6)))
    assertEquals(Piece.WHITE, board1.pieceAt(Position(1, 7)))

    assertEquals(null, board1.pieceAt(Position(2, 0)))
    assertEquals(null, board1.pieceAt(Position(2, 1)))
    assertEquals(null, board1.pieceAt(Position(2, 2)))
    assertEquals(null, board1.pieceAt(Position(2, 3)))
    assertEquals(null, board1.pieceAt(Position(2, 4)))
    assertEquals(null, board1.pieceAt(Position(2, 5)))
    assertEquals(null, board1.pieceAt(Position(2, 6)))
    assertEquals(null, board1.pieceAt(Position(2, 7)))

    assertEquals(null, board1.pieceAt(Position(3, 0)))
    assertEquals(null, board1.pieceAt(Position(3, 1)))
    assertEquals(null, board1.pieceAt(Position(3, 2)))
    assertEquals(null, board1.pieceAt(Position(3, 3)))
    assertEquals(null, board1.pieceAt(Position(3, 4)))
    assertEquals(null, board1.pieceAt(Position(3, 5)))
    assertEquals(null, board1.pieceAt(Position(3, 6)))
    assertEquals(null, board1.pieceAt(Position(3, 7)))

    assertEquals(null, board1.pieceAt(Position(4, 0)))
    assertEquals(null, board1.pieceAt(Position(4, 1)))
    assertEquals(null, board1.pieceAt(Position(4, 2)))
    assertEquals(null, board1.pieceAt(Position(4, 3)))
    assertEquals(null, board1.pieceAt(Position(4, 4)))
    assertEquals(null, board1.pieceAt(Position(4, 5)))
    assertEquals(null, board1.pieceAt(Position(4, 6)))
    assertEquals(null, board1.pieceAt(Position(4, 7)))

    assertEquals(null, board1.pieceAt(Position(5, 0)))
    assertEquals(null, board1.pieceAt(Position(5, 1)))
    assertEquals(null, board1.pieceAt(Position(5, 2)))
    assertEquals(null, board1.pieceAt(Position(5, 3)))
    assertEquals(null, board1.pieceAt(Position(5, 4)))
    assertEquals(null, board1.pieceAt(Position(5, 5)))
    assertEquals(null, board1.pieceAt(Position(5, 6)))
    assertEquals(null, board1.pieceAt(Position(5, 7)))

    assertEquals(null, board1.pieceAt(Position(6, 0)))
    assertEquals(Piece.BLACK, board1.pieceAt(Position(6, 1)))
    assertEquals(Piece.BLACK, board1.pieceAt(Position(6, 2)))
    assertEquals(Piece.BLACK, board1.pieceAt(Position(6, 3)))
    assertEquals(Piece.BLACK, board1.pieceAt(Position(6, 4)))
    assertEquals(Piece.BLACK, board1.pieceAt(Position(6, 5)))
    assertEquals(Piece.BLACK, board1.pieceAt(Position(6, 6)))
    assertEquals(Piece.BLACK, board1.pieceAt(Position(6, 7)))

    assertEquals(null, board1.pieceAt(Position(7, 0)))
    assertEquals(null, board1.pieceAt(Position(7, 1)))
    assertEquals(null, board1.pieceAt(Position(7, 2)))
    assertEquals(null, board1.pieceAt(Position(7, 3)))
    assertEquals(null, board1.pieceAt(Position(7, 4)))
    assertEquals(null, board1.pieceAt(Position(7, 5)))
    assertEquals(null, board1.pieceAt(Position(7, 6)))
    assertEquals(null, board1.pieceAt(Position(7, 7)))
  }

  @Test
  fun `test init 2`() {
    assertEquals(null, board2.pieceAt(Position(0, 0)))
    assertEquals(null, board2.pieceAt(Position(0, 1)))
    assertEquals(null, board2.pieceAt(Position(0, 2)))
    assertEquals(null, board2.pieceAt(Position(0, 3)))
    assertEquals(null, board2.pieceAt(Position(0, 4)))
    assertEquals(null, board2.pieceAt(Position(0, 5)))
    assertEquals(null, board2.pieceAt(Position(0, 6)))
    assertEquals(null, board2.pieceAt(Position(0, 7)))

    assertEquals(null, board2.pieceAt(Position(1, 0)))
    assertEquals(Piece.WHITE, board2.pieceAt(Position(1, 1)))
    assertEquals(Piece.WHITE, board2.pieceAt(Position(1, 2)))
    assertEquals(Piece.WHITE, board2.pieceAt(Position(1, 3)))
    assertEquals(Piece.WHITE, board2.pieceAt(Position(1, 4)))
    assertEquals(Piece.WHITE, board2.pieceAt(Position(1, 5)))
    assertEquals(Piece.WHITE, board2.pieceAt(Position(1, 6)))
    assertEquals(Piece.WHITE, board2.pieceAt(Position(1, 7)))

    assertEquals(null, board2.pieceAt(Position(2, 0)))
    assertEquals(null, board2.pieceAt(Position(2, 1)))
    assertEquals(null, board2.pieceAt(Position(2, 2)))
    assertEquals(null, board2.pieceAt(Position(2, 3)))
    assertEquals(null, board2.pieceAt(Position(2, 4)))
    assertEquals(null, board2.pieceAt(Position(2, 5)))
    assertEquals(null, board2.pieceAt(Position(2, 6)))
    assertEquals(null, board2.pieceAt(Position(2, 7)))

    assertEquals(null, board2.pieceAt(Position(3, 0)))
    assertEquals(null, board2.pieceAt(Position(3, 1)))
    assertEquals(null, board2.pieceAt(Position(3, 2)))
    assertEquals(null, board2.pieceAt(Position(3, 3)))
    assertEquals(null, board2.pieceAt(Position(3, 4)))
    assertEquals(null, board2.pieceAt(Position(3, 5)))
    assertEquals(null, board2.pieceAt(Position(3, 6)))
    assertEquals(null, board2.pieceAt(Position(3, 7)))

    assertEquals(null, board2.pieceAt(Position(4, 0)))
    assertEquals(null, board2.pieceAt(Position(4, 1)))
    assertEquals(null, board2.pieceAt(Position(4, 2)))
    assertEquals(null, board2.pieceAt(Position(4, 3)))
    assertEquals(null, board2.pieceAt(Position(4, 4)))
    assertEquals(null, board2.pieceAt(Position(4, 5)))
    assertEquals(null, board2.pieceAt(Position(4, 6)))
    assertEquals(null, board2.pieceAt(Position(4, 7)))

    assertEquals(null, board2.pieceAt(Position(5, 0)))
    assertEquals(null, board2.pieceAt(Position(5, 1)))
    assertEquals(null, board2.pieceAt(Position(5, 2)))
    assertEquals(null, board2.pieceAt(Position(5, 3)))
    assertEquals(null, board2.pieceAt(Position(5, 4)))
    assertEquals(null, board2.pieceAt(Position(5, 5)))
    assertEquals(null, board2.pieceAt(Position(5, 6)))
    assertEquals(null, board2.pieceAt(Position(5, 7)))

    assertEquals(Piece.BLACK, board2.pieceAt(Position(6, 0)))
    assertEquals(Piece.BLACK, board2.pieceAt(Position(6, 1)))
    assertEquals(Piece.BLACK, board2.pieceAt(Position(6, 2)))
    assertEquals(Piece.BLACK, board2.pieceAt(Position(6, 3)))
    assertEquals(Piece.BLACK, board2.pieceAt(Position(6, 4)))
    assertEquals(Piece.BLACK, board2.pieceAt(Position(6, 5)))
    assertEquals(Piece.BLACK, board2.pieceAt(Position(6, 6)))
    assertEquals(null, board2.pieceAt(Position(6, 7)))

    assertEquals(null, board2.pieceAt(Position(7, 0)))
    assertEquals(null, board2.pieceAt(Position(7, 1)))
    assertEquals(null, board2.pieceAt(Position(7, 2)))
    assertEquals(null, board2.pieceAt(Position(7, 3)))
    assertEquals(null, board2.pieceAt(Position(7, 4)))
    assertEquals(null, board2.pieceAt(Position(7, 5)))
    assertEquals(null, board2.pieceAt(Position(7, 6)))
    assertEquals(null, board2.pieceAt(Position(7, 7)))
  }

  @Test
  fun `test positionOf`() {
    assertContentEquals(
      listOf(
        Position(1, 1),
        Position(1, 2),
        Position(1, 3),
        Position(1, 4),
        Position(1, 5),
        Position(1, 6),
        Position(1, 7),
      ), board1.positionsOf(Piece.WHITE)
    )
    assertContentEquals(
      listOf(
        Position(6, 1),
        Position(6, 2),
        Position(6, 3),
        Position(6, 4),
        Position(6, 5),
        Position(6, 6),
        Position(6, 7),
      ), board1.positionsOf(Piece.BLACK)
    )
  }

  @Test
  fun `test positionOf 2`() {
    assertContentEquals(
      listOf(
        Position(1, 1),
        Position(1, 2),
        Position(1, 3),
        Position(1, 4),
        Position(1, 5),
        Position(1, 6),
        Position(1, 7),
      ), board2.positionsOf(Piece.WHITE)
    )
    assertContentEquals(
      listOf(
        Position(6, 0),
        Position(6, 1),
        Position(6, 2),
        Position(6, 3),
        Position(6, 4),
        Position(6, 5),
        Position(6, 6),
      ), board2.positionsOf(Piece.BLACK)
    )
  }

}
