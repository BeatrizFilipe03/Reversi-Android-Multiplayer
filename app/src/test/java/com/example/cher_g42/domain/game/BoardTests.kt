package com.example.cher_g42.domain.game

import org.junit.Test
import org.junit.Assert.*


class BoardTests {
    @Test
    fun testBoardCreation(){
        val board = createBoard()
        assertEquals(board.cells[(BOARD_SIZE / 2)-1][(BOARD_SIZE / 2)-1].color,PieceColor.WHITE)
        assertEquals(board.cells[(BOARD_SIZE / 2)-1][(BOARD_SIZE / 2)].color,PieceColor.BLACK)
        assertEquals(board.cells[(BOARD_SIZE / 2) ][(BOARD_SIZE / 2)-1].color,PieceColor.BLACK)
        assertEquals(board.cells[(BOARD_SIZE / 2)][(BOARD_SIZE / 2)].color,PieceColor.WHITE)
    }
    @Test
    fun testBoardIsOverWithAvailableMoves(){
        val board = createBoard()
        assertFalse(checkIfOver(board))
    }
    @Test
    fun testBoardIsOverWithNoAvailableMoves(){
        val row = List<Cell>(3){Cell(Position((it+1).toColumn(),Row(it +1)),PieceColor.WHITE)}
        val board = Board(listOf(row,row,row))
        assertTrue(checkIfOver(board))
    }
    @Test
    fun testBoardIsFullWithAvailableMoves(){
        val board = createBoard()
        assertFalse(board.isBoardFull())
    }
    @Test
    fun testBoardIsFullWithNoAvailableMoves() {
        val row =
            List<Cell>(3) { Cell(Position((it + 1).toColumn(), Row(it + 1)), PieceColor.WHITE) }
        val board = Board(listOf(row, row, row))
        assertTrue(board.isBoardFull())
    }
    @Test
    fun testBoardScore(){
        val board = createBoard()
        val score = board.score()
        assertEquals(score.first,2)
        assertEquals(score.second,2)
    }
    @Test
    fun testBoardFromString(){
        val boardString =
                "++++++++\n" +
                "++++++++\n" +
                "++++++++\n" +
                "+++wb+++\n" +
                "+++bw+++\n" +
                "++++++++\n" +
                "++++++++\n" +
                "++++++++\n"
        val board = Board.fromString(boardString)
        assertEquals(board, createBoard())
    }
    @Test
    fun testBoardToString(){
        val board = createBoard()
        val boardString = board.toString()
        val board2String = "++++++++\n" +
                "++++++++\n" +
                "++++++++\n" +
                "+++wb+++\n" +
                "+++bw+++\n" +
                "++++++++\n" +
                "++++++++\n" +
                "++++++++\n"
        assertEquals(boardString,board2String)
    }
    @Test
    fun testcheckAvailableMovesWithAvailableMoves(){
        val board = createBoard()
        val availableMoves = board.checkAvailableMoves(PieceColor.BLACK)
        assertEquals(availableMoves.size,4)
        assertTrue(availableMoves.contains(Position(Column('C'),Row(4))))
        assertTrue(availableMoves.contains(Position(Column('D'),Row(3))))
        assertTrue(availableMoves.contains(Position(Column('F'),Row(5))))
        assertTrue(availableMoves.contains(Position(Column('E'),Row(6))))
    }
    @Test
    fun testcheckAvailableMovesWithNoAvailableMoves(){
        val row = List<Cell>(3){Cell(Position((it + 1).toColumn(), Row(it + 1)), PieceColor.WHITE)}
        val board = Board(listOf(row,row,row))
        val availableMoves = board.checkAvailableMoves(PieceColor.BLACK)
        assertEquals(availableMoves.size,0)
    }
    @Test
    fun testBoardPlayWithAAvailableMoves(){
        val board = createBoard()
        val newBoard = board.play(Position(Column('C'),Row(4)),PieceColor.BLACK)
        assertEquals(newBoard.cells[4-1][3-1].color,PieceColor.BLACK)
    }
}