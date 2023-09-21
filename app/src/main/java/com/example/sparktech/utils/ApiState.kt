package com.example.sparktech.utils

sealed class ApiState {
    data class Success<out R>(val data: R) : ApiState()
    data class Error<out S>(val error : S) : ApiState()
    object Loading : ApiState()
}