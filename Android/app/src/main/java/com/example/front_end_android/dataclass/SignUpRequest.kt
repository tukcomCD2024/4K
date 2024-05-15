package com.example.front_end_android.dataclass

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val language: String
)
