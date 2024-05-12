package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.front_end_android.databinding.ActivitySignUpBinding
import com.example.front_end_android.dataclass.SignUpRequest
import com.example.front_end_android.dataclass.SignUpResponse
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class SignUp : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goSignIn.setOnClickListener{
            finish()
        }

        binding.createAccountBtn.setOnClickListener {
            if (binding.scrollViewTextFieldEmailEdt.text.toString().isEmpty() and
                binding.scrollViewTextFieldConfirmPasswordEdt.text.toString().isEmpty() and
                binding.scrollViewTextFieldNicknameEdt.text.toString().isEmpty() and
                binding.scrollViewTextFieldPasswordEdt.text.toString().isEmpty() and
                binding.scrollViewTextFieldUserNameEdt.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter all information!!!", Toast.LENGTH_SHORT).show()
            }else{
                val gson = GsonBuilder().setLenient().create()
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://4kringo.shop:8080/")
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

                val signUpService = retrofit.create(RetrofitService::class.java)

                val signUpEmailData = binding.scrollViewTextFieldEmailEdt.text.toString()
                val signUpPasswordData = binding.scrollViewTextFieldPasswordEdt.text.toString()
                val signUpUserNameData = binding.scrollViewTextFieldUserNameEdt.text.toString()
                val signUpRequest = SignUpRequest(signUpEmailData,signUpPasswordData,signUpUserNameData)
                val call = signUpService.signUpRetrofit(signUpRequest)

                call.enqueue(object : Callback<SignUpResponse>{
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                        if (response.isSuccessful){
                            val jsonResponse = response.body()
                            Log.d("YMC", "onResponse 성공: $jsonResponse\n $response")
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Log.d("YMC", "onFailure 에러: ${t.message}")//*
                    }

                })

            }
        }

    }
}