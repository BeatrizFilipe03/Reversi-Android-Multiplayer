package com.example.cher_g42.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cher_g42.domain.favorites.FavoriteGamesRepository
import com.example.cher_g42.domain.game.Column
import com.example.cher_g42.domain.game.Game
import com.example.cher_g42.domain.game.GameService
import com.example.cher_g42.domain.game.GameState
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import com.example.cher_g42.domain.game.Row
import com.example.cher_g42.domain.game.createGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

sealed class GameStatus {
    data object Loading : GameStatus()
    data object Starting : GameStatus()
    data object Playing : GameStatus()
    data object Waiting : GameStatus()
    data object GameOver : GameStatus()
}

class GameViewModel(
    private val challenge: String,
    private val colorChosen: String,
    private val opponentName: String,
    private val service: GameService,
    private val favoriteGames: FavoriteGamesRepository
) : ViewModel() {
    private val _state = MutableStateFlow<GameStatus>(GameStatus.Starting)
    val state: StateFlow<GameStatus> = _state

    private val _game = MutableStateFlow(createGame(UUID.fromString(challenge)))
    val game: StateFlow<Game> = _game

    private val _movesHistory = MutableStateFlow<List<Pair<Position?, PieceColor>>>(emptyList())
    private val movesHistory: StateFlow<List<Pair<Position?, PieceColor>>> = _movesHistory

    private val pieceColor = PieceColor.valueOf(colorChosen)

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
                    }
                }
            }
        }
    }

    private fun startGame() {
        check(_state.value == GameStatus.Starting) { "Game already started" }
        check(pieceColor == PieceColor.BLACK) { "Only black can start the game" }
        _state.value = GameStatus.Loading
        viewModelScope.launch {
            service.startGame(UUID.fromString(challenge))
            _state.value = GameStatus.Playing
        }
    }

    fun playMove(position: Position) {
        viewModelScope.launch {
            if (state.value is GameStatus.Playing) {
                try {
                    val lastMove = game.value.lastMove
                    if (lastMove == null) {
                        _movesHistory.value += (null to game.value.currentPlayer.opposite())
                    } else if (lastMove != Position(Column('D'), Row(4)) && movesHistory.value.none { it.first == lastMove }) {
                        _movesHistory.value += (game.value.lastMove to game.value.currentPlayer.opposite())
                    }
                    service.playMove(game.value, position)
                    _movesHistory.value += (position to pieceColor)
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
                _movesHistory.value += (game.value.lastMove to game.value.currentPlayer.opposite())
                service.pass(game.value)
                _movesHistory.value += (null to pieceColor)
                _state.value = GameStatus.Waiting
            }
        }
    }

    private fun listenToGame() {
        viewModelScope.launch {
            service.listenToGame(challenge) { game ->
                _game.value = game
                if (game.state == GameState.FINISHED) {
                    val lastMove = game.lastMove
                    if (movesHistory.value.none { it.first == lastMove }) {
                        _movesHistory.value += (lastMove to game.currentPlayer.opposite())
                    }
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

    fun addToFavorites() {
        service.deleteGame(UUID.fromString(challenge))
        viewModelScope.launch {
            val game = game.value
            val now = LocalDateTime.now()
            val winner = getWinner()
            favoriteGames.addFavoriteGame(game.id, opponentName, now, movesHistory.value, winner)
        }
    }

    class GameViewModelFactory(private val challenge: String, private val colorChosen: String, private val opponentName: String, private val game: GameService, private val favoriteGames: FavoriteGamesRepository) : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return GameViewModel(challenge, colorChosen, opponentName, game, favoriteGames) as T
        }
    }
}