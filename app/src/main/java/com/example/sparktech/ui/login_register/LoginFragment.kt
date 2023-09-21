package com.example.sparktech.ui.login_register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.sparktech.data.model.LoginResponse
import com.example.sparktech.data.model.UserLogin
import com.example.sparktech.databinding.FragmentLoginBinding
import com.example.sparktech.ui.MainActivity
import com.example.sparktech.utils.getString
import com.example.sparktech.utils.isNotEmpty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val viewModel: LoginRegisterViewModel by viewModels()

    private lateinit var navController: NavController
    private var isUserLoggedIn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUserLoggedIn = viewModel.isUserLoggedIn()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        navController = findNavController()
        if (isUserLoggedIn) navController.navigate(directions = LoginFragmentDirections.actionLoginFragmentToDashBoardFragment())
        (activity as MainActivity?)?.supportActionBar?.apply {
            title = "Login Screen"
            setDisplayHomeAsUpEnabled(false)
        }

        initUi()
        initLoginObserver()
        binding?.registerText?.setOnClickListener {
            findNavController().navigate(directions = LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        return binding?.root
    }

    private fun initLoginObserver() {
        viewModel.userLogin.observe(viewLifecycleOwner) { loginResponse ->
            when (loginResponse) {
                is LoginResponse.Success -> {
                    navController.navigate(directions = LoginFragmentDirections.actionLoginFragmentToDashBoardFragment())
                }

                is LoginResponse.Error -> {
                    Toast.makeText(context, loginResponse.errorMessage, Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }
    }

    private fun initUi() {
        binding?.apply {
            loginButton.setOnClickListener {
                if (isValidate()) {
                    val userLogin = UserLogin(
                        username = userName.getString(),
                        password = userPassword.getString()
                    )
                    viewModel.userSignIn(userLogin = userLogin)
                }
            }
        }
    }

    fun isValidate(): Boolean {
        binding?.apply {
            return userName.isNotEmpty() &&
                    userPassword.isNotEmpty()
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}