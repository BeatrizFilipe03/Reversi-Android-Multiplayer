package com.example.cher_g42.ui.matchmaking

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cher_g42.ui.TAG
import com.example.cher_g42.ui.theme.CheRg42Theme

class MatchmakingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "MatchmakingActivity.onCreate")
        setContent {
            CheRg42Theme {
                MatchmakingScreen(onNavigateBack = { finish() })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "MatchmakingActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "MatchmakingActivity..onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "MatchmakingActivity.onDestroy")
    }
}