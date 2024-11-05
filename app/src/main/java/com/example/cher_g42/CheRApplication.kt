package com.example.cher_g42

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.cher_g42.domain.game.Lobby
import com.example.cher_g42.domain.player.UserInfoRepository
import com.example.cher_g42.services.UserInfoDataStore

const val TAG = "CheRApp"

/**
 * The contract for the object that holds all the globally relevant dependencies.
 */
interface DependenciesContainer {
    val userInfoRepository: UserInfoRepository
    val lobby: Lobby
}

/**
 * The application class to be used as a Service Locator.
 */
class CheRApplication : Application(), DependenciesContainer {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info")

    override val userInfoRepository: UserInfoRepository
        get() = UserInfoDataStore(dataStore)

    override val lobby: Lobby
        get() = TODO("Not yet implemented")
}