package com.example.cher_g42.domain.game

data class Piece(
    val position: Position,
    val color: PieceColor
) {

}

data class Cell(
    val position: Position,
    val color: PieceColor?
){
    fun getNeighboursPos(): List<Position>{
        val neighbours = mutableListOf<Position>()
        if (position.row.value + 1 <= BOARD_SIZE) neighbours.add(downNeighbourPos())
        if (position.row.value - 1 >= 1) neighbours.add(upNeighbourPos())
        if (position.column.toInt() + 1 <= BOARD_SIZE) neighbours.add(nextNeighbourPos())
        if (position.column.toInt() - 1 >= 1) neighbours.add(previousNeighbourPos())
        return neighbours.toList()
    }
    fun nextNeighbourPos():Position{
        return Position(Column(position.column.value+1),position.row)

    }
    fun previousNeighbourPos():Position{
        return Position(Column(position.column.value-1),position.row)
    }
    fun upNeighbourPos():Position{
        return Position(position.column,Row(position.row.value-1))
    }
    fun downNeighbourPos():Position{
        return Position(position.column,Row(position.row.value+1))
    }
}