package com.example.cher_g42.ui.matchmaking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cher_g42.ui.theme.CheRg42Theme
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.example.cher_g42.R

@Composable
fun MatchmakingScreen(onNavigateBack: () -> Unit = {}) {
    EnterNameDialog(onNameEntered = { /*Preview*/ }, onNavigateBack = onNavigateBack)
}


// Screen that appears first to request user name
@Composable
fun EnterNameDialog(
    onNameEntered: () -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    var playerName by remember { mutableStateOf("") }

    // Background overlay effect
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)) // Transparent black background
    ) {
        // Back button
        Button(
            onClick = { onNavigateBack() },
            modifier = Modifier
                .size(65.dp)
                .padding(14.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        }

        // Popup Content
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title message
                Text(
                    text = "Please, insert your player name",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 12.dp, bottom = 5.dp)
                )

                // "Accept?" message
                Text(
                    text = "(max 30 characters)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                TextField(
                    value = playerName,
                    onValueChange = { newValue ->
                        if (newValue.length <= 30) { // Limit the input to 30 characters
                            playerName = newValue
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.secondary
                    ),
                    placeholder = { Text(
                        text= "Enter name",
                        style = MaterialTheme.typography.bodyMedium
                        ) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { onNameEntered() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        text = "Confirm",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


// Screen that appears when a user is selected by another for a game, go to game if accept, back to lobby if declined
@Composable
fun GameInviteDialog(
    username: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(80.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title message
                Text(
                    text = "$username just invited you for a game!",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 20.dp, top = 12.dp)
                )

                // "Accept?" message
                Text(
                    text = "Accept?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                // Yes and No buttons
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onDecline() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(
                            text = "No",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    Button(
                        onClick = { onAccept() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Text(
                            text = "Yes",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}


// Main Lobby Screen, shows after the user picks their name
@Composable
fun LobbyScreen(
    username: String,
    players: List<String>,
    onInvite: (String) -> Unit,
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

                    PlayerListItem(playerName = player, onInvite = { onInvite(player) }, cardColor = cardColor)
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

@Composable
fun PlayerListItem(playerName: String, onInvite: () -> Unit, cardColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = playerName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f).padding(horizontal = 5.dp)
            )

            IconButton(
                onClick = { onInvite() },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.controller_filled),
                    contentDescription = "Invite to Play"
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EnterNameDialogPreview() {
    CheRg42Theme {
        EnterNameDialog(
            onNameEntered = { /* Preview only, no action needed */ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameInviteDialogPreview() {
    CheRg42Theme {
        GameInviteDialog(
            username = "UserName",
            onAccept = { /* Preview only, no action needed */ },
            onDecline = { /* Preview only, no action needed */ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LobbyScreenPreview() {
    CheRg42Theme {
        LobbyScreen(
            username = "UserName",
            players = listOf(
                "Alice",
                "Bob",
                "Charlie",
                "Alice"
            ),
            onInvite = { /* Preview only, no action needed */ },
            onNavigateBack = { /* Preview only, no action needed */ }
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MatchmakingScreenPreview() {
    CheRg42Theme {
        MatchmakingScreen()
    }
}