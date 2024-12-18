package com.example.cher_g42.ui.game

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.cher_g42.CheRApplication
import com.example.cher_g42.ui.TAG
import com.example.cher_g42.ui.theme.CheRg42Theme

class GameActivity : ComponentActivity() {
    private val challenge by lazy {
        intent.getStringExtra("challenge") ?: "Unknown Challenge"
    }
    private val colorChosen by lazy {
        intent.getStringExtra("colorChosen") ?: "Unknown Color"
    }
    private val playerName by lazy {
        intent.getStringExtra("playerName") ?: "Unknown Player"
    }
    private val playerId by lazy {
        intent.getStringExtra("playerId") ?: "Unknown ID"
    }

    private val game by lazy {
        (application as CheRApplication).game
    }

    private val viewModel by viewModels<GameViewModel>(
        factoryProducer = {
            GameViewModel.GameViewModelFactory(challenge, colorChosen, playerName, playerId, game)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "GameActivity.onCreate")
        Log.v(TAG, "Challenge: $challenge")
        Log.v(TAG, "Color Chosen: $colorChosen")
        setContent {
            CheRg42Theme {
                GameScreen(viewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "GameActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "GameActivity..onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "GameActivity.onDestroy")
    }
}