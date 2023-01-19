package com.example.connect4.screens.game

import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.connect4.R
import com.example.connect4.databinding.FragmentGameBinding

const val NUMBEROFCOLUMNS = 7
const val NUMBEROFRAWS = 6
const val BLUE = "blue"
const val RED = "red"

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        tintBoard()

        // Se inicializan los listeners para cuando se presionan las columnas
        binding.tablero.forEach { column ->
            column.setOnClickListener { addTokenToColumn(column as CardView)  }
        }

        binding.btnNext.setOnClickListener {
            view.findNavController().navigate(GameFragmentDirections
                .actionGameFragmentToScoreFragment(viewModel.turnColor))
        }
        binding.tvTitle.text = viewModel.title
        if (viewModel.isAlreadyAWinner){
            disableCardsClickable()
            setButtonNextEnable()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun addTokenToColumn(column: CardView){
        val columnIndex = binding.tablero.indexOfChild(column)

        // Comprueba que el tablero no este lleno
        if(viewModel.fullLevelRaw[columnIndex] > 0){
            viewModel.fullLevelRaw[columnIndex] -= 1

            //Le asigna el valor seleccionado al las raws de la columna
            viewModel.matrix["column$columnIndex"]?.set(
                viewModel.fullLevelRaw[columnIndex], if (viewModel.turnColor == BLUE) 1  else -1)
            tintRaws(column, viewModel.fullLevelRaw[columnIndex])
            if (viewModel.detectFourInLine(columnIndex, viewModel.fullLevelRaw[columnIndex])) {
                setWinner()

            } else{
                viewModel.turnColor = if (viewModel.turnColor == BLUE) RED else BLUE
                changeTitle()
            }
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
        val itemColor = viewModel.matrix["column${binding.tablero.indexOfChild(column)}"]
            ?.get(raw)!!

        linearLayout.get(raw).backgroundTintList =
            if (itemColor != 0){
                if(itemColor > 0) ContextCompat.getColorStateList(binding.root.context, R.color.blue)
                else ContextCompat.getColorStateList(binding.root.context, R.color.red)
            } else ContextCompat.getColorStateList(binding.root.context, R.color.sahding_yellow)
    }


    private fun disableCardsClickable(){
        // Deshabilita las CardView para ser clickable
        binding.tablero.forEach {
            it.isClickable = false
        }
    }

    private fun setButtonNextEnable(){
        with(binding.btnNext){
            this.visibility = Button.VISIBLE
            this.isClickable = true
        }
    }



    private fun changeTitle(){
        if (viewModel.turnColor == RED){
            viewModel.title = getString(R.string.tv_turn_red)
            binding.tvTitle.text = viewModel.title
        } else{
            viewModel.title = getString(R.string.tv_turn_blue)
            binding.tvTitle.text = viewModel.title
        }

    }

    private fun setWinner(){
        disableCardsClickable()
        setButtonNextEnable()
        viewModel.isAlreadyAWinner = true
        if (viewModel.turnColor == RED) {
            viewModel.title = getString(R.string.tv_winner_red)
        } else{
            viewModel.title = getString(R.string.tv_winner_blue)
        }
        binding.tvTitle.text = viewModel.title
    }


}