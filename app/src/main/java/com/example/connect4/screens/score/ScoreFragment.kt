package com.example.connect4.screens.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.connect4.R
import com.example.connect4.database.ScoreDatabase
import com.example.connect4.databinding.FragmentScoreBinding


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
        val args = ScoreFragmentArgs.fromBundle(requireArguments())

        // Get reference to the application context
        val application = requireNotNull(this.activity).application

        // Get reference to the DAO of the datasource
        val dataSource = ScoreDatabase.getInstance(application).scoreDatabaseDao


        viewModelFactory = ScoreViewModelFactory(args.winner, args.timeLength, dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

        _binding?.apply {
            scoreViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.tvTitle.text = getString(R.string.score_title, viewModel.winner)

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}