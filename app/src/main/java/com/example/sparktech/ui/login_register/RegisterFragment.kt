package com.example.sparktech.ui.login_register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sparktech.R
import com.example.sparktech.data.model.NetworkResponse
import com.example.sparktech.data.model.UserData
import com.example.sparktech.databinding.FragmentRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: LoginRegisterViewModel by viewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        initUi()
        initResponse()
        return binding?.root
    }

    private fun initResponse() {
        viewModel.userData.observe(viewLifecycleOwner) { networkResponse ->
            when (networkResponse) {
                is NetworkResponse.Success -> {
                    Toast.makeText(
                        context,
                        networkResponse.userRegResponse.username.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is NetworkResponse.Error -> {
                    if (!networkResponse.errorMessage.isNullOrEmpty()) {
                        networkResponse.errorMessage.let {
                            Toast.makeText(
                                context,
                                it,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        networkResponse.errorRegResponse?.let {
                            Toast.makeText(
                                context,
                                it.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                else -> {}
            }
        }
    }


    private fun validateFields(): Boolean {
        binding?.apply {
            return userRegFirstname.isNotEmpty() &&
                    userRegSecondname.isNotEmpty() &&
                    userRegEmail.isNotEmpty() &&
                    userRegPassword.isNotEmpty() &&
                    userPassword2.isNotEmpty() &&
                    userRegUserName.isNotEmpty()
        }
        return false
    }

    private fun initUi() {
        binding?.apply {
            userRegButton.setOnClickListener {
                if (validateFields()) {
                    val user = UserData(
                        username = userRegUserName.getString(),
                        password = userRegPassword.getString(),
                        password2 = userPassword2.getString(),
                        email = userRegEmail.getString(),
                        first_name = userRegFirstname.getString(),
                        last_name = userRegSecondname.getString()
                    )
                    viewModel.userRegister(userData = user)
                }
            }
        }
    }

    fun TextInputLayout.getString(): String {
        return this.editText?.text.toString()
    }

    fun TextInputLayout.isNotEmpty(): Boolean {
        return this.editText?.text?.isNotEmpty() ?: false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}