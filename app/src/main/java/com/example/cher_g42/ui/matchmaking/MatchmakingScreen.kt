package com.example.cher_g42.ui.matchmaking

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import com.example.cher_g42.R
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.PlayerInfo
import com.example.cher_g42.domain.player.UserInfo
import com.example.cher_g42.ui.common.ListItem
import com.example.cher_g42.ui.common.LoadingScreen
import java.util.UUID

@Composable
fun MatchmakingScreen(viewModel: MatchmakingViewModel, onNavigateBack: () -> Unit = {}, onNavigateToGame: (String, PieceColor, String) -> Unit) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is MatchmakingState.Loading -> {
            LoadingScreen()
        }
        is MatchmakingState.NotInLobby -> EnterNameDialog(
            onNameEntered = { name ->
                viewModel.enterLobby(name)
            },
            onNavigateBack = onNavigateBack
        )
        is MatchmakingState.InLobby -> LobbyScreen(
            username = viewModel.currUser?.info?.name ?: "",
            players = viewModel.players.collectAsState().value,
            onInvite = { invitedPlayer ->
                viewModel.issueChallenge(invitedPlayer)
            },
            onNavigateBack = onNavigateBack
        )
        is MatchmakingState.InChallenge -> {
            val stateChallenge = state as MatchmakingState.InChallenge
            val challenger = stateChallenge.challenger
            val challenged = stateChallenge.challenged
            val challengeId = stateChallenge.id
            val challengerName = challenger.info.name

            if (viewModel.currUser == challenger) {
                Text("Waiting for ${challenged.info.name} to accept the challenge")
            } else {
                GameInviteDialog(
                    username = challengerName,
                    onAccept = {
                        viewModel.acceptChallenge(challengeId, challengerName)
                    },
                    onDecline = {
                        viewModel.rejectChallenge(challengeId)
                    }
                )
            }
        }
        is MatchmakingState.InChooseColor -> {
            val stateChooseColor = state as MatchmakingState.InChooseColor
            val challengeId = stateChooseColor.challenge
            val opponentName = stateChooseColor.opponentName
            ChoosePieceDialog { pieceColor ->
                viewModel.choosePieceColor(challengeId, pieceColor, opponentName)
            }
        }
        is MatchmakingState.InGame -> {
            // Go to game screen
            val stateChallenge = state as MatchmakingState.InGame
            val challengeId = stateChallenge.challengeId
            val pieceColor = stateChallenge.color
            val opponentName = stateChallenge.opponentName
            onNavigateToGame(challengeId, pieceColor, opponentName)
        }
    }
}


// Screen that appears first to request user name
@Composable
fun EnterNameDialog(
    onNameEntered: (String) -> Unit,
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
                    onClick = { onNameEntered(playerName) },
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

@Composable
fun Invite(playerName: String, onInvite: (String) -> Unit) {
    IconButton(
        onClick = { onInvite(playerName) },
        modifier = Modifier.padding(horizontal = 5.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.controller_filled),
            contentDescription = "Invite to Play"
        )
    }
}


// Screen that appears when two users enter a game, to choose piece color
@Composable
fun ChoosePieceDialog(
    onChoice: (PieceColor) -> Unit
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
                Text(
                    text = "Choose your piece color:",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 20.dp, top = 15.dp)
                )

                // Buttons to choose piece color
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // White piece button
                    Button(
                        onClick = { onChoice(PieceColor.WHITE) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Canvas(
                                modifier = Modifier.size(20.dp)
                            ) {
                                drawCircle(color = Color.White)
                                drawCircle(
                                    color = Color.Gray,
                                    style = Stroke(width = 2f)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "White",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Black piece button
                    Button(
                        onClick = { onChoice(PieceColor.BLACK) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Canvas(
                                modifier = Modifier.size(20.dp)
                            ) {
                                drawCircle(color = Color.Black)
                                drawCircle(
                                    color = Color.Gray,
                                    style = Stroke(width = 2f)
                                ) // Optional border
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Black",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
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