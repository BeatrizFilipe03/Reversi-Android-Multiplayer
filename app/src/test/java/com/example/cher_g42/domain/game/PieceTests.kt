package com.example.cher_g42.domain.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PieceTests {
    @Test
    fun testValidCell() {
        val cell = Cell(Position.fromString("A,1"),PieceColor.WHITE)
        assertEquals(cell.position.toString(), "A,1")
        assertEquals(cell.color, PieceColor.WHITE)
    }
    @Test
    fun testValidCellWithNullColor() {
        val cell = Cell(Position.fromString("A,1"),null)
        assertEquals(cell.position.toString(), "A,1")
        assertEquals(cell.color, null)
    }
    @Test
    fun testInvalidCell() {
        assertThrows(IllegalArgumentException::class.java) {
            Cell(Position.fromString("A,0"), PieceColor.WHITE)
        }
        assertThrows(IllegalArgumentException::class.java) {
            Cell(Position.fromString("Z,1"), PieceColor.WHITE)
        }

    }
}