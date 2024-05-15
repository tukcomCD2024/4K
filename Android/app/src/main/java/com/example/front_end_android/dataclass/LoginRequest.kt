package com.example.front_end_android.dataclass

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("loginEmail")
    val loginEmail: String,
    @SerializedName("password")
    val password: String
)