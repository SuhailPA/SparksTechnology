package com.example.sparktech.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sparktech.R
import com.example.sparktech.databinding.FragmentDashBoardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashBoardFragment : Fragment() {

    lateinit var binding: FragmentDashBoardBinding
    private val viewModel: DashBoardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        initUi()
        return binding.root
    }

    private fun initUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dashBoardList.collect {
                binding.DashboardResult.text = it.toString()
            }
        }

    }

}