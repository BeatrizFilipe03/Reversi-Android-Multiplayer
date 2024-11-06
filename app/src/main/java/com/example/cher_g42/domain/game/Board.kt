package com.example.cher_g42.domain.game

import java.lang.IllegalArgumentException

const val BOARD_SIZE = 8

data class Board(val cells: List<List<Cell>>) {//não gostei porque para ver os movimentos diponiveis tenho de percorrer todas as peças só para descobrir se a posição ao lado está vazia
   fun checkAvailableMoves(currentPlayer: PieceColor): List<Position> {
       var row = 0
       var col = 0
       val availableMoves = mutableListOf<Position>()
       while(row < cells.size){
           while(col < cells[row].size){
               if(cells[row][col].color == currentPlayer){
                   val neighboursPos = cells[row][col].getNeighboursPos()
                   for(neighbourPos in neighboursPos){
                       val neighbourCell = cells[neighbourPos.row.value][neighbourPos.column.toInt()]
                       if(neighbourCell.color == currentPlayer.opposite() ){
                           val rowDiff = neighbourPos.row.value - (row +1)
                           val colDiff = neighbourPos.column.toInt() - (col +1)
                           if (isValidRow(neighbourPos.row.value + rowDiff, BOARD_SIZE)
                               && isValidColumn(neighbourPos.column.toInt() + colDiff, BOARD_SIZE)){
                               var nextColRow = Pair(neighbourPos.column.toInt() + colDiff,neighbourPos.row.value + rowDiff)
                               if(cells[nextColRow.second][nextColRow.first].color == null) {
                                   availableMoves.add(nextColRow.toPosition())
                               }else{
                                   while (isValidColumn(nextColRow.first, BOARD_SIZE)
                                       && isValidRow(nextColRow.second, BOARD_SIZE) &&
                                       cells[nextColRow.second][nextColRow.first].color == currentPlayer.opposite()){
                                       nextColRow = Pair(nextColRow.first + colDiff,nextColRow.second + rowDiff)
                                       if(!isValidColumn(nextColRow.first, BOARD_SIZE) ||
                                           !isValidRow(nextColRow.second, BOARD_SIZE) ||
                                           cells[nextColRow.second][nextColRow.first].color == currentPlayer) break
                                       if(cells[nextColRow.second][nextColRow.first].color == null) {
                                           availableMoves.add(nextColRow.toPosition())
                                           break
                                       }
                                   }
                               }
                           }
                       }
                   }
               }
               col++
           }
           row++
       }
    return availableMoves.toList()
   }
   fun isBoardFull():Boolean{
       for(row in cells){
           for(cell in row){
               if(cell.color == null) return false
           }
       }
       return true
   }
   fun play(position:Position, color: PieceColor): Board {
       val pair = position.toPair()
       if (cells[pair.second][pair.first].color != null)
           throw IllegalArgumentException("illegal move, cell(${pair.first},${pair.second}) already occupied")
       val newCell = Cell(position,color)
       val newCells = cells.toMutableList()
       val newCellsRow = newCells[pair.second].toMutableList()
       newCellsRow[pair.first] = newCell
       newCells[pair.second] = newCellsRow
       return Board(newCells)

   }
}