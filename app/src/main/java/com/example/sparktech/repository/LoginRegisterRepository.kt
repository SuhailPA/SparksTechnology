package com.example.sparktech.repository


import com.example.sparktech.data.model.UserData
import com.example.sparktech.data.model.UserLogin
import com.example.sparktech.data.model.UserLoginResponse
import com.example.sparktech.data.model.UserRegResponse
import com.example.sparktech.data.remote.SparkAPI
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named


interface LoginRegisterRepositoryImpl {
    suspend fun userRegistration(user: UserData): Response<UserRegResponse>
    suspend fun userLogin(userLogin: UserLogin): Response<UserLoginResponse>
}

class LoginRegisterRepository @Inject constructor( val sparkAPI: SparkAPI) :
    LoginRegisterRepositoryImpl {
    override suspend fun userRegistration(user: UserData): Response<UserRegResponse> =
        sparkAPI.registerUser(userData = user)

    override suspend fun userLogin(userLogin: UserLogin): Response<UserLoginResponse> =
        sparkAPI.userLogin(userLogin = userLogin)


}