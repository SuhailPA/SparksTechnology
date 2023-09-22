package com.example.sparktech.data.model

data class UserData(
    val username: String,
    val password: String,
    val password2: String,
    val email: String,
    val first_name: String,
    val last_name: String
)

data class ErrorBody(
    val username: List<String?>?,
    val password: List<String?>?,
    val email: List<String?>?,
    val password2: List<String?>?,
    val first_name: List<String?>?,
    val last_name: List<String?>?
)

