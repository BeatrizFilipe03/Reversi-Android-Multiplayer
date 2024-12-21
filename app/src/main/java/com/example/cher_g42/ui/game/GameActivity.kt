package com.example.cher_g42.ui.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.cher_g42.CheRApplication
import com.example.cher_g42.DependenciesContainer
import com.example.cher_g42.ui.MainActivity
import com.example.cher_g42.ui.TAG
import com.example.cher_g42.ui.theme.CheRg42Theme

class GameActivity : ComponentActivity() {
    private val navigation by lazy { Navigation(this) }

    private val challenge by lazy {
        intent.getStringExtra("challenge") ?: "Unknown Challenge"
    }
    private val colorChosen by lazy {
        intent.getStringExtra("colorChosen") ?: "Unknown Color"
    }
    private val opponentName by lazy {
        intent.getStringExtra("opponentName") ?: "Unknown Opponent"
    }

    private val game by lazy {
        (application as CheRApplication).game
    }

    private val favoriteGamesRepository by lazy { (application as DependenciesContainer).favoriteGamesRepository }

    private val viewModel by viewModels<GameViewModel>(
        factoryProducer = {
            GameViewModel.GameViewModelFactory(challenge, colorChosen, opponentName, game, favoriteGamesRepository)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "GameActivity.onCreate")
        setContent {
            CheRg42Theme {
                GameScreen(viewModel, onNavigateHome = { navigation.navigateToHome() })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "GameActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "GameActivity.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "GameActivity.onDestroy")
    }

    class Navigation(private val activity: ComponentActivity) {
        fun navigateToHome() {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}