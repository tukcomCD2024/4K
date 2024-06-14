package com.example.front_end_android.dataclass

import com.google.gson.annotations.SerializedName

data class FindMyFriendsResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("email")
    val email: String
)
