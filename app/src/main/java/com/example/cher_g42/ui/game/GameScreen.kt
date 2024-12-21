package com.example.cher_g42.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.cher_g42.ui.common.LoadingScreen
import com.example.cher_g42.ui.game.views.GameMainScreen
import com.example.cher_g42.ui.game.views.GameMainScreenOver

@Composable
fun GameScreen(viewModel: GameViewModel, onNavigateHome: () -> Unit) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is GameStatus.Loading -> {
            LoadingScreen()
        }
        is GameStatus.Waiting -> {
            GameMainScreen(viewModel, false)
        }
        is GameStatus.Playing -> {
            GameMainScreen(viewModel, true)
        }
        is GameStatus.GameOver -> {
            GameMainScreenOver(viewModel, onNavigateHome)
        }
        else -> {}
    }
}