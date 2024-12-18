package com.example.cher_g42.ui.matchmaking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.cher_g42.CheRApplication
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.ui.TAG
import com.example.cher_g42.ui.game.GameActivity
import com.example.cher_g42.ui.theme.CheRg42Theme
import java.util.UUID

class MatchmakingActivity : ComponentActivity() {
    private val navigation by lazy { Navigation(this) }

    private val lobby by lazy {
        (application as CheRApplication).lobby
    }

    private val viewModel by viewModels<MatchmakingViewModel>(
        factoryProducer = {
            MatchmakingViewModel.MatchmakingViewModelFactory(lobby)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "MatchmakingActivity.onCreate")
        setContent {
            CheRg42Theme {
                MatchmakingScreen(
                    viewModel,
                    onNavigateBack = { finish() },
                    onNavigateToGame = { challenge, color, playerId, playerName ->
                        navigation.navigateToGame(challenge, color, playerId, playerName)
                        finish()
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "MatchmakingActivity.onStart")
    }

    override fun onStop() {
        viewModel.leaveLobby()
        super.onStop()
        Log.v(TAG, "MatchmakingActivity.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "MatchmakingActivity.onDestroy")
    }
}

class Navigation(private val activity: ComponentActivity) {
    fun navigateToGame(challenge: String, color: PieceColor, playerId: UUID, playerName: String) {
        val intent = Intent(activity, GameActivity::class.java).apply {
            putExtra("challenge", challenge)
            putExtra("colorChosen", color.name)
            putExtra("playerId", playerId.toString())
            putExtra("playerName", playerName)
        }
        activity.startActivity(intent)
    }
}