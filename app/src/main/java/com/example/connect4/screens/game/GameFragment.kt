package com.example.connect4.screens.game

import android.icu.text.AlphabeticIndex
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.helper.widget.MotionEffect.*
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.connect4.R
import com.example.connect4.databinding.FragmentGameBinding

const val NUMBEROFCOLUMNS = 7
const val NUMBEROFRAWS = 6

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    // Values are asign considering a fixed board's matrix [6, 7]
    private var fullLevelRaw = mutableListOf(6, 6, 6, 6, 6, 6, 6)
    private var matrix = mutableMapOf<String, MutableList<Int>>()
    private var turnColorId = R.color.blue


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        startMatrix()
        tintBoard()

        binding.column1.setOnClickListener { addTokenToColumn(it as CardView) }
        binding.column2.setOnClickListener { addTokenToColumn(it as CardView) }
        binding.column3.setOnClickListener { addTokenToColumn(it as CardView) }
        binding.column4.setOnClickListener { addTokenToColumn(it as CardView) }
        binding.column5.setOnClickListener { addTokenToColumn(it as CardView) }
        binding.column6.setOnClickListener { addTokenToColumn(it as CardView) }
        binding.column7.setOnClickListener { addTokenToColumn(it as CardView) }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun startMatrix(){
        matrix[binding.column1.id.toString()] = mutableListOf( 0, 0, 0, 0, 0, 0)
        matrix[binding.column2.id.toString()] = mutableListOf( 0, 0, 0, 0, 0, 0)
        matrix[binding.column3.id.toString()] = mutableListOf( 0, 0, 0, 0, 0, 0)
        matrix[binding.column4.id.toString()] = mutableListOf( 0, 0, 0, 0, 0, 0)
        matrix[binding.column5.id.toString()] = mutableListOf( 0, 0, 0, 0, 0, 0)
        matrix[binding.column6.id.toString()] = mutableListOf( 0, 0, 0, 0, 0, 0)
        matrix[binding.column7.id.toString()] = mutableListOf( 0, 0, 0, 0, 0, 0)
    }


    private fun tintBoard(){
        for (column in 0 until NUMBEROFCOLUMNS){
            tintColumn(binding.tablero[column] as CardView)
        }
    }


    private fun tintColumn(column: CardView){
        for (raw in 0 until NUMBEROFRAWS){
            tintRaws(column, raw)
        }
    }


    private fun tintRaws(column: CardView, raw: Int){
        val linearLayout = column.get(0) as LinearLayout

        linearLayout.get(raw).backgroundTintList = if (raw >= fullLevelRaw[binding.tablero.indexOfChild(column)]){
            ContextCompat.getColorStateList(binding.root.context, turnColorId)
        } else ContextCompat.getColorStateList(binding.root.context, R.color.sahding_yellow)

    }


    fun addTokenToColumn(column: CardView){
        val columnIndex = binding.tablero.indexOfChild(column)
        val columnId = column.id.toString()

        // Comprueba que el tablero no este lleno
        if(fullLevelRaw[columnIndex] > 0){
            fullLevelRaw[columnIndex] -= 1

            //Le asigna el valor seleccionado al las raws de la columna
            matrix[columnId]?.set(fullLevelRaw[columnIndex], if (turnColorId == R.color.blue) 1  else -1)
            tintRaws(column, fullLevelRaw[columnIndex])
            detectFourInLine(column, fullLevelRaw[columnIndex])
            turnColorId = if (turnColorId == R.color.blue) R.color.red else R.color.blue
        }

    }


    private fun detectFourInLine(currentColumn: CardView, currentRaw: Int) {
        val currentColumnIndex = binding.tablero.indexOfChild(currentColumn)
        var itemsInLine = 1


        println("\n### Turn: ${ContextCompat.getColorStateList(this.requireContext(), turnColorId).toString()}")

        // Prove Diagonal NorthEast / SouthWest
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Direction.NORTHEAST)
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Direction.SOUTHWEST)
        println("->NorthEast-SouthWest: $itemsInLine")
        itemsInLine = 1

        // Prove  Horizontal
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Direction.EAST)
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Direction.WEST)
        println("->Horizontal: $itemsInLine")
        itemsInLine = 1

        // Prove Vertical
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Direction.SOUTH)
        println("->Vertical: $itemsInLine")
        itemsInLine = 1

        // Prove Diagonal NorthWest / SouthEast
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Direction.NORTHWEST)
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Direction.SOUTHEAST)
        println("->NorthWest-SouthEast: $itemsInLine")


    }

    /*
    private fun isRightUpSame(currentColumn: CardView, currentRaw: Int, sum: Int): Int{
        val currentColumnIndex = binding.tablero.indexOfChild(currentColumn)

        // Corrobora que hay columna a la derecha
        if (currentColumnIndex < NUMBEROFCOLUMNS-1){
            val nextColumnIndex = currentColumnIndex+1
            val nextColumn = binding.tablero[nextColumnIndex] as CardView

            // Corrobora que hay al menos una fila superior
            if (currentRaw > 0){
                val nextRaw = currentRaw-1

                // Corrobora que sea el mismo color a la derecha/Arriba
                if (matrix[currentColumn.id.toString()]?.get(currentRaw) ==
                    matrix[nextColumn.id.toString()]?.get(nextRaw)){

                    return isRightUpSame(nextColumn, nextRaw, sum + 1)
                }
            }
        }
        return sum
    }

     */


/*
    private fun isLeftBottomSame(currentColumn: CardView, currentRaw: Int, sum: Int): Int{
        val currentColumnIndex = binding.tablero.indexOfChild(currentColumn)

        // Corrobora que hay columna a la izquierda
        if (currentColumnIndex > 0){
            val nextColumnIndex = currentColumnIndex-1
            val nextColumn = binding.tablero[nextColumnIndex] as CardView

            // Corrobora que hay al menos una fila inferior
            if (currentRaw < NUMBEROFRAWS-1){
                val nextRaw = currentRaw+1

                // Corrobora que sea el mismo color a la left/bottom
                if (matrix[currentColumn.id.toString()]?.get(currentRaw) ==
                    matrix[nextColumn.id.toString()]?.get(nextRaw)){

                    return isLeftBottomSame(nextColumn, nextRaw, sum + 1)
                }
            }
        }
        return sum
    }

 */

    private fun isSame(currentColumn: CardView, currentRaw: Int, sum: Int, direction: Direction): Int{
        val currentColumnIndex = binding.tablero.indexOfChild(currentColumn)

        // Corrobora que hay columna a la izquierda
        if (direction.columnCondition(currentColumnIndex, NUMBEROFCOLUMNS)){
            val nextColumnIndex = direction.nextColumnIndex(currentColumnIndex)
            val nextColumn = binding.tablero[nextColumnIndex] as CardView

            // Corrobora que hay al menos una fila inferior
            if (direction.rawCondition(currentRaw, NUMBEROFRAWS)){
                val nextRaw = direction.nextRaw(currentRaw)

                // Corrobora que sea el mismo color a la left/bottom
                if (matrix[currentColumn.id.toString()]?.get(currentRaw) ==
                    matrix[nextColumn.id.toString()]?.get(nextRaw)){

                    return isSame(nextColumn, nextRaw, sum + 1, direction)
                }
            }
        }
        return sum
    }



    enum class Direction {
        NORTHEAST,
        EAST,
        SOUTHEAST,
        SOUTH,
        SOUTHWEST,
        WEST,
        NORTHWEST;

        fun nextColumnIndex(currentColumnIndex: Int) : Int{
            return when(this) {
                NORTHEAST, EAST, SOUTHEAST -> {currentColumnIndex + 1}
                SOUTH -> currentColumnIndex
                SOUTHWEST, WEST, NORTHWEST -> {currentColumnIndex - 1}
            }
        }

        fun nextRaw(currentRaw: Int) : Int{
            return when(this) {
                NORTHEAST, NORTHWEST  -> {currentRaw - 1}
                EAST, WEST -> currentRaw
                SOUTHWEST, SOUTHEAST, SOUTH -> {currentRaw + 1}
            }
        }

        fun columnCondition(currentColumnIndex: Int, numberOfColumns: Int) : Boolean{
            return when(this) {
                NORTHEAST, EAST, SOUTHEAST -> {currentColumnIndex < numberOfColumns-1}
                SOUTH -> true
                SOUTHWEST, WEST, NORTHWEST -> {currentColumnIndex > 0}
            }
        }

        fun rawCondition(currentRaw: Int, numberOfRaws: Int) : Boolean{
            return when(this) {
                NORTHWEST, NORTHEAST -> {currentRaw > 0}
                EAST, WEST -> true
                SOUTHEAST, SOUTH, SOUTHWEST -> {currentRaw < NUMBEROFRAWS-1}
            }
        }
    }

    private fun isRightSame(){

    }

    private fun isRightBottomSame(){

    }

    private fun isButtomSame(){

    }


    private fun linearLayout(column: CardView): LinearLayout{
        return column.get(0) as LinearLayout
    }


}