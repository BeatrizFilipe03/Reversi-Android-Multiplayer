package com.example.cher_g42.domain.game

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID.randomUUID

class GameTests {
    @Test
    fun testGameCreation() {
        val game = createGame(randomUUID())
        assertEquals(game.board, createBoard())
        assertEquals(game.state, GameState.ACTIVE)
        assertEquals(game.currentPlayer, PieceColor.BLACK)
    }
    @Test
    fun testWinnerWithNoWinner() {
        val game = createGame(randomUUID())
        assertEquals(game.winner(), null)
    }
    @Test
    fun testWinnerWithBlackWin() {
        val row = List<Cell>(3){Cell(Position((it + 1).toColumn(), Row(it + 1)), PieceColor.BLACK)}
        val board = Board(listOf(row,row,row))
        val game = Game(randomUUID(),board,GameState.FINISHED,PieceColor.WHITE)
        assertEquals(game.winner(), PieceColor.BLACK)
    }
    @Test
    fun testWinnerWithWhiteWin() {
        val row = List<Cell>(3){Cell(Position((it + 1).toColumn(), Row(it + 1)), PieceColor.WHITE)}
        val board = Board(listOf(row,row,row))
        val game = Game(randomUUID(),board,GameState.FINISHED,PieceColor.BLACK)
        assertEquals(game.winner(), PieceColor.WHITE)
    }
    @Test
    fun testWinnerWithNoGameWinner() {
        val board = createBoard()
        val game = Game(randomUUID(), board, GameState.FINISHED, PieceColor.BLACK)
        assertEquals(game.winner(), null)
    }
    @Test
    fun testPassWithNoAvailableMovesForWhite() {
        val boardString =
                    "bbbbbbb+\n" +
                    "bbbbbbbw\n" +
                    "bbbbbbbb\n" +
                    "bbbbbbbb\n" +
                    "bbbbbbbb\n" +
                    "bbbbbbbb\n" +
                    "bbbbbbbb\n" +
                    "bbbbbbbb\n"

        val board = Board.fromString(boardString)
        val game = Game(randomUUID(),board,GameState.ACTIVE,PieceColor.WHITE,null)
        val newGame = game.pass()
        assertEquals(newGame.currentPlayer,PieceColor.BLACK)
        assertEquals(newGame.state,GameState.ACTIVE)

    }
    @Test
    fun testGamePlay() {
        val game = createGame(randomUUID())
        val newGame = game.play(Position(Column('C'), Row(4)))
        assertEquals(newGame.board, createBoard().play(Position(Column('C'), Row(4)), PieceColor.BLACK))
    }

}