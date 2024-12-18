package com.example.cher_g42.services

import com.example.cher_g42.domain.game.Board
import com.example.cher_g42.domain.game.Game
import com.example.cher_g42.domain.game.GameService
import com.example.cher_g42.domain.game.GameState
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import com.example.cher_g42.domain.game.createGame
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

/**
 * Names of the fields used in the document representations.
 */
private const val GAME = "game"
private const val BOARD = "board"
private const val ID = "id"
private const val STATE = "state"
private const val CURRENT_PLAYER = "currentPlayer"

/**
 * Implementation of the Game's service using Firebase's Firestore.
 */
class GameFirebaseService(private val db: FirebaseFirestore) : GameService {

    override fun startGame(id: UUID) {
        val game = createGame(id)
        db.collection(GAME)
            .document(id.toString())
            .set(game.toMap())
    }

    override fun updateGame(game: Game) {
        db.collection(GAME)
            .document(game.id.toString())
            .set(game.toMap())
    }

    override fun listenToGame(gameId: String, onGameUpdate: (Game) -> Unit) {
        db.collection(GAME)
            .document(gameId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    // Get game as string an then convert it to Game
                    val game = snapshot.data?.toGame()
                    if (game != null) {
                        onGameUpdate(game)
                    }
                }
            }
    }

    override fun playMove(game: Game, position: Position) {
        val check = game.board.checkAvailableMoves(game.currentPlayer)
        if (position !in check) {
            throw IllegalArgumentException("Invalid move")
        } else {
            val newGame = game.play(position)
            updateGame(newGame)
        }
    }

    override fun pass(game: Game) {
        val newGame = game.pass()
        updateGame(newGame)
    }

    private fun Game.toMap(): Map<String, Any> {
        return mapOf(
            ID to id.toString(),
            BOARD to board.toString(),
            STATE to state.name,
            CURRENT_PLAYER to currentPlayer.name
        )
    }

    private fun Map<String, Any>.toGame(): Game {
        return Game(
            UUID.fromString(this[ID] as String),
            Board.fromString(this[BOARD] as String),
            GameState.valueOf(this[STATE] as String),
            PieceColor.valueOf(this[CURRENT_PLAYER] as String)
        )
    }
}