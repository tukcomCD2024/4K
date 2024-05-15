package com.example.front_end_android.dataclass

data class ErrorResponse(
    val status: String,
    val message: String,
    val data: Any,
    val code: Any
)
