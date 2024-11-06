package com.example.cher_g42.domain.game

data class Column(val value: Char) {
    init {
        require(value in 'A'..'H') { "Invalid column value" }
    }
    fun toInt(): Int {
        return value.code - 'A'.code + 1
    }
}
fun Int.toColumn(): Column {
    require(this in 1..8) { "Invalid column value" }
    return Column('A' + this - 1)
}