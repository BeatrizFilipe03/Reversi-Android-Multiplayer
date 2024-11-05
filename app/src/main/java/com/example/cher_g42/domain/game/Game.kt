package com.example.cher_g42.domain.game

import java.util.UUID

data class Game(
    val id: UUID,
    val board: Board,
    val state: GameState,
    val currentPlayer: PieceColor
) {

    fun play(move: Piece): Game {
        // TODO - Implement the play function
        return this
    }

    private fun checkWin(): Boolean {
        // TODO - Implement the checkWin function
        return false
    }
}