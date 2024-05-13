package com.example.front_end_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.front_end_android.databinding.ActivityLoginBinding
import com.example.front_end_android.dataclass.LoginRequest
import com.example.front_end_android.dataclass.LoginResponse
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.forgotPass.setOnClickListener {
            val intent = Intent(this, FindPassword::class.java)
            startActivity(intent)
        }

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://4kringo.shop:8080/")
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val service = retrofit.create(RetrofitService::class.java)

        binding.loginBtn.setOnClickListener {

            var loginemail = binding.emailEdt.text.toString()
            var loginpassword = binding.passwordEdt.text.toString()
            val loginRequest = LoginRequest(loginemail, loginpassword)
            val call = service.loginRetrofit(loginRequest)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val jsonResponse = response.body()
                        Log.d("YMC", "onResponse 성공: $jsonResponse $response")
                        val message = jsonResponse?.message

                        val headers = response.headers()
                        val access_token = headers.get("Access-Token")
                        val refresh_token = headers.get("Refresh-Token")
                        MyApplication.preferences.setString("AccessToken",access_token.toString())
                        MyApplication.preferences.setString("RefreshToken",refresh_token.toString())
                        MyApplication.preferences.setString("email",loginemail)
                        Log.d("YMC", "access token : $access_token")
                        Log.d("YMC", "Refresh token : $refresh_token")
                        Log.d("YMC", "message: $message")

                        if (message == "로그인 성공") {
                            val intent = Intent(this@Login, NaviActivity::class.java)
                            startActivity(intent)
                        }

                    } else {
                        Log.d("YMC", "onResponse 실패")//*
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("YMC", "onFailure 에러: ${t.message}")//*
                }
            })

        }

    }
}