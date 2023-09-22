package com.example.sparktech.ui.login_register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.sparktech.data.model.ErrorBody
import com.example.sparktech.data.model.NetworkResponse
import com.example.sparktech.data.model.UserData
import com.example.sparktech.databinding.FragmentRegisterBinding
import com.example.sparktech.ui.MainActivity
import com.example.sparktech.utils.ApiState
import com.example.sparktech.utils.TextInputViews
import com.example.sparktech.utils.getString
import com.example.sparktech.utils.isNotEmpty
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: LoginRegisterViewModel by viewModels()
    private lateinit var navController: NavController
    private var _binding: FragmentRegisterBinding? = null
    private lateinit var fieldToViewMap: Map<TextInputViews, TextInputLayout?>
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

        fieldToViewMap = mapOf(
            TextInputViews.username to binding?.userRegUserName,
            TextInputViews.password to binding?.userRegPassword,
            TextInputViews.email to binding?.userRegEmail,
            TextInputViews.first_name to binding?.userRegFirstname,
            TextInputViews.last_name to binding?.userRegSecondname
        )
        backButtonHandler()
        initUi()
        initResponse()
        textInputValidation()
        return binding?.root
    }

    private fun textInputValidation() {
        binding?.apply {
            userRegUsernameEditText.doOnTextChanged { text, _, _, _ ->
                userRegUserName.error = if (text?.length!! == 0) "Field can't be empty"
                else null
            }
            userRegFirstNameEditText.doOnTextChanged { text, _, _, _ ->
                userRegFirstname.error = if (text?.length!! == 0) "Field can't be empty"
                else null
            }
            userRegSecondNameEditText.doOnTextChanged { text, _, _, _ ->
                userRegSecondname.error = if (text?.length!! == 0) "Field can't be empty"
                else null
            }
            userRegEmailEditText.doOnTextChanged { text, _, _, _ ->
                userRegEmail.error = if (text?.length!! == 0) "Field can't be empty"
                else null
            }
            userRegPasswordEditText.doOnTextChanged { text, _, _, _ ->
                userRegPassword.error = if (text?.length!! == 0) "Field can't be empty"
                else null
            }
            userRegPassword2EditText.doOnTextChanged { text, _, _, _ ->
                userRegPassword.error = if (text?.length!! == 0) "Field can't be empty"
                else null
            }
        }

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
                    if (networkResponse.error != null) {
                        networkResponse.error.let {
                            val errorBody = networkResponse.error as ErrorBody
                            showCorrespondingErrorMessages(errorBody)
                        }
                    }
                }

                else -> {}
            }
        }
    }


    private fun showCorrespondingErrorMessages(errorBody: ErrorBody) {
        val errorFields = TextInputViews.values()


        for (field in errorFields) {
            val errors = when (field) {
                TextInputViews.username -> errorBody.username
                TextInputViews.password -> errorBody.password
                TextInputViews.email -> errorBody.email
                TextInputViews.password2 -> errorBody.password2
                TextInputViews.first_name -> errorBody.first_name
                TextInputViews.last_name -> errorBody.last_name
            }

            val view = fieldToViewMap[field]

            if (view is TextInputLayout) {
                view.error = errors?.joinToString("\n")
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
                hideKeyboard()
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
                } else {
                    showErrorMessage()
                }
            }
        }
    }

    private fun showErrorMessage() {
        for (textField in TextInputViews.values()) {
            val view = fieldToViewMap[textField]
            view?.let {
                if (!view.isNotEmpty()) {
                    view.requestFocus()
                    view.error = "Field can't be empty"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideKeyboard() {
        activity?.currentFocus?.let { view ->
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}