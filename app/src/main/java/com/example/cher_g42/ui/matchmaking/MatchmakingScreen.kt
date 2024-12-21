package com.example.cher_g42.ui.matchmaking

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.ui.common.LoadingScreen
import com.example.cher_g42.ui.matchmaking.views.ChoosePieceDialog
import com.example.cher_g42.ui.matchmaking.views.EnterNameDialog
import com.example.cher_g42.ui.matchmaking.views.GameInviteDialog
import com.example.cher_g42.ui.matchmaking.views.LobbyScreen

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