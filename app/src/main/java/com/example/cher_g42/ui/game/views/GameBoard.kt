package com.example.cher_g42.ui.game.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cher_g42.domain.game.Board
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import com.example.cher_g42.domain.game.Row
import com.example.cher_g42.domain.game.toColumn

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