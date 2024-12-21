package com.example.cher_g42.ui.matchmaking.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Screen that appears first to request user name
@Composable
fun EnterNameDialog(
    onNameEntered: (String) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    var playerName by remember { mutableStateOf("") }

    // Background overlay effect
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)) // Transparent black background
    ) {
        // Back button
        Button(
            onClick = { onNavigateBack() },
            modifier = Modifier
                .size(65.dp)
                .padding(14.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        }

        // Popup Content
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title message
                Text(
                    text = "Please, insert your player name",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 12.dp, bottom = 5.dp)
                )

                Text(
                    text = "(max 30 characters)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                TextField(
                    value = playerName,
                    onValueChange = { newValue ->
                        if (newValue.length <= 30) { // Limit the input to 30 characters
                            playerName = newValue
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.secondary
                    ),
                    placeholder = { Text(
                        text= "Enter name",
                        style = MaterialTheme.typography.bodyMedium
                    ) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { onNameEntered(playerName) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        text = "Confirm",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}