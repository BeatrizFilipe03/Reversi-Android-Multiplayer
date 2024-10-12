package com.example.cher_g42

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cher_g42.ui.theme.CheRg42Theme

class FavoritesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "FavoritesActivity.onCreate")
        setContent {
            CheRg42Theme() {
                FavoritesScreen(onNavigateBack = { finish() })
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