package com.example.cher_g42.ui.matchmaking.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cher_g42.R

@Composable
fun Invite(playerName: String, onInvite: (String) -> Unit) {
    IconButton(
        onClick = { onInvite(playerName) },
        modifier = Modifier.padding(horizontal = 5.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.controller_filled),
            contentDescription = "Invite to Play"
        )
    }
}