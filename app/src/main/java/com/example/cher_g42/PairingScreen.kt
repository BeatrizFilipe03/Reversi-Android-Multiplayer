package com.example.cher_g42

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cher_g42.ui.theme.CheRg42Theme

@Composable
fun PairingScreen(onNavigateBack: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = { onNavigateBack() }) {
            Text(text = "Back")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PairingScreenPreview() {
    CheRg42Theme() {
        FavoritesScreen()
    }
}