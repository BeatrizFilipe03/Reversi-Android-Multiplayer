package com.example.cher_g42.ui.favorites

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cher_g42.domain.favorites.FavoriteGame
import com.example.cher_g42.domain.favorites.FavoriteGamesRepository
import com.example.cher_g42.domain.game.Board
import com.example.cher_g42.domain.game.GameState
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import com.example.cher_g42.domain.game.checkIfOver
import com.example.cher_g42.domain.game.createBoard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class FavoritesState {
    data object Loading : FavoritesState()
    data object Loaded : FavoritesState()
    data class Watching(val gameId: UUID, val winner: PieceColor) : FavoritesState()
}

class FavoritesViewModel (
    private val favoriteGamesRepository: FavoriteGamesRepository
) : ViewModel() {
    private val _favorites = MutableStateFlow<List<FavoriteGame>>(emptyList())
    val favorites: StateFlow<List<FavoriteGame>> = _favorites

    private val _currentIndex = mutableIntStateOf(0)

    private val _state = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val state: StateFlow<FavoritesState> = _state

    private val _board = MutableStateFlow<Board?>(null)
    val board: StateFlow<Board?> = _board

    private val _currentStatus = MutableStateFlow(Pair(GameState.ACTIVE, PieceColor.BLACK))
    val currentStatus: StateFlow<Pair<GameState, PieceColor>> = _currentStatus

    private val _playOrder = MutableStateFlow<List<Pair<Position?, PieceColor>>>(emptyList())

    init {
        observeState()
    }

    private fun observeState() {
        viewModelScope.launch {
            _state.collect { state ->
                when (state) {
                    is FavoritesState.Loading -> {
                        getFavorites()
                    }
                    is FavoritesState.Watching -> {
                        val gameId = state.gameId
                        val favoriteGame = _favorites.value.first { it.id == gameId }
                        _playOrder.value = favoriteGame.playOrder
                        Log.v("FavoritesViewModel", "Play order: ${_playOrder.value}")
                        updateBoard()
                    }
                    else -> {
                }
            }
        }
    }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            _favorites.value = favoriteGamesRepository.getAllFavoriteGames()
            _state.value = FavoritesState.Loaded
        }
    }

    fun onWatch(gameId: UUID, winner: PieceColor) {
        _state.value = FavoritesState.Watching(gameId, winner)
    }

    fun moveForward() {
        if (_currentIndex.intValue < _playOrder.value.size) {
            _currentIndex.intValue += 1
            updateBoard()
        }
    }

    fun moveBackward() {
        if (_currentIndex.intValue > 0) {
            _currentIndex.intValue -= 1
            updateBoard()
        }
    }

    private fun updateBoard() {
        _board.value = recreateBoardFromPlays(_playOrder.value.take(_currentIndex.intValue))
        updateCurrentStatus()
    }

    private fun updateCurrentStatus() {
        if (_board.value == null) {
            return
        }
        val favState = state.value as FavoritesState.Watching
        val isOver = checkIfOver(_board.value!!)
        val winner = favState.winner
        val currentPiece = if (isOver) winner else _playOrder.value[_currentIndex.intValue].second
        val state = if (isOver && winner == PieceColor.EMPTY) {
            GameState.DRAW
        } else if (isOver) {
            GameState.FINISHED
        } else {
            GameState.ACTIVE
        }
        _currentStatus.value = Pair(state, currentPiece)
    }

    private fun recreateBoardFromPlays(plays: List<Pair<Position?, PieceColor>>): Board {
        var board = createBoard()
        for ((position, color) in plays) {
            if (position == null) {
                continue
            }
            board = board.play(position, color) // Update the board with each play
        }
        return board
    }

    fun onNavigateBack() {
        _currentIndex.intValue = 0
        _board.value = null
        _playOrder.value = emptyList()
        _state.value = FavoritesState.Loaded
    }

    class FavoritesViewModelFactory(private val favoriteGamesRepository: FavoriteGamesRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoritesViewModel(favoriteGamesRepository) as T
        }
    }

}