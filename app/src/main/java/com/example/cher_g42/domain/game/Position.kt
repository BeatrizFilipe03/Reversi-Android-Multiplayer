package com.example.cher_g42.domain.game

data class Position(val column: Column, val row: Row) {
}

fun Pair<Int, Int>.toPosition(): Position {
    return Position(this.first.toColumn(), Row(this.second))
}
fun Position.toPair(): Pair<Int, Int> {
    return Pair(this.column.toInt(), this.row.value)
}
fun isValidRow(value: Int,boardSize:Int) = value in 1 .. boardSize

/**
 * Checks whether [value] is a valid column index
 */
fun isValidColumn(value: Int,boardSize:Int) = value in 1 .. boardSize