package com.example.cher_g42.domain.game

import java.util.UUID

data class Game(
    val id: UUID,
    val board: Board,
    val state: GameState,
    val currentPlayer: PieceColor
) {

    fun play(position: Position): Game { //not finish
        return this.copy(board = board.play(position, currentPlayer), currentPlayer = currentPlayer.opposite())
    }

    private fun checkWin(): Boolean {
        if(board.isBoardFull()) return true
        if(board.checkAvailableMoves(currentPlayer) == emptyList<Position>()) return true
        return false
    }
}