package com.example.cher_g42.ui.favorites

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.cher_g42.domain.game.Board
import com.example.cher_g42.domain.game.GameState
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.ui.common.ListItem
import com.example.cher_g42.ui.common.LoadingScreen
import com.example.cher_g42.ui.game.GameBoard
import java.time.format.DateTimeFormatter
import java.util.UUID

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesListScreen(viewModel: FavoritesViewModel, onNavigateToHome: () -> Unit = {}) {
    val gamesState = viewModel.favorites.collectAsState()
    val games = gamesState.value
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorite Games",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Center-align the title
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToHome() }) {
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
            BottomAppBar (
                modifier = Modifier.height(40.dp),
                content = {
                    Text(
                        text = "Rewatch a game by clicking on the play button",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Center-align the title
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp)
            ) {
                items(games.size) { index ->
                    val game = games[index]
                    // Alternate colors based on index
                    val cardColor = if (index % 2 == 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }

                    // Format the date and time
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                    val formattedDateTime = game.dateTime.format(formatter)

                    // Combine game details and date/time
                    val string = """
                        ${game.id}
                        Opponent: ${game.adversaryName}
                        Date: $formattedDateTime
                    """.trimIndent()

                    ListItem(name = string, cardColor = cardColor) {
                        Watch(game.id, onWatch = { viewModel.onWatch(game.id, game.winner) })
                    }
                }
            }
        }
    }
}


@Composable
fun Watch(gameId: UUID, onWatch: (UUID) -> Unit) {
    IconButton(
        onClick = { onWatch(gameId) }
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Rewatch game",
        )
    }
}

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

