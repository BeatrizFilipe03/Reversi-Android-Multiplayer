package com.example.cher_g42.domain.favorites

import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.util.UUID

interface FavoriteGamesRepository {
    val favoriteGames: Flow<List<FavoriteGame?>>
    suspend fun addFavoriteGame(gameId: UUID, opponentName: String, dateTime: LocalDateTime, playOrder: List<Pair<Position?, PieceColor>>, winner: PieceColor?)
    suspend fun getAllFavoriteGames(): List<FavoriteGame>
}