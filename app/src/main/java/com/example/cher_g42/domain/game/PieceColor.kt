package com.example.cher_g42.domain.game

enum class PieceColor {
    WHITE,
    BLACK,
    EMPTY;

    fun opposite(): PieceColor {
        return when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
            EMPTY -> EMPTY
        }
    }
}