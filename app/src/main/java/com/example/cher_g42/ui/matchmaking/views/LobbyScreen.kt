package com.example.cher_g42.ui.matchmaking.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cher_g42.domain.game.PlayerInfo
import com.example.cher_g42.domain.player.UserInfo
import com.example.cher_g42.ui.common.ListItem
import com.example.cher_g42.ui.theme.CheRg42Theme
import java.util.UUID

// Main Lobby Screen, shows after the user picks their name
@Composable
fun LobbyScreen(
    username: String,
    players: List<PlayerInfo>,
    onInvite: (PlayerInfo) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    // Back button
    Button(
        onClick = { onNavigateBack() },
        modifier = Modifier
            .size(65.dp)
            .padding(14.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Left-pointing arrow icon
            contentDescription = "Back",
            modifier = Modifier.size(20.dp)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(8.dp))

        Text(
            text = "Lobby",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.primary
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(15.dp)
        ) {
            items(players.size) { index ->
                val player = players[index]
                // Alternate colors based on index
                val cardColor = if (index % 2 == 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                }

                ListItem(name = player.info.name, cardColor = cardColor) {
                    Invite(player.info.name, onInvite = { onInvite(player) })
                }
            }
        }

        // Welcome message
        Row(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome $username!\nInvite a player by clicking on the \uD83C\uDFAE",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LobbyScreenPreview() {
    CheRg42Theme {
        LobbyScreen(
            username = "Player 1",
            players = listOf(
                PlayerInfo(UserInfo("Player 2"), UUID.randomUUID()),
                PlayerInfo(UserInfo("Player 3"), UUID.randomUUID())
            ),
            onInvite = { }
        )
    }
}