package com.example.front_end_android.dataclass

import com.google.gson.annotations.SerializedName

data class CallFCMRequest(
    @SerializedName("targetUserEmail")
    val targetUserEmail: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String
)
