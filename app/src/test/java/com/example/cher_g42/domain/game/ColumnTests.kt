package com.example.cher_g42.domain.game

import org.junit.Test

import org.junit.Assert.*

class ColumnTests {
    @Test
    fun column_value_in_range() {
        val column= Column('A')
        assertEquals(column.value,'A')
    }
    @Test
    fun column_value_out_of_range() {
        assertThrows(IllegalArgumentException::class.java) {
            Column('Z')
        }
    }
    @Test
    fun columnsWithTheSameValueAreEqual() {
        val column1 = Column('A')
        val column2 = Column('A')
        assertEquals(column1, column2)
    }
    @Test
    fun validIntToColumn() {
        val column = 1.toColumn()
        assertEquals(column.value, 'A')
    }
    @Test
    fun invalidIntToColumn() {
        assertThrows(IllegalArgumentException::class.java) {
            0.toColumn()
        }
    }
    @Test
    fun validStringToColumn() {
        val column = "A".toColumn()
        assertEquals(column.value, 'A')
    }
    @Test
    fun invalidStringToColumn() {
        assertThrows(IllegalArgumentException::class.java) {
            "Z".toColumn()
        }
        assertThrows(IllegalArgumentException::class.java) {
            "AA".toColumn()
        }
    }
}