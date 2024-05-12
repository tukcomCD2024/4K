package com.example.front_end_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.front_end_android.databinding.ActivityAddFriendBinding
import com.example.front_end_android.dataclass.AddFriendRequest
import com.example.front_end_android.dataclass.LoginResponse
import com.example.front_end_android.util.AuthInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class AddFriend : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backFriendsImg.setOnClickListener {
            finish()
        }
        binding.backFriendsTxt.setOnClickListener {
            finish()
        }

        binding.createAccountBtn.setOnClickListener {

            val accessToken = MyApplication.preferences.getString("AccessToken",".")
            val senderEmail = MyApplication.preferences.getString("email",".")
            val receiverEmail = binding.receiverEmailEdt.text.toString()

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(accessToken))
                .build()

            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://4kringo.shop:8080/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            val service = retrofit.create(RetrofitService::class.java)

            val addFriendRequest = AddFriendRequest(senderEmail, receiverEmail)
            val call = service.sendFriendRequestRetrofit(addFriendRequest)
            Log.d("YMC", "accessToken: $accessToken")

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {

                        val jsonResponse = response.body()
                        Log.d("YMC", "onResponse 성공: $jsonResponse $response")

                    } else {
                        Log.d("YMC", "onResponse 실패")//*
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("YMC", "onFailure 에러: ${t.message}")//*
                }
            })

        }

    }
}