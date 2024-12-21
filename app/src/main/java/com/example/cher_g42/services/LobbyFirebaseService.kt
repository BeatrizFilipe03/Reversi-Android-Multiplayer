package com.example.cher_g42.services

import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.matchmaking.LobbyService
import com.example.cher_g42.domain.game.PlayerInfo
import com.example.cher_g42.domain.player.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Names of the fields used in the document representations.
 */
private const val LOBBY = "lobby"
private const val NAME = "name"
private const val ID = "id"
private const val INFO = "info"

private const val CHALLENGE = "challenge"
private const val CHALLENGER = "challenger"
private const val CHALLENGED = "challenged"
private const val CHALLENGESTATE = "state"
private const val COLOR = "color"

enum class ChallengeState {
    PENDING,
    ACCEPTED,
    REJECTED
}

/**
 * Implementation of the Game's lobby using Firebase's Firestore.
 */
class LobbyFirebaseService(private val db: FirebaseFirestore) : LobbyService {

    override fun getPlayers(currUser: PlayerInfo, onPlayersUpdate: (List<PlayerInfo>) -> Unit) {
        db.collection(LOBBY)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val players = snapshot.documents
                        .filter { it.id != currUser.id.toString() } // Exclude currUser
                        .map { it.id to it.data }
                        .map { (id, data) ->
                            PlayerInfo(
                                data!!.toUserInfo(),
                                UUID.fromString(id)
                            )
                        }
                    onPlayersUpdate(players)
                }
            }
    }

    override suspend fun enterLobby(playerInfo: PlayerInfo) {
            db.collection(LOBBY)
                .document(playerInfo.id.toString())
                .set(
                    hashMapOf(
                        NAME to playerInfo.info.name
                    )
                )
                .await()
    }

    override suspend fun leaveLobby(playerInfo: PlayerInfo) {
            db.collection(LOBBY)
                .document(playerInfo.id.toString())
                .delete()
                .await()
    }

    override suspend fun issueChallenge(challenger: PlayerInfo, challenged: PlayerInfo): String {
        val uuid = UUID.randomUUID().toString()
        db.collection(CHALLENGE)
            .document(uuid)
            .set(
                hashMapOf(
                    CHALLENGER to challenger.toMap(),
                    CHALLENGED to challenged.toMap(),
                    CHALLENGESTATE to ChallengeState.PENDING,
                    COLOR to PieceColor.EMPTY.name
                )
            )
            .await()
        return uuid
    }

    // A function that listens to the challenges to see if someone has challenged the player
    override fun listenToChallenges(playerInfo: PlayerInfo, onChallenge: (PlayerInfo, String) -> Unit) {
        db.collection(CHALLENGE)
            .whereEqualTo(CHALLENGED, playerInfo.toMap())
            .whereEqualTo(CHALLENGESTATE, ChallengeState.PENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val doc = snapshot.documents[0]
                    val challenger = doc.get(CHALLENGER)
                    if (challenger != null) {
                        onChallenge((challenger as Map<String, Any>).toPlayerInfo(), doc.id)
                    }
                }
            }
    }

    override fun acceptChallenge(challengeId: String) {
        db.collection(CHALLENGE)
            .document(challengeId)
            .update(CHALLENGESTATE, ChallengeState.ACCEPTED)
    }

    override fun rejectChallenge(challengeId: String) {
        db.collection(CHALLENGE)
            .document(challengeId)
            .update(CHALLENGESTATE, ChallengeState.REJECTED)
    }

    // Function to listen in a specific challenge to see if challenge state changes
    override fun listenToChallengeState(challengeId: String, onStateChange: (ChallengeState) -> Unit) {
        db.collection(CHALLENGE)
            .document(challengeId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    // Get the current state from the Firestore document
                    val stateString = snapshot.getString(CHALLENGESTATE)
                    val challengeState = ChallengeState.valueOf(stateString ?: ChallengeState.PENDING.name)
                    onStateChange(challengeState) // Notify about state change
                }
            }
    }

    // Function to delete a challenge
    override fun deleteChallenge(challengeId: String) {
        db.collection(CHALLENGE)
            .document(challengeId)
            .delete()
    }

    override fun updatePieceColor(challengeId: String, color: PieceColor) {
        db.collection(CHALLENGE)
            .document(challengeId)
            .update(COLOR, color.name)
    }

    override fun listenToColorChange(challengeId: String, onColorChange: (PieceColor) -> Unit) {
        db.collection(CHALLENGE)
            .document(challengeId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    // Get the current state from the Firestore document
                    val colorString = snapshot.getString(COLOR)
                    val color = PieceColor.valueOf(colorString!!)
                    if (color != PieceColor.EMPTY) {
                        onColorChange(color) // Notify about state change
                    }
                }
            }
    }

    private fun PlayerInfo.toMap(): Map<String, Any> {
        return mapOf(
            ID to id.toString(),
            INFO to mapOf(
                NAME to info.name
            )
        )
    }

    private fun Map<String, Any>.toPlayerInfo(): PlayerInfo {
        val info = this[INFO] as Map<String, Any>
        return PlayerInfo(
            UserInfo(info[NAME] as String),
            UUID.fromString(this[ID] as String)
        )
    }

    private fun Map<String, Any>.toUserInfo(): UserInfo {
        return UserInfo(this[NAME] as String)
    }
}