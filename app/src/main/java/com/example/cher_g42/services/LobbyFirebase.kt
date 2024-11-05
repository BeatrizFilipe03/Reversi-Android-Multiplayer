package com.example.cher_g42.services

import com.example.cher_g42.domain.game.Challenge
import com.example.cher_g42.domain.game.Lobby
import com.example.cher_g42.domain.game.LobbyEvent
import com.example.cher_g42.domain.game.PlayerInfo
import com.example.cher_g42.domain.game.RosterUpdated
import com.example.cher_g42.domain.player.UserInfo
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Names of the fields used in the document representations.
 */
const val LOBBY = "lobby"
const val NAME_FIELD = "name"
const val CHALLENGER_FIELD = "challenger"
const val CHALLENGER_ID_FIELD = "id"

/**
 * Implementation of the Game's lobby using Firebase's Firestore.
 */
class LobbyFirebase(private val db: FirebaseFirestore) : Lobby {

    private var isLocalPlayerInside = false

    override suspend fun enter(localPlayer: PlayerInfo): Flow<LobbyEvent> {
        check(!isLocalPlayerInside) { "The local player is already inside the lobby" }

        isLocalPlayerInside = true

        return callbackFlow {
            val localPlayerDocRef = addLocalPlayer(localPlayer)
            val roasterUpdatedSubscription = subscribeRoasterUpdated(this)

            awaitClose {
                roasterUpdatedSubscription.remove()
                localPlayerDocRef.delete()
            }
        }
    }

    override suspend fun issueChallenge(to: PlayerInfo, localPlayer: PlayerInfo): Challenge {
        check(isLocalPlayerInside) { "The local player must be inside the lobby to issue a challenge" }

        // Cria um novo objeto Challenge com o desafiante (jogador local) e o desafiado
        val challenge = Challenge(
            challenger = localPlayer,
            challenged = to
        )

        // Adiciona o desafio ao Firestore
        val challengeDocRef = db.collection(LOBBY).document(localPlayer.id.toString())
        challengeDocRef.set(challenge.toDocumentContent()).await()

        // Recupera o desafio do Firestore e converte-o usando `toChallengeOrNull`
        val savedChallengeSnapshot = challengeDocRef.get().await()
        return savedChallengeSnapshot.toChallengeOrNull() ?: throw IllegalStateException("Challenge could not be created")
    }

    override suspend fun leave() {
        check(isLocalPlayerInside) { "The local player is not in the lobby" }

        // Remove o jogador local do Firestore
        db.collection(LOBBY).document(isLocalPlayerInside.toString()).delete().await()
        isLocalPlayerInside = false
    }

    private suspend fun addLocalPlayer(localPlayer: PlayerInfo): DocumentReference {
        val docRef = db.collection(LOBBY).document(localPlayer.id.toString())
        docRef
            .set(localPlayer.info.toDocumentContent())
            .await()
        return docRef
    }

    private fun subscribeRoasterUpdated(flow: ProducerScope<LobbyEvent>) =
        db.collection(LOBBY).addSnapshotListener { snapshot, error ->
            when {
                error != null -> flow.close(error)
                snapshot != null -> flow.trySend(RosterUpdated(snapshot.toPlayerList()))
            }
        }
}

/**
 * [UserInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties
 */
fun UserInfo.toDocumentContent() = mapOf(
    NAME_FIELD to name
)

fun QuerySnapshot.toPlayerList() = map { it.toPlayerInfo() }

/**
 * Extension function used to convert player info documents stored in the Firestore DB
 * into [PlayerInfo] instances.
 */
fun QueryDocumentSnapshot.toPlayerInfo() =
    PlayerInfo(
        info = UserInfo(
            name = data[NAME_FIELD] as String
        ),
        id = UUID.fromString(id),
    )

@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.toChallengeOrNull(): Challenge? {
    val docData = data
    if (docData != null) {
        val challenger = docData[CHALLENGER_FIELD] as Map<String, String>?
        if (challenger != null) {
            return Challenge(
                challenger = playerInfoFromDocContent(challenger),
                challenged = this.toPlayerInfo()
            )
        }
    }
    return null
}

fun playerInfoFromDocContent(properties: Map<String, Any>) = PlayerInfo(
    info = UserInfo(
        name = properties[NAME_FIELD] as String
    ),
    id = UUID.fromString(properties[CHALLENGER_ID_FIELD] as String)
)

fun DocumentSnapshot.toPlayerInfo() = PlayerInfo(
    info = UserInfo(
        name = data?.get(NAME_FIELD) as String
    ),
    id = UUID.fromString(id)
)

fun Challenge.toDocumentContent() = mapOf(
    CHALLENGER_FIELD to challenger.info.toDocumentContent(),
    CHALLENGER_ID_FIELD to challenger.id.toString()
)