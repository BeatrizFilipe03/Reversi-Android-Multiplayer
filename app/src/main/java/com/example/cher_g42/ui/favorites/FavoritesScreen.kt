package com.example.cher_g42.ui.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.cher_g42.ui.common.LoadingScreen
import com.example.cher_g42.ui.favorites.views.FavoritesListScreen
import com.example.cher_g42.ui.favorites.views.RewatchGameScreen

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel, onNavigateToHome: () -> Unit) {
    val state by viewModel.state.collectAsState()
    when (state) {
        is FavoritesState.Loading -> {
            LoadingScreen()
        }
        is FavoritesState.Loaded -> {
            FavoritesListScreen(viewModel, onNavigateToHome)
        }
        is FavoritesState.Watching -> {
            val board by viewModel.board.collectAsState()
            val status by viewModel.currentStatus.collectAsState()
            RewatchGameScreen(
                board = board!!,
                onPrevious = { viewModel.moveBackward() },
                onNext = { viewModel.moveForward() },
                onNavigateBack = { viewModel.onNavigateBack() },
                status = status,
                winner = (state as FavoritesState.Watching).winner
            )
        }
    }
}