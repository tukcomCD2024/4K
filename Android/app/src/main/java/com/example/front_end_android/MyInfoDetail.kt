package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.front_end_android.databinding.ActivityMyInfoDetailBinding
import com.example.front_end_android.dataclass.ErrorResponse
import com.example.front_end_android.dataclass.FindRequest
import com.example.front_end_android.dataclass.FindResponse
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

class MyInfoDetail : AppCompatActivity() {
    private lateinit var binding:ActivityMyInfoDetailBinding
    private lateinit var selectedLanguage:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.changeBtn.setOnClickListener {
            binding.changeBtn.isEnabled = false
            binding.changeBtn.visibility = View.INVISIBLE
            binding.checkBtn.isEnabled = true
            binding.checkBtn.visibility = View.VISIBLE
        }

        val spinner: Spinner = findViewById(R.id.language_spinner)
        ArrayAdapter.createFromResource(
            this@MyInfoDetail,
            R.array.language_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.isEnabled = false
        spinner.isClickable = false
        spinner.isFocusable = false

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selected= parent.getItemAtPosition(position).toString()
                if(selected == "English")
                    selectedLanguage = "en"
                else if(selected == "Korean")
                    selectedLanguage = "ko"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        val accessToken = MyApplication.preferences.getString("AccessToken",".")
        val refreshToken = MyApplication.preferences.getString("RefreshToken",".")
        val userEmail = MyApplication.preferences.getString("email",".")
        Log.d("YMC", "access token : $accessToken")
        Log.d("YMC", "Refresh token : $refreshToken")
        Log.d("YMC", "email : $userEmail")

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

        val findRequest = FindRequest(userEmail)
        val call_find = service.findRetrofit(findRequest)

        call_find.enqueue(object : Callback<FindResponse> {
            override fun onResponse(call: Call<FindResponse>, response: Response<FindResponse>) {
                val jsonResponse = response.body()
                val message = jsonResponse?.message
                val status = jsonResponse?.status
                val data = jsonResponse?.data

                if (response.isSuccessful) {
                    Log.d("YMC", "onResponse 성공: $jsonResponse $response")
                    Log.d("YMC", "message: $message")
                    Log.d("YMC", "data: $data")
                    Log.d("YMC", "status: $status")
                    val name = data?.name
                    if (data != null) {
                        binding.scrollViewTextFieldEmailEdt.setText(data.email)
                        binding.scrollViewTextFieldUserNameEdt.setText(data.name)
                        if(data.language == "en"){
                            spinner.setSelection(0)
                        }else if(data.language == "ko"){
                            spinner.setSelection(1)
                        }
                    }

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
            override fun onFailure(call: Call<FindResponse>, t: Throwable) {
                Log.d("YMC", "onFailure 에러: ${t.message}")//*
            }
        })

    }
}