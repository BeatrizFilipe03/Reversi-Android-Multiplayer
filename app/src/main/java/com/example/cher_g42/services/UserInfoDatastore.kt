package com.example.cher_g42.services

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cher_g42.domain.player.UserInfo
import com.example.cher_g42.domain.player.UserInfoRepository
import kotlinx.coroutines.flow.first

private const val USER_NAME_KEY = "Name"

/**
 * A user information repository implementation supported in DataStore, the
 * modern alternative to SharedPreferences.
 */
class UserInfoDataStore(private val store: DataStore<Preferences>) : UserInfoRepository {

    private val nameKey = stringPreferencesKey(USER_NAME_KEY)

    override suspend fun getUserInfo(): UserInfo? {
        val preferences = store.data.first()
        val name = preferences[nameKey]
        return if (name != null) UserInfo(name) else null
    }

    override suspend fun updateUserInfo(userInfo: UserInfo) {
        store.edit { preferences ->
            preferences[nameKey] = userInfo.name
        }
    }
}