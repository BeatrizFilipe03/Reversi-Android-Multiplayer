package com.example.cher_g42.domain.favorites

import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import java.time.LocalDateTime
import java.util.UUID

data class FavoriteGame(
    val id: UUID,
    val adversaryName: String,
    val dateTime: LocalDateTime,
    val playOrder: List<Pair<Position?, PieceColor>>,
    val winner: PieceColor
)