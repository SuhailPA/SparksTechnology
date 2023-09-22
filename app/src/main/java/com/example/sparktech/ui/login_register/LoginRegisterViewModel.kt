package com.example.sparktech.ui.login_register


import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparktech.data.model.ErrorBody
import com.example.sparktech.data.model.LoginError
import com.example.sparktech.data.model.UserData
import com.example.sparktech.data.model.UserLogin
import com.example.sparktech.repository.LoginRegisterRepositoryImpl
import com.example.sparktech.utils.ApiState
import com.example.sparktech.utils.Event
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _registerData = MutableLiveData<Event<ApiState>>()
    val registerData: LiveData<Event<ApiState>>
        get() = _registerData

    private val _loginData = MutableLiveData<Event<ApiState>>()
    val loginData: LiveData<Event<ApiState>>
        get() = _loginData

    fun userRegister(userData: UserData) {
        viewModelScope.launch {
            try {
                val response = repository.userRegistration(user = userData)
                _registerData.value = if (response.isSuccessful) {
                    Event(ApiState.Success(response.body()!!))
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    Event(ApiState.Error(errorResponse))
                }
            } catch (e: IOException) {
                _registerData.value = Event(ApiState.Error(e.message.toString()))
            } catch (e: HttpException) {
                _registerData.value = Event(ApiState.Error(e.message.toString()))
            } catch (e: Exception) {
                _registerData.value = Event(ApiState.Error(e.message.toString()))
            }
        }
    }


    fun userSignIn(userLogin: UserLogin) {
        viewModelScope.launch {
            try {
                val response = repository.userLogin(userLogin = userLogin)
                _loginData.value = if (response.isSuccessful) {
                    storeInEncryptedPref("accessToken", response.body()?.access.orEmpty())
                    storeInEncryptedPref("refreshToken", response.body()?.access.orEmpty())
                    Event(ApiState.Success(response.body()!!))
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()?.string(), LoginError::class.java)
                    Event(ApiState.Error(errorResponse.detail))
                }
            } catch (e: IOException) {
                _loginData.value = Event(ApiState.Error(e.message))
            } catch (e: HttpException) {
                _loginData.value = Event(ApiState.Error(e.message))
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