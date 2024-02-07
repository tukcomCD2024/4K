package com.example.front_end_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.front_end_android.databinding.ActivityLoginBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
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

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.105:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        val service = retrofit.create(RetrofitService::class.java);

        binding.loginBtn.setOnClickListener {

            var loginemail = binding.emailEdt.text.toString()
            var loginpassword = binding.passwordEdt.text.toString()

            val call = service.loginRetrofit(loginemail, loginpassword)

            call.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        val jsonResponse = response.body()
                        Log.d("YMC", "onResponse 성공: $jsonResponse")//*

                        if(jsonResponse=="success"){
                            val intent = Intent(this@Login, NaviActivity::class.java)
                            startActivity(intent)
                        }

                    } else {
                        Log.d("YMC", "onResponse 실패")//*
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Log.d("YMC", "onFailure 에러: ${t.message}")//*
                }
            })

        }

    }
}