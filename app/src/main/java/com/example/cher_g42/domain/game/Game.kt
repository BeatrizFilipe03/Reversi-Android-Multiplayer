package com.example.cher_g42.domain.game

import java.util.UUID

typealias scoreBW = Pair<Int,Int>

data class Game(
    val id: UUID,
    val board: Board,
    val state: GameState,
    val currentPlayer: PieceColor
) {


    fun play(position: Position): Game { //como board e gamestate são atualizados ao mesmo tempo o gamestate pode não dizer imediatamente que o jogo que ele já terminou
        return this.copy(
            board = board.play(position, currentPlayer),
            state = if (checkWin()) {
                if (board.score().first == board.score().second) GameState.DRAW else GameState.FINISHED
            } else {GameState.ACTIVE}, //gamestate.finished não mosta quem é o vencedor
            currentPlayer = currentPlayer.opposite()
        )
    }

    private fun checkWin(): Boolean {
        if (board.isBoardFull()) return true
        if (board.checkAvailableMoves(currentPlayer) == emptyList<Position>()) return true
        return false
    }
    override fun toString(): String {
        var result = id.toString() + "\n"
        stringOfNumbersInBoard(board.cells.size)
        stringOfLettersInBoard(board.cells[0].size)
        for (row in board.cells){
            for(col in row){
                result += when(col.color){
                    PieceColor.BLACK -> "b"
                    PieceColor.WHITE -> "w"
                    else -> "+"
                }

            }
            result += "\n"
        }
        result += state.name +"\n"
        result += currentPlayer.name
        return result
    }

}

fun createGame(): Game{
    val id = UUID.randomUUID()
    val board = createBoard()
    return Game(id,board,GameState.ACTIVE,PieceColor.BLACK)

}

fun String.toGame():Game{
    val gameList = this.split('\n')
    val id: UUID = UUID.fromString(gameList[0]) //se gamelist[0], não for tipo uuid dá erro
    val cells = emptyList<List<Cell>>().toMutableList()
    for (r in 0 until gameList[1].length){
        val rowString = gameList [1  + r]
        val row = emptyList<Cell>().toMutableList()
        for(c in 0 until rowString.length){
            val cell = when(rowString[c] ){
                'b'-> Cell(Position(c.toColumn(),Row(r)),PieceColor.BLACK)
                'w'-> Cell(Position(c.toColumn(),Row(r)),PieceColor.WHITE)
                else -> Cell(Position(c.toColumn(),Row(r)),null)
            }
            row.add(cell)
        }
        cells.add(row)
    }
    val state = GameState.valueOf(gameList[1 + gameList[1].length])
    val currentPlayer = PieceColor.valueOf(gameList[1 + gameList[1].length + 1])
    return Game(id,Board(cells),state,currentPlayer)
}

private fun stringOfNumbersInBoard(rowSize:Int):String{
    var stringOfNumbers =""
    for( i in 1 .. rowSize){
        stringOfNumbers += i.toString()
    }
    return stringOfNumbers
}

private fun stringOfLettersInBoard(colSize:Int):String{
    var stringOfLetters =""
    for( i in 0 until  colSize){
        stringOfLetters += ('A'.code + i).toChar()
    }
    return stringOfLetters
}