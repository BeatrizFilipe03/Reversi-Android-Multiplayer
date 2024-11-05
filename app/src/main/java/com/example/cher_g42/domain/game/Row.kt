package com.example.cher_g42.domain.game

data class Row(val value: Int) {
    init {
        require(value in 1..8) { "Invalid row value" }
    }
}