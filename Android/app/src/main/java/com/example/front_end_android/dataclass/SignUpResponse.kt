package com.example.front_end_android.dataclass

data class SignUpResponse(
    val status: String,
    val data: Any,
    val message: String
)

data class SignUpErrorResponse(
    val status: String,
    val message: String,
    val data: Any
)