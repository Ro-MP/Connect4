package com.example.connect4.screens.winnerName

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.connect4.R
import com.example.connect4.database.ScoreDatabase
import com.example.connect4.databinding.FragmentWinnerNameBinding

class WinnerNameFragment : Fragment() {

    private var _binding: FragmentWinnerNameBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WinnerNameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWinnerNameBinding.inflate(inflater, container, false)
        val view = binding.root
        val args = WinnerNameFragmentArgs.fromBundle(requireArguments())


        val application = requireNotNull(this.activity).application
        val dataSource = ScoreDatabase.getInstance(application).scoreDatabaseDao

        val viewModelFactory = WinnerNameViewModelFactory(args.scoreId, dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WinnerNameViewModel::class.java)

        _binding?.apply {
            winnerNameViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }


        binding.etWinnerName.doOnTextChanged { text, start, before, count ->
            //val text = binding.etWinnerName.text.toString()
            viewModel.setNewName(text.toString())
                //binding.tilWinnerName.error = null

        }

        viewModel.isEditTextNull.observe(viewLifecycleOwner, Observer { isEditTextNull ->
            if (isEditTextNull) {
                binding.tilWinnerName.error = getString(R.string.error)
            }
        })

        viewModel.onBackToScoreFragment.observe(viewLifecycleOwner, Observer {

            backToScoreFragment(view, args.scoreId)
        })



        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun backToScoreFragment(view: View, scoreId: Long) {
        view.findNavController().navigate(
            WinnerNameFragmentDirections.actionWinnerNameFragmentToScoreFragment(
                scoreId,
                null,
                null
            )
        )
    }

}