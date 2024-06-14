package com.example.front_end_android.dataclass

data class FindResponse(
    val status: String,
    val data: FindData?,
    val message: String
)

data class FindData(
    val id: Int,
    val email: String,
    val password: String,
    val name: String,
    val firebaseToken: String,
    val language: String,
    val role: String
)
