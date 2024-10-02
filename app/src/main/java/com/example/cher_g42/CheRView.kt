package com.example.cher_g42

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// The tags used to identify the views in the tests.
const val IDLE_VIEW_TAG = "idle_view"
const val ROLLING_VIEW_TAG = "rolling_view"

@Composable
fun CheRIdleView(
    state: CheRScreenState.Idle,
    onDiceRollIntent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxSize().testTag(IDLE_VIEW_TAG)
    ) {
    }
}

@Composable
fun CheRRollingView(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxSize().testTag(ROLLING_VIEW_TAG)
    ) {

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheRIdleViewPreview() {
    CheRIdleView(state = CheRScreenState.Idle, onDiceRollIntent = {})
}