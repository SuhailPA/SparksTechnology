package com.example.sparktech.repository

import com.example.sparktech.data.model.DashboardData
import com.example.sparktech.data.remote.SparkAPI
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

interface DashboardRepositoryImpl {
    suspend fun getAllData(): Response<List<DashboardData>>
}

class DashboardRepository @Inject constructor(private val sparkAPI: SparkAPI) : DashboardRepositoryImpl {
    override suspend fun getAllData(): Response<List<DashboardData>> = sparkAPI.getAllData()

}