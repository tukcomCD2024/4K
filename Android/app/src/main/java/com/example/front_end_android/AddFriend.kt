package com.example.front_end_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.front_end_android.databinding.ActivityAddFriendBinding
import com.example.front_end_android.dataclass.AddFriendRequest
import com.example.front_end_android.dataclass.AddFriendResponse
import com.example.front_end_android.dataclass.ErrorResponse
import com.example.front_end_android.dataclass.LoginResponse
import com.example.front_end_android.util.AuthInterceptor
import com.google.gson.Gson
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

        binding.friendRequestTxt.setOnClickListener {
            val intent = Intent(this, Friend_Request::class.java)
            startActivity(intent)
        }

        binding.sendFriendRequestBtn.setOnClickListener {

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

            call.enqueue(object : Callback<AddFriendResponse> {
                override fun onResponse(call: Call<AddFriendResponse>, response: Response<AddFriendResponse>) {
                    val jsonResponse = response.body()
                    val message = jsonResponse?.message

                    if (response.isSuccessful) {
                        Log.d("YMC", "onResponse 성공: $jsonResponse $response")
                        Log.d("YMC", "message: $message")

                    } else {
                        Log.d("YMC", "onResponse 실패")//*
                        Log.d("YMC", "onResponse 실패: $jsonResponse $response")
                        Log.d("YMC", "message: $message")
                        val errorBody = response.errorBody()
                        if (errorBody != null) {
                            val errorJson = errorBody.string()
                            Log.d("YMC", "onResponse 실패 errorJson: $errorJson")

                            val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)

                            val status = errorResponse.status
                            val message = errorResponse.message
                            val data = errorResponse.data
                            val code = errorResponse.code
                            Log.d("YMC", "onResponse 실패 : $status $message $data $code")
                        } else {
                            Log.d("YMC", "onResponse 실패 : errorBody is null")
                        }
                    }

                }
                override fun onFailure(call: Call<AddFriendResponse>, t: Throwable) {
                    Log.d("YMC", "onFailure 에러: ${t.message}")//*
                }
            })

        }

    }
}