package com.example.sparktech.data.model

data class UserLoginResponse(
    val access: String,
    val email: String,
    val firstname: String,
    val lastname: String,
    val refresh: String,
    val username: String
)

data class UserLogin(
    val username: String,
    val password: String
)

sealed interface LoginResponse {
    data class Success(val userLoginResponse: UserLoginResponse) : LoginResponse
    data class Error(val errorRegResponse: ErrorBody? = null, val errorMessage: String? = null) :
        LoginResponse

}