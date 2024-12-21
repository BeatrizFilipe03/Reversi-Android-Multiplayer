package com.example.cher_g42.services

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cher_g42.domain.favorites.FavoriteGame
import com.example.cher_g42.domain.favorites.FavoriteGamesRepository
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import com.example.cher_g42.domain.game.Row
import com.example.cher_g42.domain.game.toColumn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID

/**
 * A favorites game repository implementation supported in DataStore, the
 * modern alternative to SharedPreferences.
 */
class FavoriteGamesDataStore(private val store: DataStore<Preferences>) : FavoriteGamesRepository {

    override val favoriteGames: Flow<List<FavoriteGame?>> =
        store.data.map { preferences ->
            preferences.toFavoriteGames()
        }

    override suspend fun addFavoriteGame(
        gameId: UUID,
        opponentName: String,
        dateTime: LocalDateTime,
        playOrder: List<Pair<Position?, PieceColor>>,
        winner: PieceColor?
    ) {
        store.updateData { preferences ->
            val count = preferences[favoriteGamesCountKey]?.toIntOrNull() ?: 0

            // Serialize playOrder considering null positions
            val serializedPlayOrder = playOrder.joinToString(";") { (pos, color) ->
                "${pos?.column?.value ?: "null"}:${pos?.row?.value ?: "null"},${color.name}"
            }

            preferences.toMutablePreferences().apply {
                this[gameIdKey(count)] = gameId.toString()
                this[opponentNameKey(count)] = opponentName
                this[dateTimeKey(count)] = dateTime.toString()
                this[playOrderKey(count)] = serializedPlayOrder
                this[winnerKey(count)] = winner?.name ?: PieceColor.EMPTY.name
                this[favoriteGamesCountKey] = (count + 1).toString()
            }
        }
    }

    override suspend fun getAllFavoriteGames(): List<FavoriteGame> {
        return store.data
            .first() // Collect the latest data snapshot
            .toFavoriteGames()
    }

    private val favoriteGamesCountKey = stringPreferencesKey("favoriteGamesCount")
    private fun gameIdKey(index: Int) = stringPreferencesKey("gameId$index")
    private fun opponentNameKey(index: Int) = stringPreferencesKey("opponentName$index")
    private fun dateTimeKey(index: Int) = stringPreferencesKey("dateTime$index")
    private fun playOrderKey(index: Int) = stringPreferencesKey("playOrder$index")
    private fun winnerKey(index: Int) = stringPreferencesKey("winner$index")

    private fun Preferences.toFavoriteGames(): List<FavoriteGame> {
        val count = this[favoriteGamesCountKey]?.toIntOrNull() ?: 0
        return (0 until count).mapNotNull { index ->
            val gameId = this[gameIdKey(index)]?.let(UUID::fromString)
            val opponentName = this[opponentNameKey(index)]
            val dateTime = this[dateTimeKey(index)]?.let(LocalDateTime::parse)
            val playOrder = this[playOrderKey(index)]?.let { serialized ->
                serialized.split(";").mapNotNull { pair ->
                    val (pos, color) = pair.split(",").takeIf { it.size == 2 } ?: return@mapNotNull null
                    val (x, y) = pos.split(":").takeIf { it.size == 2 } ?: return@mapNotNull null

                    // Check if either x or y is null or empty, and return null for the position
                    val column = if (x == "null") null else x.toColumn()
                    val row = if (y == "null") null else Row(y.toInt())

                    // Only create Position if both column and row are valid
                    val position = if (column != null && row != null) Position(column, row) else null
                    val pieceColor = PieceColor.valueOf(color)

                    position to pieceColor
                }
            }
            val winner = this[winnerKey(index)]?.let { PieceColor.valueOf(it) } ?: PieceColor.EMPTY

            if (gameId != null && opponentName != null && dateTime != null && playOrder != null) {
                FavoriteGame(gameId, opponentName, dateTime, playOrder, winner)
            } else null
        }
    }

}
