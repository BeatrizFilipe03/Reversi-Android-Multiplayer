package com.example.cher_g42.domain.game

import org.junit.Test

import org.junit.Assert.*

class RowTests {
    @Test
    fun row_value_in_range() {
        val row= Row(1)
        assertEquals(row.value,1)
    }
    @Test
    fun row_value_out_of_range() {
        assertThrows(IllegalArgumentException::class.java) {
            Row(0)
        }
    }
    @Test
    fun rowsWithTheSameValueAreEqual(){
        val row1 = Row(1)
        val row2 = Row(1)
        assertEquals(row1,row2)
    }
}