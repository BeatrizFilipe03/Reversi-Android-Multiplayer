package com.example.cher_g42.domain.favorites


import com.example.cher_g42.domain.game.Column
import com.example.cher_g42.domain.game.PieceColor
import com.example.cher_g42.domain.game.Position
import com.example.cher_g42.domain.game.Row
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID

class FavoriteGameTests {
    @Test
    fun validFavoriteGame(){
        val fav = FavoriteGame(
            UUID.randomUUID(),
            "johnCina",
            LocalDateTime.of(2024,12,21,23,59,59,59),
            listOf(Pair(Position(Column('A'), Row(1)),PieceColor.BLACK)),
            PieceColor.BLACK
        )
        assertEquals(fav.winner,PieceColor.BLACK)
        assertEquals(fav.dateTime,LocalDateTime.of(2024,12,21,23,59,59,59))
        assertEquals(fav.adversaryName,"johnCina")
    }
}