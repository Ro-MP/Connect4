package com.example.connect4.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel


class GameViewModel : ViewModel() {
    // Values are asign considering a fixed board's matrix [6, 7]
    var fullLevelRaw: MutableList<Int>  = mutableListOf()
    var matrix = mutableMapOf<String, MutableList<Int>>()
    var turnColor = "blue"
    var title = "BLUE STARTS"
    private val numberOfColumns = 7
    private val numberOfRaws = 6
    var isAlreadyAWinner = false

    init {
        fullLevelRaw = mutableListOf(6, 6, 6, 6, 6, 6, 6)

        startMatrix()


    }



    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed")

    }

    private fun startMatrix(){
        for (cardIndex in 0 until numberOfColumns){
            matrix["column$cardIndex"] = mutableListOf( 0, 0, 0, 0, 0, 0)
        }
    }



    fun detectFourInLine(currentColumn: Int, currentRaw: Int): Boolean  {
        var itemsInLine = 1

        // Prove Diagonal NorthEast / SouthWest
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.NORTHEAST)
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.SOUTHWEST)
        if (isWinner(itemsInLine)) return true
        else itemsInLine = 1

        // Prove  Horizontal
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.EAST)
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.WEST)
        if (isWinner(itemsInLine)) return true
        else itemsInLine = 1

        // Prove Vertical
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.SOUTH)
        if (isWinner(itemsInLine)) return true
        else itemsInLine = 1

        // Prove Diagonal NorthWest / SouthEast
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.NORTHWEST)
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.SOUTHEAST)
        if (isWinner(itemsInLine)) return true

        return false
    }

    // monitor if the next item on the passed direction has or not the same color,
    //  but first verify that space exists
    private fun isSame(currentColumn: Int, currentRaw: Int, sum: Int, direction: Directions): Int{

        // Corrobora que hay columna a la izquierda
        if (direction.columnCondition(currentColumn, numberOfColumns)){
            val nextColumn = direction.nextColumnIndex(currentColumn)

            // Corrobora que hay al menos una fila inferior
            if (direction.rawCondition(currentRaw, numberOfRaws)){
                val nextRaw = direction.nextRaw(currentRaw)

                // Corrobora que sea el mismo color a la left/bottom
                if (matrix["column$currentColumn"]?.get(currentRaw) ==
                    matrix["column$nextColumn"]?.get(nextRaw)){

                    return isSame(nextColumn, nextRaw, sum + 1, direction)
                }
            }
        }
        return sum
    }

    private fun isWinner(itemsInLine: Int): Boolean {
        return if (itemsInLine >= 4) {
            true
        } else {
            println("Falta barrio")
            false
        }
    }

}