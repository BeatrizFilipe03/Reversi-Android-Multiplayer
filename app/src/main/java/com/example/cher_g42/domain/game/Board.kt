package com.example.cher_g42.domain.game

import java.lang.IllegalArgumentException

const val BOARD_SIZE = 8

enum class DirectionMath(val pair: Pair<Int, Int>) {
    UP(Pair(0, 1)),
    DOWN(Pair(0, -1)),
    LEFT(Pair(-1, 0)),
    RIGHT(Pair(1, 0)),
    UP_LEFT(Pair(-1, 1)),
    UP_RIGHT(Pair(1, 1)),
    DOWN_LEFT(Pair(-1, -1)),
    DOWN_RIGHT(Pair(1, -1))
}

data class Board(val cells: List<List<Cell>>) {

    fun checkAvailableMoves(currentPlayer: PieceColor): List<Position> {
        val availableMoves = mutableListOf<Position>()

        for (row in cells) {
            for (cell in row) {
                if (cell.color != null) continue // Skip non-empty cells

                val position = cell.position
                for (direction in DirectionMath.entries) {
                    val (dx, dy) = direction.pair
                    var x = position.column.toInt()
                    var y = position.row.value
                    var foundOpponent = false

                    while (true) {
                        x += dx
                        y += dy
                        if (!isValidColumn(x, BOARD_SIZE) || !isValidRow(y, BOARD_SIZE)) break

                        val neighborCell = cells[y - 1][x - 1] // Adjust indices to 0-based
                        when {
                            neighborCell.color == currentPlayer.opposite() -> {
                                foundOpponent = true
                            }
                            neighborCell.color == currentPlayer && foundOpponent -> {
                                availableMoves.add(position)
                                break
                            }
                            else -> break // Empty cell or no valid sequence
                        }
                    }
                }
            }
        }

        return availableMoves
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
       val (x, y) = position.toPair()

       // Ensure the move is valid (the cell must be empty)
       if (cells[y - 1][x - 1].color != null) {
           throw IllegalArgumentException("Illegal move: cell (${x}, ${y}) is already occupied")
       }

       val newCells = cells.map { it.toMutableList() }.toMutableList()
       newCells[y - 1][x - 1] = Cell(position, color) // Place the new piece

       // Check all directions to flip opponent's pieces
       for (direction in DirectionMath.entries) {
           val (dx, dy) = direction.pair
           var currentX = x + dx
           var currentY = y + dy
           val capturedPositions = mutableListOf<Pair<Int, Int>>()

           // Find opponent pieces to potentially flip
           while (isValidColumn(currentX, BOARD_SIZE) && isValidRow(currentY, BOARD_SIZE)) {
               val currentCell = cells[currentY - 1][currentX - 1]
               when {
                   currentCell.color == color.opposite() -> {
                       capturedPositions.add(Pair(currentX, currentY))
                   }
                   currentCell.color == color -> {
                       // If we hit our color and captured pieces exist, flip them
                       if (capturedPositions.isNotEmpty()) {
                           for ((flipX, flipY) in capturedPositions) {
                               val flipCell = newCells[flipY - 1][flipX - 1]
                               newCells[flipY - 1][flipX - 1] = flipCell.copy(color = color)
                           }
                       }
                       break
                   }
                   else -> break // Empty cell or invalid sequence
               }
               currentX += dx
               currentY += dy
           }
       }

       return Board(newCells)
   }

    fun score(): scoreBW {
        var whiteScore = 0
        var blackScore = 0
        for (row in cells) {
            for (cell in row) {
                if (cell.color == PieceColor.WHITE) whiteScore++
                if (cell.color == PieceColor.BLACK) blackScore++
            }
        }
        return scoreBW(blackScore, whiteScore)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (row in cells) {
            for (cell in row) {
                builder.append(
                    when (cell.color) {
                        PieceColor.BLACK -> "b"
                        PieceColor.WHITE -> "w"
                        else -> "+"
                    }
                )
            }
            builder.append("\n") // New line for each row
        }
        return builder.toString()
    }

    companion object {
        fun fromString(boardString: String): Board {
            val rows = boardString.trim().split("\n")
            val cells = mutableListOf<List<Cell>>()

            for ((r, rowString) in rows.withIndex()) {
                val row = mutableListOf<Cell>()
                for ((c, char) in rowString.withIndex()) {
                    val color = when (char) {
                        'b' -> PieceColor.BLACK
                        'w' -> PieceColor.WHITE
                        else -> null
                    }
                    row.add(Cell(Position((c + 1).toColumn(), Row(r + 1)), color))
                }
                cells.add(row)
            }

            return Board(cells)
        }
    }
}

fun createBoard():Board{
    val cells = mutableListOf<List<Cell>>()

    for (r in 1..BOARD_SIZE) {
        val row = mutableListOf<Cell>()
        for (c in 1..BOARD_SIZE) {
            // Determine the initial pieces
            val pieceColor = when {
                r == BOARD_SIZE / 2 && c == BOARD_SIZE / 2 -> PieceColor.WHITE
                r == BOARD_SIZE / 2 && c == BOARD_SIZE / 2 + 1 -> PieceColor.BLACK
                r == BOARD_SIZE / 2 + 1 && c == BOARD_SIZE / 2 -> PieceColor.BLACK
                r == BOARD_SIZE / 2 + 1 && c == BOARD_SIZE / 2 + 1 -> PieceColor.WHITE
                else -> null
            }

            row.add(Cell(Position(c.toColumn(), Row(r)), pieceColor))
        }
        cells.add(row)
    }

    return Board(cells)
}