package com.example.cher_g42.domain.game

import org.junit.Test

import org.junit.Assert.*

class PieceColorTests {
    @Test
    fun opposite_returns_correct_color() {
        assertEquals(PieceColor.WHITE.opposite(), PieceColor.BLACK)
        assertEquals(PieceColor.BLACK.opposite(), PieceColor.WHITE)
        assertEquals(PieceColor.EMPTY.opposite(), PieceColor.EMPTY)
    }
    @Test
    fun same_Color_are_equal() {
        val white = PieceColor.WHITE
        val black = PieceColor.BLACK
        val empty = PieceColor.EMPTY
        assertEquals(white, PieceColor.WHITE)
        assertEquals(black, PieceColor.BLACK)
        assertEquals(empty, PieceColor.EMPTY)
    }
}