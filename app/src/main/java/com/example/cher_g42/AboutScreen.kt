package com.example.cher_g42

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cher_g42.ui.theme.CheRg42Theme

@Composable
fun AboutScreen(onNavigateBack: () -> Unit = {}, onContactUs: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = { onNavigateBack() }) {
            Text(text = "Back")
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.made_by))

            Spacer(modifier = Modifier.height(32.dp))

            // First author
            Text(text = stringResource(R.string.author_1))

            Spacer(modifier = Modifier.height(16.dp))

            // Second author
            Text(text = stringResource(R.string.author_2))

            Spacer(modifier = Modifier.height(25.dp))

            // Send Email
            IconButton(onClick = onContactUs, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutScreenPreview() {
    CheRg42Theme() {
        AboutScreen()
    }
}