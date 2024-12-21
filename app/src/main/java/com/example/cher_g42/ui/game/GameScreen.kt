package com.example.cher_g42.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cher_g42.domain.game.Board
import com.example.cher_g42.domain.game.GameState
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import com.example.cher_g42.domain.game.Row
import com.example.cher_g42.domain.game.toColumn
import com.example.cher_g42.ui.common.LoadingScreen

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameMainScreenOver(viewModel: GameViewModel, onNavigateHome: () -> Unit) {
    val game by viewModel.game.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Game Over",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateHome() }) {
                        Icon(Icons.Default.Home, contentDescription = "Go Home")
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (game.state == GameState.DRAW) {
                Spacer(modifier = Modifier.height(35.dp))
                Text(text = "It's a draw", style = MaterialTheme.typography.bodySmall)
            } else {
                val winner = viewModel.getWinner()
                if (winner != null) {
                    Spacer(modifier = Modifier.height(35.dp))
                    Text(text = "Winner: $winner", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            GameBoard(
                board = game.board,
                onCellClick = { position ->
                    viewModel.playMove(position)
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(onClick = {
                viewModel.addToFavorites()
            }) {
                Text(text = "Add to Favorites")
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(text = "Game State: ${game.state.name}", style = MaterialTheme.typography.bodySmall)
        }
    }
}


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


@Composable
fun GameBoard(board: Board, onCellClick: (Position) -> Unit = {}, availableMoves: List<Position> = emptyList(), isPlayerTurn: Boolean = false, modifier: Modifier = Modifier) {
    val size = board.cells.size

    LazyVerticalGrid(
        columns = GridCells.Fixed(size),
        modifier = modifier.padding(5.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(size * size) { index ->
            val row = index / size
            val col = index % size
            val cell = board.cells[row][col]
            val position = Position((col + 1).toColumn(), Row(row + 1))

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .border(1.dp, Color.Black)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        onCellClick(position)
                    },
                contentAlignment = Alignment.Center
            ) {
                // Show the piece color if the cell is occupied
                if (cell.color != PieceColor.EMPTY) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                when (cell.color) {
                                    PieceColor.BLACK -> Color.Black
                                    PieceColor.WHITE -> Color.White
                                    else -> Color.Transparent
                                }
                            )
                    )
                }

                if (isPlayerTurn) {
                    // Show available moves as a transparent piece
                    if (position in availableMoves) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FF0000))
                        )
                    }
                }
            }
        }
    }
}