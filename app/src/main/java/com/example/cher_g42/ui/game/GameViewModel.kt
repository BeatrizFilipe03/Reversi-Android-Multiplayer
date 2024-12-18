package com.example.cher_g42.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cher_g42.domain.game.Game
import com.example.cher_g42.domain.game.GameService
import com.example.cher_g42.domain.game.GameState
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import com.example.cher_g42.domain.game.createGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class GameStatus {
    object Starting : GameStatus()
    object Playing : GameStatus()
    object Waiting : GameStatus()
    object GameOver : GameStatus()
}

class GameViewModel(
    private val challenge: String,
    private val colorChosen: String,
    private val playerName: String,
    private val playerId: String,
    private val service: GameService
) : ViewModel() {
    private val _state = MutableStateFlow<GameStatus>(GameStatus.Starting)
    val state: StateFlow<GameStatus> = _state

    private val _game = MutableStateFlow<Game>(createGame(UUID.fromString(challenge)))
    val game: StateFlow<Game> = _game

    val pieceColor = PieceColor.valueOf(colorChosen)

    init {
        observeState()
    }

    private fun observeState() {
        viewModelScope.launch {
            _state.collect { state ->
                when (state) {
                    is GameStatus.Starting -> {
                       if (pieceColor == PieceColor.BLACK) {
                           startGame()
                       } else {
                            _state.value = GameStatus.Waiting
                       }
                    }
                    is GameStatus.Waiting -> {
                        listenToGame()
                    }
                    else -> {
                        // Optional: handle other state transitions
                    }
                }
            }
        }
    }

    private fun startGame() {
        check(pieceColor == PieceColor.BLACK) { "Only black can start the game" }
        check(_state.value == GameStatus.Starting) { "Game already started" }
        service.startGame(UUID.fromString(challenge))
        _state.value = GameStatus.Playing
    }

    fun playMove(position: Position) {
        viewModelScope.launch {
            if (state.value is GameStatus.Playing) {
                try {
                    service.playMove(game.value, position)
                    _state.value = GameStatus.Waiting
                } catch (e: IllegalArgumentException) {
                    _state.value = GameStatus.Playing
                }
            }
        }
    }

    fun pass() {
        viewModelScope.launch {
            if (state.value is GameStatus.Playing) {
                service.pass(game.value)
                _state.value = GameStatus.Waiting
            }
        }
    }

    private fun listenToGame() {
        viewModelScope.launch {
            service.listenToGame(challenge) { game ->
                _game.value = game
                if (game.state == GameState.FINISHED) {
                    _state.value = GameStatus.GameOver
                } else if (game.currentPlayer == pieceColor) {
                    _state.value = GameStatus.Playing
                }
            }
        }
    }

    fun getWinner(): PieceColor? {
        return game.value.winner()
    }

    class GameViewModelFactory(private val challenge: String, private val colorChosen: String, private val playerName: String, private val playerId: String, private val game: GameService) : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return GameViewModel(challenge, colorChosen, playerName, playerId, game) as T
        }
    }
}