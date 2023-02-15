package com.example.connect4.screens.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.connect4.R
import com.example.connect4.database.ScoreDatabase
import com.example.connect4.databinding.FragmentScoreBinding
import com.example.connect4.nullRepresentByDecimal
import com.example.connect4.screens.score.scoreRecyclerView.ScoreAdapter


class ScoreFragment : Fragment() {

    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScoreBinding.inflate(inflater, container, false)
        val view = binding.root
        val args = ScoreFragmentArgs.fromBundle(requireArguments())

        // Get reference to the application context
        val application = requireNotNull(this.activity).application

        // Get reference to the DAO of the datasource
        val dataSource = ScoreDatabase.getInstance(application).scoreDatabaseDao



        val scoreId = if (args.scoreId == nullRepresentByDecimal()) null else args.scoreId
        viewModelFactory = ScoreViewModelFactory(
            scoreId,
            args.winner,
            args.timeLength,
            dataSource,
            application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)


        _binding?.apply {
            scoreViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }


        // Inicializa adapter para RecyclerView y vincula al RV de la vista
        val adapter = ScoreAdapter()
        binding.rvScoresList.adapter = adapter
        // Mantiene actualizado el data del RVAdapter con el LiveData de scores del ViewModel
        viewModel.scores.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        viewModel.currentWinner.observe(viewLifecycleOwner, Observer {
            binding.tvTitle.text = getString(R.string.score_title, it)
        })



        binding.btnChangeName.setOnClickListener {
            viewModel.getNewScoreId()?.apply {
                view.findNavController().navigate(ScoreFragmentDirections
                    .actionScoreFragmentToWinnerNameFragment(this))
            }
        }

        return view
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}