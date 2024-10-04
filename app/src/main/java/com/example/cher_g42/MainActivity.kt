package com.example.cher_g42

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cher_g42.ui.theme.CheRg42Theme

const val TAG = "CheR"

class MainActivity : ComponentActivity() {

    private val navigateToAboutIntent: Intent by lazy {
        Intent(this, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheRg42Theme {
                CheRScreen(onAboutNavigate = { startActivity(navigateToAboutIntent) })
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CheRg42Theme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    CheRg42Theme {
        CheRScreen()
    }
}