package com.example.cher_g42.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cher_g42.ui.favorites.FavoritesActivity
import com.example.cher_g42.ui.home.AboutActivity
import com.example.cher_g42.ui.home.HomeScreen
import com.example.cher_g42.ui.matchmaking.MatchmakingActivity
import com.example.cher_g42.ui.theme.CheRg42Theme

const val TAG = "CheR"

class MainActivity : ComponentActivity() {
    private val navigation by lazy { Navigation(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheRg42Theme {
                HomeScreen(
                    onAboutNavigate = { navigation.navigateToAbout() },
                    onFavoritesNavigate = { navigation.navigateToFavorites() },
                    onMatchmakingNavigate = { navigation.navigateToMatchmaking() }
                )
            }
        }
    }
}

class Navigation(private val activity: ComponentActivity) {
    fun navigateToAbout() {
        activity.startActivity(Intent(activity, AboutActivity::class.java))
    }

    fun navigateToFavorites() {
        activity.startActivity(Intent(activity, FavoritesActivity::class.java))
    }

    fun navigateToMatchmaking() {
        activity.startActivity(Intent(activity, MatchmakingActivity::class.java))
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    CheRg42Theme {
        HomeScreen()
    }
}