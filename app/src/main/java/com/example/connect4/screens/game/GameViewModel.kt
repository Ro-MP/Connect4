package com.example.connect4.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connect4.R


class GameViewModel : ViewModel() {
    private val _matrix = MutableLiveData< MutableMap<String, MutableList<Int>> > ()
    val matrix: LiveData< MutableMap<String, MutableList<Int>> >
        get() = _matrix

    private val _title = MutableLiveData< String > ("BLUE STARTS")
    val title: LiveData<String>
        get() = _title

    private val _isAlreadyAWinner = MutableLiveData(false)
    val isAlreadyAWinner: LiveData<Boolean>
        get() = _isAlreadyAWinner

    private val _movementsDone = MutableLiveData<Int>()
    val movementsDone : LiveData<Int>
        get() = _movementsDone

    // Values are asign considering a fixed board's matrix [6, 7]
    var fullLevelRaw: MutableList<Int> = mutableListOf(6, 6, 6, 6, 6, 6, 6)
    var turnColor = "blue"
    private val numberOfColumns = 7
    private val numberOfRaws = 6

    var currentColumn = 0
    var currentRaw = 5



    init {
        startMatrix()
        _movementsDone.value = 0


    }



    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed")

    }

    private fun startMatrix(){
        for (cardIndex in 0 until numberOfColumns){
            if (matrix.value == null) {
                _matrix.value = mutableMapOf("column$cardIndex" to mutableListOf( 0, 0, 0, 0, 0, 0))
            } else {
                _matrix.value?.put("column$cardIndex", mutableListOf( 0, 0, 0, 0, 0, 0))
            }
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
                if (matrix.value?.get("column$currentColumn")?.get(currentRaw) ==
                    matrix.value?.get("column$nextColumn")?.get(nextRaw)){
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


    fun addTokenToColumn(columnIndex: Int){

        // Comprueba que el tablero no este lleno
        if(fullLevelRaw[columnIndex] > 0){
            fullLevelRaw[columnIndex] -= 1

            currentColumn = columnIndex
            currentRaw = fullLevelRaw[columnIndex]

            //Le asigna el valor seleccionado al las raws de la columna
            matrix.value?.get("column$columnIndex")?.set(
                fullLevelRaw[columnIndex], if (turnColor == BLUE) 1  else -1)


            if (detectFourInLine(columnIndex, fullLevelRaw[columnIndex])) {
                setWinner()
            } else{
                turnColor = if (turnColor == BLUE) RED else BLUE
                changeTitle()
            }

            _movementsDone.value = movementsDone.value?.plus(1)
        }

    }

    private fun changeTitle(){
        if (turnColor == RED){
            _title.value = TitlesText.TURNRED.text
        } else{
            _title.value = TitlesText.TURNBLUE.text
        }

    }

    private fun setWinner(){
        _isAlreadyAWinner.value = true
        if (turnColor == RED) {
            _title.value = TitlesText.WINNERRED.text
        } else{
            _title.value = TitlesText.WINNERBLUE.text
        }
    }

    enum class TitlesText(val text: String){
        INITIAL("BLUE STARTS"),
        TURNRED("TURN OF RED"),
        TURNBLUE("TURN OF BLUE"),
        WINNERRED("RED DEFEAT YOU"),
        WINNERBLUE("BLUE DEFEAT YOU")
    }


}