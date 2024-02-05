package com.example.front_end_android

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService {

    @FormUrlEncoded
    @POST("member/login")
    fun loginRetrofit(@Field("loginEmail") email: String,
                @Field("password") password:String): Call<JsonObject>

}