package com.example.front_end_android.dataclass

import com.google.gson.annotations.SerializedName

data class FriendRequestListResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("email")
    val email: String
)

// 배열 형태로 오는 경우를 처리하기 위해 리스트로 변경
data class FriendRequestListResponseArray(
    val friendRequests: List<FriendRequestListResponse>
)