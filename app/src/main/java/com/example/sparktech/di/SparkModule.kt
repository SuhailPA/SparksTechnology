package com.example.sparktech.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.sparktech.data.remote.SparkAPI
import com.example.sparktech.repository.DashboardRepository
import com.example.sparktech.repository.DashboardRepositoryImpl
import com.example.sparktech.repository.LoginRegisterRepository
import com.example.sparktech.repository.LoginRegisterRepositoryImpl
import com.example.sparktech.utils.OAuthInterceptor

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SparkModule {

    @Provides
    @Singleton
    fun providesContext(application: Application): Context {
        return application.applicationContext
    }


    @Singleton
    @Provides
    fun providesBaseURL(): String = "http://148.251.86.36:8001/"

    @Singleton
    @Provides
    fun providesRetrofitWithToken(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(providesBaseURL())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    @Singleton
    @Provides
    fun providesSparkAPI(retrofit: Retrofit): SparkAPI = retrofit.create(SparkAPI::class.java)

    @Singleton
    @Provides
    fun providesLoginRepository(sparkAPI: SparkAPI): LoginRegisterRepositoryImpl =
        LoginRegisterRepository(sparkAPI = sparkAPI)

    @Singleton
    @Provides
    fun providesEncryptedPref(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "spark_encrypted_pref",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }

    @Provides
    @Singleton
    fun providesOKHttpClient(encryptedPref: SharedPreferences): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val token = encryptedPref.getString("accessToken", "").orEmpty()
        return OkHttpClient.Builder().addInterceptor(
            OAuthInterceptor(encryptedPref)
        ).addInterceptor(interceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideDashBoardRepository(sparkAPI: SparkAPI): DashboardRepositoryImpl =
        DashboardRepository(sparkAPI)
}