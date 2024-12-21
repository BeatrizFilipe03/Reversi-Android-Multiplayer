package com.example.cher_g42.domain.game

import org.junit.Test

import org.junit.Assert.*

class PositionTests {
    @Test
    fun position_from_string() {
        val position = Position.fromString("A,1")
        assertEquals(position.column.value, 'A')
        assertEquals(position.row.value, 1)
    }
    @Test
    fun position_to_pair() {
        val position = Position.fromString("A,1")
        val pair = position.toPair()
        assertEquals(pair.first, 1)
    }
    @Test
    fun position_from_Col_and_Row() {
        val position = Position(column = Column('A'), row = Row(1) )
        assertEquals(position.column.value, 'A')
        assertEquals(position.row.value, 1)
    }
    @Test
    fun testIsValidRowWithValidValue() {
        assertTrue(isValidRow(1,8))
    }
    @Test
    fun testIsInvalidRowWithInvalidValue() {
        assertFalse(isValidRow(9,8))
    }
    @Test
    fun position_to_string() {
        val position = Position(column = Column('A'), row = Row(1) )
        assertEquals(position.toString(), "A,1")

    }

}