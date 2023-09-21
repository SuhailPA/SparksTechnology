package com.example.sparktech.ui.dashboard

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparktech.data.model.DashboardData
import com.example.sparktech.repository.DashboardRepository
import com.example.sparktech.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    private val encryptedPref: SharedPreferences
) : ViewModel() {

    private val _dashBoardList = MutableStateFlow<ApiState>(ApiState.Loading)
    val dashBoardList: StateFlow<ApiState> = _dashBoardList

    init {
        getAllData()
    }

    private fun getAllData() {
        viewModelScope.launch {
            _dashBoardList.value = try {
                val response = repository.getAllData()
                if (response.isSuccessful) {
                    ApiState.Success(response.body())
                } else {
                    ApiState.Error(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                ApiState.Error(e.message)

            } catch (e: HttpException) {
                ApiState.Error(e.message)
            }
        }
    }

    fun clearSharedPref() {
        encryptedPref.edit().clear().apply()
    }
}