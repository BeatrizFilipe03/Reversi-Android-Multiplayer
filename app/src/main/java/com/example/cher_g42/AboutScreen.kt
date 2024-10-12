package com.example.cher_g42

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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

@Composable
fun AboutScreen(onNavigateBack: () -> Unit = {}, onContactUs: () -> Unit = {}) {
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
                Box(modifier = Modifier.weight(0.45f)) {
                    // Green section
                    GreenSectionAbout(modifier = Modifier.fillMaxSize())

                    // Back button positioned on top of the green section
                    Button(
                        onClick = { onNavigateBack() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(65.dp)
                            .padding(14.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Left-pointing arrow icon
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // White section
                WhiteSectionAbout(modifier = Modifier.weight(0.55f), onContactUs = onContactUs)
            }
        }
    }
}

@Composable
fun GreenSectionAbout(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Email image
            Image(
                painter = painterResource(id = R.drawable.email_contactus),
                contentDescription = "Email Image",
                modifier = Modifier.size(180.dp)
            )

            // About title
            Text(
                text = stringResource(R.string.contact),
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun WhiteSectionAbout(modifier: Modifier = Modifier, onContactUs: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Person Icon
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Person Icon",
                modifier = Modifier.size(45.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            //Author 1
            Text(
                text = stringResource(R.string.author_1),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.author_1_email),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(35.dp))

            //Author 2
            Text(
                text = stringResource(R.string.author_2),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.author_2_email),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(45.dp))

            // Send Email button
            Button(
                onClick = { onContactUs() },
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.email),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(25.dp))
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