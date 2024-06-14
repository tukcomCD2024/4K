package com.example.front_end_android.dataclass

import com.google.gson.annotations.SerializedName

data class UpdateRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String?,
    @SerializedName("newPassword")
    val newPassword: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("language")
    val language: String
)
