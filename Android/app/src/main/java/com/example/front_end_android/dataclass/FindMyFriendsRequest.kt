package com.example.front_end_android.dataclass

import com.google.gson.annotations.SerializedName

data class FindMyFriendsRequest(
    @SerializedName("email")
    val email: String
)
