package com.example.connect4.screens.game

import android.os.Bundle
import android.text.BoringLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.navigation.findNavController
import com.example.connect4.R
import com.example.connect4.databinding.FragmentGameBinding
import java.util.ArrayList

const val NUMBEROFCOLUMNS = 7
const val NUMBEROFRAWS = 6
const val BLUE = "blue"
const val RED = "red"

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    // Values are asign considering a fixed board's matrix [6, 7]
    private var fullLevelRaw = mutableListOf(6, 6, 6, 6, 6, 6, 6)
    private var matrix = mutableMapOf<String, MutableList<Int>>()
    private var turnColor = BLUE


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inicializa la Matriz de valores desde null o desde valores guardados en onSaveInstanceState()
        if (savedInstanceState == null) {
            startMatrix()
        } else {
            binding.tablero.forEach {
                val id = it.id.toString()
                matrix[id] = savedInstanceState.getIntegerArrayList(id) as MutableList<Int>
            }
            fullLevelRaw = savedInstanceState.getIntegerArrayList("FULLRAW") as MutableList<Int>
            binding.tvTitle.text = savedInstanceState.getString(binding.tvTitle.id.toString())
            binding.btnNext.visibility = savedInstanceState.getInt(binding.btnNext.id.toString())
        }

        tintBoard()

        // Se inicializan los listeners para cuando se presionan las columnas
        binding.tablero.forEach {
            it.setOnClickListener { addTokenToColumn(it as CardView)  }
        }

        binding.btnNext.setOnClickListener {
            view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToScoreFragment(turnColor))
        }

        return view
    }


    override fun onSaveInstanceState(outState: Bundle) {
        // Almacena valores de matrix
        binding.tablero.forEach {
            val id = it.id.toString()
            outState.putIntegerArrayList(id, matrix[id] as ArrayList<Int>)
        }
        // Almacena valores de fullLevelRaw
        outState.putIntegerArrayList("FULLRAW", fullLevelRaw as ArrayList<Int>)
        // Almacena valores de Tv_title
        outState.putString(binding.tvTitle.id.toString(), binding.tvTitle.text.toString())
        // Almacena visibilidad de next Button
        outState.putInt(binding.btnNext.id.toString(), binding.btnNext.visibility)

        super.onSaveInstanceState(outState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun startMatrix(){
        binding.tablero.forEach {
            matrix[it.id.toString()] = mutableListOf( 0, 0, 0, 0, 0, 0)
        }
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

        val itemColor = matrix[column.id.toString()]?.get(raw)!!
        linearLayout.get(raw).backgroundTintList =
            if (itemColor != 0){
                if(itemColor > 0) ContextCompat.getColorStateList(binding.root.context, R.color.blue)
                else ContextCompat.getColorStateList(binding.root.context, R.color.red)
            } else ContextCompat.getColorStateList(binding.root.context, R.color.sahding_yellow)
    }


    fun addTokenToColumn(column: CardView){
        val columnIndex = binding.tablero.indexOfChild(column)
        val columnId = column.id.toString()

        // Comprueba que el tablero no este lleno
        if(fullLevelRaw[columnIndex] > 0){
            fullLevelRaw[columnIndex] -= 1

            //Le asigna el valor seleccionado al las raws de la columna
            matrix[columnId]?.set(fullLevelRaw[columnIndex], if (turnColor == BLUE) 1  else -1)
            tintRaws(column, fullLevelRaw[columnIndex])
            if (!detectFourInLine(column, fullLevelRaw[columnIndex])) {
                turnColor = if (turnColor == BLUE) RED else BLUE
                changeTitle()
            }

        }

    }


    private fun detectFourInLine(currentColumn: CardView, currentRaw: Int): Boolean  {
        var itemsInLine = 1

        // Prove Diagonal NorthEast / SouthWest
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.NORTHEAST)
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.SOUTHWEST)
        if (iswinner(itemsInLine)) return true
        else itemsInLine = 1

        // Prove  Horizontal
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.EAST)
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.WEST)
        if (iswinner(itemsInLine)) return true
        else itemsInLine = 1

        // Prove Vertical
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.SOUTH)
        if (iswinner(itemsInLine)) return true
        else itemsInLine = 1

        // Prove Diagonal NorthWest / SouthEast
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.NORTHWEST)
        itemsInLine = isSame(currentColumn, currentRaw, itemsInLine, Directions.SOUTHEAST)
        if (iswinner(itemsInLine)) return true

        return false
    }


    // monitor if the next item on the passed direction has or not the same color,
    //  but first verify that space exists
    private fun isSame(currentColumn: CardView, currentRaw: Int, sum: Int, direction: Directions): Int{
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


    fun iswinner(itemsInLine: Int): Boolean {
        if (itemsInLine >= 4) {
            if (turnColor == RED) {
                println(getString(R.string.tv_winner_red))
                binding.tvTitle.text = getString(R.string.tv_winner_red)
            } else{
                println(getString(R.string.tv_winner_blue))
                binding.tvTitle.text = getString(R.string.tv_winner_blue)
            }
            // Deshabilita las CardView para ser clickable
            binding.tablero.forEach {
                it.isClickable = false
            }
            binding.btnNext.visibility = Button.VISIBLE
            return true
        } else {
            println("Falta barrio")
            return false
        }
    }


    fun changeTitle(){
        if (turnColor == RED){
            binding.tvTitle.text = getString(R.string.tv_turn_red)
        } else
            binding.tvTitle.text = getString(R.string.tv_turn_blue)
    }



}