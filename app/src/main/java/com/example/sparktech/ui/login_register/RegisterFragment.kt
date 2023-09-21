package com.example.sparktech.ui.login_register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.sparktech.data.model.NetworkResponse
import com.example.sparktech.data.model.UserData
import com.example.sparktech.databinding.FragmentRegisterBinding
import com.example.sparktech.ui.MainActivity
import com.example.sparktech.utils.ApiState
import com.example.sparktech.utils.getString
import com.example.sparktech.utils.isNotEmpty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: LoginRegisterViewModel by viewModels()
    private lateinit var navController: NavController
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
        navController = findNavController()
        (activity as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Register Screen"
        }
        backButtonHandler()
        initUi()
        initResponse()
        return binding?.root
    }

    private fun backButtonHandler() {
        (activity as MainActivity).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> navController.navigateUp()
                }
                return true
            }

        })
    }

    private fun initResponse() {
        viewModel.userData.observe(viewLifecycleOwner) { networkResponse ->
            when (networkResponse) {
                is ApiState.Success<*> -> {
                    Toast.makeText(
                        context,
                        "Successfully Registered",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigateUp()
                }

                is ApiState.Error<*> -> {
                    if (networkResponse.error !=null) {
                        networkResponse.error.let {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}