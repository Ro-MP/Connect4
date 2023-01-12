package com.example.connect4.screens.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.connect4.R
import com.example.connect4.databinding.FragmentTitleBinding


class TitleFragment : Fragment() {

    private var _binding: FragmentTitleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTitleBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnTitle.setOnClickListener {
            view.findNavController().navigate(R.id.action_titleFragment_to_gameFragment)
        }



        return view
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}