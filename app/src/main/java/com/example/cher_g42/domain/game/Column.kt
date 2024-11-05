package com.example.cher_g42.domain.game

data class Column(val value: Char) {
    init {
        require(value in 'A'..'H') { "Invalid column value" }
    }
}