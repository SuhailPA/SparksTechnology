package com.example.sparktech.ui.login_register


import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparktech.data.model.ErrorBody
import com.example.sparktech.data.model.LoginError
import com.example.sparktech.data.model.LoginResponse
import com.example.sparktech.data.model.NetworkResponse
import com.example.sparktech.data.model.UserData
import com.example.sparktech.data.model.UserLogin
import com.example.sparktech.data.model.UserLoginResponse
import com.example.sparktech.data.model.UserRegResponse
import com.example.sparktech.repository.LoginRegisterRepositoryImpl
import com.example.sparktech.utils.ApiState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val repository: LoginRegisterRepositoryImpl,
    private val encryptedPref: SharedPreferences
) : ViewModel() {

    /**
     * For Flow purpose
     */
//    private var _userDataFlow = MutableStateFlow<NetworkResponse?>(null)
//    val userDataFlow: StateFlow<NetworkResponse?> = _userDataFlow

    private var _userData = MutableLiveData<ApiState?>(null)
    val userData: LiveData<ApiState?> = _userData

    private var _userLogin = MutableLiveData<ApiState?>(null)
    val userLogin: LiveData<ApiState?> = _userLogin
    fun userRegister(userData: UserData) {
        viewModelScope.launch {
            try {
                val response = repository.userRegistration(user = userData)
                _userData.value = if (response.isSuccessful) {
                    ApiState.Success(response.body()!!)
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    ApiState.Error(errorResponse)
                }
            } catch (e: IOException) {
                _userData.value = ApiState.Error(e.message.toString())
            } catch (e: HttpException) {
                _userData.value = ApiState.Error(e.message.toString())
            } catch (e: Exception) {
                _userData.value = ApiState.Error(e.message.toString())
            }
        }
    }


    fun userSignIn(userLogin: UserLogin) {
        viewModelScope.launch {
            try {
                val response = repository.userLogin(userLogin = userLogin)
                _userLogin.value = if (response.isSuccessful) {
                    storeInEncryptedPref("accessToken", response.body()?.access.orEmpty())
                    storeInEncryptedPref("refreshToken", response.body()?.access.orEmpty())
                    ApiState.Success(response.body()!!)
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()?.string(), LoginError::class.java)
                    ApiState.Error(errorResponse.detail)
                }
            } catch (e: IOException) {
                _userLogin.value = ApiState.Error(e.message)
            } catch (e: HttpException) {
                _userLogin.value = ApiState.Error(e.message)
            }
        }
    }

    private fun storeInEncryptedPref(key: String, value: String) {
        val editor = encryptedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        val accessToken = encryptedPref.getString("accessToken", null)
        return accessToken != null
    }
}