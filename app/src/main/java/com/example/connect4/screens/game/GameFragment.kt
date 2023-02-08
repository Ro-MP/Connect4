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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.connect4.R
import com.example.connect4.databinding.FragmentGameBinding
import kotlinx.android.synthetic.main.fragment_game.*

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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        //_binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        _binding?.apply {
            gameViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }




        viewModel.movementsDone.observe(viewLifecycleOwner, Observer {
            tintRaws(viewModel.currentColumn, viewModel.currentRaw)
        })

        if (viewModel.movementsDone.value == 0){
            tintBoard()
        }

        /*viewModel.title.observe(viewLifecycleOwner, Observer { newTitle ->
            binding.tvTitle.text = newTitle
        })*/

        viewModel.isAlreadyAWinner.observe(viewLifecycleOwner, Observer { isAlreadyAWinner ->
            // Deshabilita las CardView para ser clickable y habilita el boton de Next
            if (isAlreadyAWinner) {
                binding.tablero.forEach {
                    it.isClickable = false
                }
                with(binding.btnNext){
                    this.visibility = Button.VISIBLE
                    this.isClickable = true
                }
            }
        })

        // Se inicializan los listeners para cuando se presionan las columnas
        binding.tablero.forEach { column ->
            column.setOnClickListener {
                viewModel.addTokenToColumn(binding.tablero.indexOfChild(column))
            }
        }

        binding.btnNext.setOnClickListener {
            view.findNavController().navigate(GameFragmentDirections
                .actionGameFragmentToScoreFragment(viewModel.turnColor, viewModel.currentTimeString.value?:""))
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun tintBoard(){
        for (columnIndex in 0 until NUMBEROFCOLUMNS){
            tintColumn(columnIndex)
        }
    }


    private fun tintColumn(columnIndex: Int){
        for (raw in 0 until NUMBEROFRAWS){
            tintRaws(columnIndex, raw)
        }
    }


    private fun tintRaws(columnIndex: Int, raw: Int){
        val column: CardView = binding.tablero.get(columnIndex) as CardView
        val linearLayout = column.get(0) as LinearLayout
        val itemColor = viewModel.matrix.value?.get("column${binding.tablero.indexOfChild(column)}")?.get(raw) ?: 0

        linearLayout.get(raw).backgroundTintList =
            if (itemColor != 0){
                if(itemColor > 0) ContextCompat.getColorStateList(binding.root.context, R.color.blue)
                else ContextCompat.getColorStateList(binding.root.context, R.color.red)
            } else ContextCompat.getColorStateList(binding.root.context, R.color.sahding_yellow)
    }










}