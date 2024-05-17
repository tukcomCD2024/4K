package com.example.front_end_android.dataclass

import com.google.gson.annotations.SerializedName

data class DeleteRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
