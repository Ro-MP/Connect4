package com.example.connect4.screens.scoreDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.connect4.R
import com.example.connect4.database.ScoreDatabase
import com.example.connect4.databinding.FragmentScoreDetailBinding
import com.example.connect4.nullRepresentByDecimal
import com.example.connect4.screens.score.ScoreViewModel
import com.example.connect4.screens.score.ScoreViewModelFactory


class ScoreDetailFragment : Fragment() {

    private var _binding: FragmentScoreDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ScoreDetailViewModelFactory
    private lateinit var viewModel: ScoreDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentScoreDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        val args = ScoreDetailFragmentArgs.fromBundle(requireArguments())


        val application = requireNotNull(this.activity).application
        val dataSource = ScoreDatabase.getInstance(application).scoreDatabaseDao

        viewModelFactory = ScoreDetailViewModelFactory(
            args.scoreId,
            dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreDetailViewModel::class.java)

        _binding?.apply {
            scoreDetailViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }


        return view
    }

}