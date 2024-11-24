package com.example.cher_g42.domain.game

import com.example.cher_g42.services.ChallengeState

interface LobbyService {

    // Returns a list of players in the lobby.
    fun getPlayers(currUser: PlayerInfo, onPlayersUpdate: (List<PlayerInfo>) -> Unit)

    // Enter the lobby.
    suspend fun enterLobby(playerInfo: PlayerInfo)

    // Leave the lobby.
    suspend fun leaveLobby(playerInfo: PlayerInfo)

    // Issue a challenge.
    suspend fun issueChallenge(challenger: PlayerInfo, challenged: PlayerInfo) : String

    // Listen to challenges.
    fun listenToChallenges(playerInfo: PlayerInfo, onChallenge: (PlayerInfo, String) -> Unit)

    // Accept a challenge.
    fun acceptChallenge(challengeId: String)

    // Reject a challenge.
    fun rejectChallenge(challengeId: String)

    fun listenToChallengeState(challengeId: String, onStateChange: (ChallengeState) -> Unit)

    fun deleteChallenge(challengeId: String)
}