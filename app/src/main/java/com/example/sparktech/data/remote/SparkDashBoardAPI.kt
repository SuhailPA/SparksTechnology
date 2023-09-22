package com.example.sparktech.data.remote

import com.example.sparktech.data.model.DashboardData
import retrofit2.Response
import retrofit2.http.GET

interface SparkDashBoardAPI {
    @GET("dashboard/")
    suspend fun getAllData(): Response<List<DashboardData>>
}