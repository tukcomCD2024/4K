package com.example.front_end_android

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: Long,
    @SerializedName("email")
    var email: String,
    @SerializedName("loginEmail")
    var loginEmail: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("firebaseToken")
    var firebaseToken: String
)

data class loginPost(
    @SerializedName("loginEmail")
    var loginEmail: String,
    @SerializedName("password")
    var password: String
)

data class loginResponse(
    var responselogin: String
)
