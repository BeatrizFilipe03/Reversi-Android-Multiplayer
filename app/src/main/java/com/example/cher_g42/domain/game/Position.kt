package com.example.cher_g42.domain.game

data class Position(val column: Column, val row: Row) {
    override fun toString(): String {
        return "${column.value},${row.value}"
    }

    companion object {
        fun fromString(value: String): Position {
            val (column, row) = value.split(",")
            return Position(column.toColumn(), Row(row.toInt()))
        }
    }
}

fun Position.toPair(): Pair<Int, Int> {
    return Pair(this.column.toInt(), this.row.value)
}
fun isValidRow(value: Int,boardSize:Int) = value in 1 .. boardSize

/**
 * Checks whether [value] is a valid column index
 */
fun isValidColumn(value: Int,boardSize:Int) = value in 1 .. boardSize