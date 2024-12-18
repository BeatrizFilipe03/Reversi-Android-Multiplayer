package com.example.cher_g42.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cher_g42.domain.game.Board
import com.example.cher_g42.domain.game.GameState
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import com.example.cher_g42.domain.game.Row
import com.example.cher_g42.domain.game.createBoard
import com.example.cher_g42.domain.game.toColumn
import com.example.cher_g42.ui.theme.CheRg42Theme

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is GameStatus.Waiting -> {
            GameMainScreen(viewModel, false)
        }
        is GameStatus.Playing -> {
            GameMainScreen(viewModel, true)
        }
        is GameStatus.GameOver -> {
            GameMainScreen(viewModel, turn = false, over = true)
        }
        else -> {}
    }
}


@Composable
fun GameMainScreen(viewModel: GameViewModel, turn: Boolean, over: Boolean = false) {
    val game by viewModel.game.collectAsState()
    val availableMoves = game.board.checkAvailableMoves(game.currentPlayer)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Text(text = "Game State: ${game.state.name}", style = MaterialTheme.typography.bodySmall)
    }
}


@Composable
fun GameBoard(board: Board, onCellClick: (Position) -> Unit, availableMoves: List<Position>, isPlayerTurn: Boolean) {
    val size = board.cells.size

    LazyVerticalGrid(
        columns = GridCells.Fixed(size),
        modifier = Modifier.padding(5.dp),
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

@Composable
fun GameBoardPreview() {
    val board = createBoard() // Create a sample board for testing
    val availableMoves = board.checkAvailableMoves(PieceColor.BLACK)

    GameBoard(
        board = board,
        onCellClick = { position -> println("Cell clicked at position: $position") },
        availableMoves = availableMoves,
        isPlayerTurn = true
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewGameBoard() {
    CheRg42Theme {
        GameBoardPreview()
    }
}