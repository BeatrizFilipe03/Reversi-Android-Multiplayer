package com.example.cher_g42.domain.game

import java.util.UUID

interface GameService {
    fun startGame(id : UUID)
    fun updateGame(game: Game)
    fun listenToGame(gameId: String, onGameUpdate: (Game) -> Unit)
    fun playMove(game: Game, position: Position)
    fun pass(game: Game)
}