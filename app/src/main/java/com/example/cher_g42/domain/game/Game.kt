package com.example.cher_g42.domain.game

import java.util.UUID

typealias scoreBW = Pair<Int,Int>

data class Game(
    val id: UUID,
    val board: Board,
    val state: GameState,
    val currentPlayer: PieceColor
) {

    fun play(position: Position): Game { //como board e gamestate são atualizados ao mesmo tempo o gamestate pode não dizer imediatamente que o jogo que ele já terminou
        return this.copy(
            board = board.play(position, currentPlayer),
            state = if (checkWin()) {
                if (board.score().first == board.score().second) GameState.DRAW else GameState.FINISHED
            } else {GameState.ACTIVE}, //gamestate.finished não mosta quem é o vencedor
            currentPlayer = currentPlayer.opposite()
        )
    }

    private fun checkWin(): Boolean {
        if (board.isBoardFull()) return true
        if (board.checkAvailableMoves(currentPlayer) == emptyList<Position>()) return true
        return false
    }
}

fun createGame(): Game{
    val id = UUID.randomUUID()
    val board = createBoard()
    return Game(id,board,GameState.ACTIVE,PieceColor.BLACK)

}