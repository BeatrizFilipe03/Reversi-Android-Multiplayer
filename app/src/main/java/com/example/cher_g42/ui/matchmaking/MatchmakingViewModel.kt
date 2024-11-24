package com.example.cher_g42.ui.matchmaking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cher_g42.domain.game.LobbyService
import com.example.cher_g42.domain.game.PlayerInfo
import com.example.cher_g42.domain.player.UserInfo
import com.example.cher_g42.services.ChallengeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MatchmakingState {
    object NotInLobby : MatchmakingState()
    object InLobby : MatchmakingState()
    data class InChallenge(val challenger: PlayerInfo, val challenged: PlayerInfo, val id: String) : MatchmakingState()
    object InGame : MatchmakingState()
    // Add states for challenges
}

class MatchmakingViewModel(
    private val service: LobbyService
) : ViewModel() {
    var currUser: PlayerInfo? = null

    private val _players = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val players: StateFlow<List<PlayerInfo>> = _players

    private val _state = MutableStateFlow<MatchmakingState>(MatchmakingState.NotInLobby)
    val state: StateFlow<MatchmakingState> = _state

    init {
        observeState()
    }

    private fun observeState() {
        viewModelScope.launch {
            _state.collect { state ->
                when (state) {
                    is MatchmakingState.InLobby -> {
                        getPlayers()
                        listenToChallenges()
                    }
                    else -> {
                        // Optional: handle other state transitions
                    }
                }
            }
        }
    }

    fun enterLobby(name : String) {
        check(currUser == null && _state.value is MatchmakingState.NotInLobby) { "The player is already inside the lobby" }
        viewModelScope.launch {
                val playerInfo = PlayerInfo(UserInfo(name))
                currUser = playerInfo
                service.enterLobby(playerInfo)
                _state.value = MatchmakingState.InLobby
        }
    }

    fun leaveLobby() {
        check(currUser != null && _state.value is MatchmakingState.InLobby) { "The player is not inside the lobby" }
        viewModelScope.launch {
            currUser?.let {
                service.leaveLobby(it)
                currUser = null
                _state.value = MatchmakingState.NotInLobby
            }
        }
    }

    private fun getPlayers() {
        service.getPlayers(currUser!!) { players ->
            _players.value = players
        }
    }

    fun issueChallenge(challenged: PlayerInfo) {
        check(currUser != null && _state.value is MatchmakingState.InLobby) { "The player is not inside the lobby" }
        viewModelScope.launch {
            currUser?.let {
                val challengeId = service.issueChallenge(it, challenged)
                _state.value = MatchmakingState.InChallenge(
                    currUser!!,
                    challenged,
                    challengeId
                )

                service.listenToChallengeState(challengeId) { state ->
                    if (state == ChallengeState.ACCEPTED) {
                        _state.value = MatchmakingState.InGame
                        service.deleteChallenge(challengeId)
                    } else if (state == ChallengeState.REJECTED) {
                        _state.value = MatchmakingState.InLobby
                        service.deleteChallenge(challengeId)
                    }
                }
            }
        }
    }

    private fun listenToChallenges() {
            currUser?.let { user ->
                service.listenToChallenges(user) { challenger, challengeId ->
                    _state.value = MatchmakingState.InChallenge(
                        challenger,
                        user,
                        challengeId
                    )
                }
            }
    }

    fun acceptChallenge(challengeId: String) {
        check(currUser != null && _state.value is MatchmakingState.InChallenge) { "The player is not in a challenge" }
        viewModelScope.launch {
            service.acceptChallenge(challengeId)
            _state.value = MatchmakingState.InGame
        }
    }

    fun rejectChallenge(challengeId: String) {
        check(currUser != null && _state.value is MatchmakingState.InChallenge) { "The player is not in a challenge" }
        viewModelScope.launch {
            service.rejectChallenge(challengeId)
            _state.value = MatchmakingState.InLobby
        }
    }

    class MatchmakingViewModelFactory(private val lobby: LobbyService) : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return MatchmakingViewModel(lobby) as T
        }
    }
}
