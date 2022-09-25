package com.example.android.click.main_package

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.android.click.R
import com.example.android.click.databinding.FragmentHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class home : Fragment() {


    val viewModel : MainViewModel by activityViewModels()

    private lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHighScore()

        binding.StartTest.setOnClickListener {
            GlobalScope.launch {
                //todo : click sound
                this.cancel()
            }
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_home2_to_runTest)
        }


    }



}