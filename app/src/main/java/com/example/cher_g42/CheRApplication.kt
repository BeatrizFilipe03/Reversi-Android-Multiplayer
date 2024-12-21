package com.example.cher_g42

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.cher_g42.domain.favorites.FavoriteGamesRepository
import com.example.cher_g42.domain.game.GameService
import com.example.cher_g42.domain.matchmaking.LobbyService
import com.example.cher_g42.services.FavoriteGamesDataStore
import com.example.cher_g42.services.GameFirebaseService
import com.example.cher_g42.services.LobbyFirebaseService
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

/**
 * The contract for the object that holds all the globally relevant dependencies.
 */
interface DependenciesContainer {
    val favoriteGamesRepository: FavoriteGamesRepository
    val lobby: LobbyService
    val game: GameService
}

/**
 * The application class to be used as a Service Locator.
 */
class CheRApplication : Application(), DependenciesContainer {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info")

    override val favoriteGamesRepository: FavoriteGamesRepository
        get() = FavoriteGamesDataStore(dataStore)

    override val lobby: LobbyService
        get() = LobbyFirebaseService(realDb)

    override val game: GameService
        get() = GameFirebaseService(realDb)

    private val realDb: FirebaseFirestore by lazy {
        Firebase.firestore
    }
}