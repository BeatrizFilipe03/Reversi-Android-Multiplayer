package com.example.cher_g42.ui.favorites.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import java.util.UUID

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