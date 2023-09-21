package com.example.sparktech.repository

import com.example.sparktech.data.model.DashboardData
import com.example.sparktech.data.remote.SparkAPI
import retrofit2.Response
import javax.inject.Inject

interface DashboardRepositoryImpl {
    suspend fun getAllData(): Response<List<DashboardData>>
}

class DashboardRepository @Inject constructor(val sparkAPI: SparkAPI) : DashboardRepositoryImpl {
    override suspend fun getAllData(): Response<List<DashboardData>> = sparkAPI.getAllData()

}