package com.example.front_end_android

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService {

    @FormUrlEncoded
    @POST("member/login")
    fun loginRetrofit(
        @Field("loginEmail") loginEmail: String,
        @Field("password") password: String
    ): Call<Any>

    @FormUrlEncoded
    @POST("friendship/findByUserIdAndStatusOrFriendIdAndStatus")
    fun myFriendsRetrofit(
        @Field("userId") userId: Int
    ): Call<Any>

    @FormUrlEncoded
    @POST("member/save")
    fun setUser(@Field("email") email: String,
                @Field("password") password:String,
                @Field("name") name: String):Call<JsonObject>

}