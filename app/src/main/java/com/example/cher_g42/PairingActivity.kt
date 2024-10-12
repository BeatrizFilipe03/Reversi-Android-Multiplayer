package com.example.cher_g42

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cher_g42.ui.theme.CheRg42Theme

class PairingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "PairingActivity.onCreate")
        setContent {
            CheRg42Theme() {
                PairingScreen(onNavigateBack = { finish() })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "PairingActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "PairingActivity..onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "PairingActivity.onDestroy")
    }
}