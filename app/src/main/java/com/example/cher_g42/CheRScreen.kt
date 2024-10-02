package com.example.cher_g42

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.cher_g42.ui.theme.CheRg42Theme
import kotlinx.coroutines.delay

/**
 * The screen implementation, which hosts the screen's state machine.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheRScreen(onAboutNavigate: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { onAboutNavigate() }) {
                        Icon(Icons.Outlined.Info, contentDescription = "")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        var state: CheRScreenState by rememberSaveable(saver = CheRScreenState.Saver) {
            mutableStateOf(CheRScreenState.Idle)
        }

        val currentState = state
        LaunchedEffect(currentState) {
            if (currentState is CheRScreenState.Rolling) {
                //delay(timeMillis = ROLLING_TIME_MS)
                state = CheRScreenState.Idle
            }
        }

        when (currentState) {
            is CheRScreenState.Idle -> CheRIdleView(
                state = currentState,
                onDiceRollIntent = { state = CheRScreenState.Rolling },
                modifier = Modifier.padding(innerPadding)
            )

            else -> CheRRollingView(modifier = Modifier.padding(innerPadding))
        }
    }
}

/**
 * The screen's possible states.
 */
sealed interface CheRScreenState {
    data object Idle : CheRScreenState
    data object Rolling : CheRScreenState

    companion object {
        val Saver = Saver<MutableState<CheRScreenState>, List<Int>>(
            save = { toSave ->
                toSave.value.let { state ->
                    if (state is Idle) listOf(

                    )
                    else emptyList()
                }
            },
            restore = { saved ->
                if (saved.isNotEmpty())
                    mutableStateOf(Idle)
                else
                    mutableStateOf(Rolling)
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheRScreenPreview() {
    CheRg42Theme {
        CheRScreen()
    }
}