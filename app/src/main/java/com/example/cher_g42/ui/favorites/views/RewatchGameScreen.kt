package com.example.cher_g42.ui.favorites.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cher_g42.domain.game.Board
import com.example.cher_g42.domain.game.GameState
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.ui.game.views.GameBoard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewatchGameScreen(
    board: Board,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onNavigateBack: () -> Unit,
    status: Pair<GameState, PieceColor>,
    winner: PieceColor
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Rewatching Game",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Center-align the title
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Left-pointing arrow icon
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp)) // Ensure title stays centered
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(40.dp),
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onPrevious() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                        }
                        IconButton(onClick = { onNext() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go Forward")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Game state displayed below the top bar
            Text(
                text = "Game State: ${status.first}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )

            // Game board
            GameBoard(
                board = board,
                modifier = Modifier
                    .weight(1f) // Take up remaining space
                    .padding(horizontal = 16.dp)
            )

            // Current player or winner displayed below the board
            if (status.first == GameState.FINISHED) {
                Text(
                    text = "Winner: $winner",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Current Player: ${status.second}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}