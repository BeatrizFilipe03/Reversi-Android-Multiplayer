package com.example.cher_g42.ui.favorites.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cher_g42.ui.common.ListItem
import com.example.cher_g42.ui.favorites.FavoritesViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesListScreen(viewModel: FavoritesViewModel, onNavigateToHome: () -> Unit = {}) {
    val gamesState = viewModel.favorites.collectAsState()
    val games = gamesState.value
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorite Games",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Center-align the title
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToHome() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Left-pointing arrow icon
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp)) // Ensure title stays centered
                }
            )
        },
        bottomBar = {
            BottomAppBar (
                modifier = Modifier.height(40.dp),
                content = {
                    Text(
                        text = "Rewatch a game by clicking on the play button",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Center-align the title
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp)
            ) {
                items(games.size) { index ->
                    val game = games[index]
                    // Alternate colors based on index
                    val cardColor = if (index % 2 == 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }

                    // Format the date and time
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                    val formattedDateTime = game.dateTime.format(formatter)

                    // Combine game details and date/time
                    val string = """
                        ${game.id}
                        Opponent: ${game.adversaryName}
                        Date: $formattedDateTime
                    """.trimIndent()

                    ListItem(name = string, cardColor = cardColor) {
                        Watch(game.id, onWatch = { viewModel.onWatch(game.id, game.winner) })
                    }
                }
            }
        }
    }
}