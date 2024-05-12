package com.example.front_end_android

import com.example.front_end_android.dataclass.LoginRequest
import com.example.front_end_android.dataclass.LoginResponse
import com.example.front_end_android.dataclass.SignUpRequest
import com.example.front_end_android.dataclass.SignUpResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService {

    @POST("member/signup")
    fun signUpRetrofit(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST("member/login")
    fun loginRetrofit(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @FormUrlEncoded
    @POST("friendship/findByUserIdAndStatusOrFriendIdAndStatusAny")
    fun myFriendsRetrofit(
        @Field("userId") userId: Int
    ): Call<Any>

    @FormUrlEncoded
    @POST("member/save")
    fun setUser(@Field("email") email: String,
                @Field("password") password:String,
                @Field("name") name: String):Call<JsonObject>

}