package com.example.sparktech.di

import com.example.sparktech.data.remote.SparkAPI
import com.example.sparktech.repository.LoginRegisterRepository
import com.example.sparktech.repository.LoginRegisterRepositoryImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SparkModule {

    @Singleton
    @Provides
    fun baseUrl(): String = "http://148.251.86.36:8001/"

    @Singleton
    @Provides
    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Singleton
    @Provides
    fun sparkApi(): SparkAPI = retrofit().create(SparkAPI::class.java)

    @Singleton
    @Provides
    fun loginRepository(): LoginRegisterRepositoryImpl = LoginRegisterRepository(sparkAPI = sparkApi())

}