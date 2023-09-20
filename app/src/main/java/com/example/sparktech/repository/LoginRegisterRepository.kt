package com.example.sparktech.repository


import com.example.sparktech.data.model.UserData
import com.example.sparktech.data.model.UserRegResponse
import com.example.sparktech.data.remote.SparkAPI
import retrofit2.Response


interface LoginRegisterRepositoryImpl {
    suspend fun userRegistration(user: UserData): Response<UserRegResponse>
}

class LoginRegisterRepository(val sparkAPI: SparkAPI) : LoginRegisterRepositoryImpl {
    override suspend fun userRegistration(user: UserData): Response<UserRegResponse> =
        sparkAPI.registerUser(userData = user)



}