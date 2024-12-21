package com.example.cher_g42.domain.game

import java.util.UUID

typealias scoreBW = Pair<Int,Int>

data class Game(
    val id: UUID,
    val board: Board,
    val state: GameState,
    val currentPlayer: PieceColor,
    val lastMove : Position? = Position(Column('D'),Row(4)),
) {

    fun play(position: Position): Game {
        // Play the move and get the updated board
        val newBoard = board.play(position, currentPlayer)

        // Check if the game is over with the updated board
        val isGameOver = if (checkIfOver(newBoard)) {
            val winner = winner()
            if (winner != null) GameState.FINISHED else GameState.DRAW
        } else {
            GameState.ACTIVE
        }

        // Return a new game state with the updated board and state
        return this.copy(
            board = newBoard,
            state = isGameOver,
            currentPlayer = currentPlayer.opposite(),
            lastMove = position
        )
    }

    fun pass(): Game {
        return this.copy(
            state = if (checkIfOver(board)) {
                val winner = winner()
                if (winner != null) GameState.FINISHED else GameState.DRAW
            } else {
                GameState.ACTIVE
            },
            currentPlayer = currentPlayer.opposite(),
            lastMove = null
        )
    }

    fun winner(): PieceColor? {
        val (blackScore, whiteScore) = board.score()
        return when {
            blackScore > whiteScore -> PieceColor.BLACK
            whiteScore > blackScore -> PieceColor.WHITE
            else -> null // It's a draw
        }
    }

}

fun createGame(id: UUID): Game{
    val board = createBoard()
    return Game(id,board,GameState.ACTIVE,PieceColor.BLACK)

}