package com.example.sparktech.data.remote

import com.example.sparktech.data.model.DashboardData
import com.example.sparktech.data.model.UserData
import com.example.sparktech.data.model.UserLogin
import com.example.sparktech.data.model.UserLoginResponse
import com.example.sparktech.data.model.UserRegResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface SparkAPI {

    @POST("register/")
    suspend fun registerUser(@Body userData: UserData): Response<UserRegResponse>

    @POST("login/")
    suspend fun userLogin(@Body userLogin: UserLogin): Response<UserLoginResponse>

    @GET("dashboard/")
    suspend fun getAllData(): Response<List<DashboardData>>

}