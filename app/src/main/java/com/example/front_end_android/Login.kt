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
            .baseUrl("http://192.168.219.108:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        val service = retrofit.create(RetrofitService::class.java);

        binding.loginBtn.setOnClickListener {

            var loginemail = binding.emailEdt.text.toString()
            var loginpassword = binding.passwordEdt.text.toString()

            val jsonObject = JsonObject()
            jsonObject.addProperty("loginEmail", "arrisung@naver.com")
            jsonObject.addProperty("password", "1234")
            //jsonObject.addProperty("loginEmail", loginemail)
            //jsonObject.addProperty("password", loginpassword)
            val call = service.loginRetrofit(jsonObject)

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val jsonResponse = response.body()
                        Log.d("YMC", "onResponse 성공: $jsonResponse")

                        val intent = Intent(this@Login, NaviActivity::class.java)
                        startActivity(intent)

                    } else {
                        Log.d("YMC", "onResponse 실패")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("YMC", "onFailure 에러: ${t.message}")
                }
            })

        }

    }
}