package com.example.sparktech.ui.login_register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.sparktech.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val viewModel: LoginRegisterViewModel by viewModels()

    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        navController = findNavController()

        (activity as MainActivity?)?.supportActionBar?.apply {
            title = "Login Screen"
            setDisplayHomeAsUpEnabled(false)
        }

        binding?.registerText?.setOnClickListener {
            findNavController().navigate(directions = LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}