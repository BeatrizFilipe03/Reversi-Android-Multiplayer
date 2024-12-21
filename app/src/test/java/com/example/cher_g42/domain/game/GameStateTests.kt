package com.example.cher_g42.domain.game

import org.junit.Assert.assertEquals
import org.junit.Test

class GameStateTests {
    @Test
    fun testGameStateActive() {
        val gameState = GameState.ACTIVE
        assertEquals(gameState, GameState.ACTIVE)
    }
    @Test
    fun testGameStateFinished() {
        val gameState = GameState.FINISHED
        assertEquals(gameState, GameState.FINISHED)
    }
    @Test
    fun testGameStateDraw() {
        val gameState = GameState.DRAW
        assertEquals(gameState, GameState.DRAW)
    }
    @Test
    fun testGameStateValues() {
        assertEquals(GameState.ACTIVE.toString(), "ACTIVE")
        assertEquals(GameState.FINISHED.toString(), "FINISHED")
        assertEquals(GameState.DRAW.toString(), "DRAW")
    }
}