package com.example.front_end_android.dataclass

import com.google.gson.JsonObject

data class LoginResponse(
    val status: String,
    val data:LoginData?,
    val message: String
)

data class LoginData(
    val accessToken:String?,
    val refreshToken:String?
)

data class LoginErrorResponse(
    val status: String,
    val message: String,
    val data: Any
)