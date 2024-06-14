package com.example.front_end_android.dataclass

import com.google.gson.annotations.SerializedName

data class AcceptFriendRequest(
    @SerializedName("senderEmail")
    val senderEmail: String,
    @SerializedName("receiverEmail")
    val receiverEmail: String
)
