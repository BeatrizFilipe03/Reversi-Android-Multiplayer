package com.example.cher_g42

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cher_g42.ui.theme.CheRg42Theme

/**
 * The screen implementation, which hosts the screen's state machine.
 */
@Composable
fun CheRScreen(
    onAboutNavigate: () -> Unit = {},
    onFavoritesNavigate: () -> Unit = {},
    onPairingNavigate: () -> Unit = {}
) {
    // Box to allow layering of elements
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Green section
                GreenSection(modifier = Modifier.weight(0.55f))

                // White section
                WhiteSection(modifier = Modifier.weight(0.45f), onAboutNavigate = onAboutNavigate, onFavoritesNavigate = onFavoritesNavigate, onPairingNavigate = onPairingNavigate)
            }
        }

        // Clouds positioned at the bottom corners
        Clouds(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun GreenSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Game title
            Text(
                text = stringResource(R.string.game_title),
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Game image
            Image(
                painter = painterResource(id = R.drawable.board_homescreen),
                contentDescription = "Game Board",
                modifier = Modifier.size(280.dp)
            )
        }
    }
}

@Composable
fun WhiteSection(modifier: Modifier = Modifier, onAboutNavigate: () -> Unit, onFavoritesNavigate: () -> Unit, onPairingNavigate: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            // Play button -> Pairing Screen
            Button(
                onClick = { onPairingNavigate() },
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.controller_filled),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )

                    Spacer(modifier = Modifier.width(50.dp))

                    Text(
                        text = stringResource(R.string.play),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Favorites button -> Favorites Screen
            OutlinedButton(
                onClick = { onFavoritesNavigate() },
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(60.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.fav_games),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.star_outlined),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Message Button -> About Screen
            IconButton (
                onClick = { onAboutNavigate() },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.email_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
fun Clouds(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.cloud1),
            contentDescription = "Clouds",
            modifier = Modifier.align(Alignment.BottomStart)
        )

        Image(
            painter = painterResource(id = R.drawable.cloud2),
            contentDescription = "Clouds",
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CheRScreenPreview() {
    CheRg42Theme {
        CheRScreen()
    }
}