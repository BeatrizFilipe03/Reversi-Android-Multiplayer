package com.example.cher_g42.ui.favorites

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.cher_g42.DependenciesContainer
import com.example.cher_g42.ui.TAG
import com.example.cher_g42.ui.theme.CheRg42Theme

class FavoritesActivity : ComponentActivity() {
    private val favoriteGamesRepository by lazy { (application as DependenciesContainer).favoriteGamesRepository }

    private val viewModel by viewModels<FavoritesViewModel>(
        factoryProducer = {
            FavoritesViewModel.FavoritesViewModelFactory(favoriteGamesRepository)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "FavoritesActivity.onCreate")
        setContent {
            CheRg42Theme {
                FavoritesScreen(viewModel, onNavigateToHome = { finish() })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "FavoritesActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "FavoritesActivity..onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "FavoritesActivity.onDestroy")
    }
}