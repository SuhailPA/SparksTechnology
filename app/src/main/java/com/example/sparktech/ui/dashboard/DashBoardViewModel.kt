package com.example.sparktech.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparktech.data.model.DashboardData
import com.example.sparktech.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _dashBoardList = MutableStateFlow<List<DashboardData>?>(null)
    val dashBoardList: StateFlow<List<DashboardData>?> = _dashBoardList

    init {
        getAllData()
    }

    private fun getAllData() {
        viewModelScope.launch {
            try {
                val response = repository.getAllData()
                if (response.isSuccessful) {
                    _dashBoardList.value = response.body()
                } else {

                }
            } catch (e: IOException) {

            } catch (e: HttpException) {

            }
        }
    }
}