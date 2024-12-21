package com.example.cher_g42.ui.game.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cher_g42.domain.game.GameState
import com.example.cher_g42.ui.game.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameMainScreen(viewModel: GameViewModel, turn: Boolean, over: Boolean = false) {
    val game by viewModel.game.collectAsState()
    val availableMoves = game.board.checkAvailableMoves(game.currentPlayer)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Playing",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            if (over) {
                Text(text = "Game Over", style = MaterialTheme.typography.bodySmall)
                if (game.state == GameState.DRAW) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "It's a draw", style = MaterialTheme.typography.bodySmall)
                } else {
                    val winner = viewModel.getWinner()
                    if (winner != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Winner: $winner", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = { viewModel.addToFavorites() }) {
                    Text(text = "Add to Favorites")
                }
            } else if (turn) {
                Text(text = "Your Turn", style = MaterialTheme.typography.bodySmall)
            } else {
                Text(text = "Opponent's Turn", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            GameBoard(board = game.board, onCellClick = { position ->
                viewModel.playMove(position)
            }, availableMoves = availableMoves, isPlayerTurn = turn)

            Spacer(modifier = Modifier.height(10.dp))

            if (availableMoves.isEmpty() && turn && game.state == GameState.ACTIVE) {
                Button(onClick = { viewModel.pass() }) {
                    Text(text = "Pass")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Game State: ${game.state.name}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}