package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.front_end_android.databinding.ActivitySignUpBinding
import com.example.front_end_android.dataclass.ErrorResponse
import com.example.front_end_android.dataclass.SignUpErrorResponse
import com.example.front_end_android.dataclass.SignUpRequest
import com.example.front_end_android.dataclass.SignUpResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class SignUp : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var selectedLanguage:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goSignIn.setOnClickListener{
            finish()
        }

        val spinner: Spinner = findViewById(R.id.language_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.language_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

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
                val signUpRequest = SignUpRequest(signUpEmailData,signUpPasswordData,signUpUserNameData,selectedLanguage)
                val call = signUpService.signUpRetrofit(signUpRequest)

                call.enqueue(object : Callback<SignUpResponse>{
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                        if (response.isSuccessful){
                            val jsonResponse = response.body()
                            Log.d("YMC", "onResponse 성공 jsonResponse: $jsonResponse")
                            Log.d("YMC", "onResponse 성공 response: $response")
                            Log.d("YMC", "onResponse 성공 language: $selectedLanguage")
                            finish()
                        }
                        else{
                            val errorBody = response.errorBody()
                            if (errorBody != null) {
                                val errorJson = errorBody.string()
                                Log.d("YMC", "onResponse 실패 errorJson: $errorJson")

                                val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)

                                val status = errorResponse.status
                                val message = errorResponse.message
                                val data = errorResponse.data
                                Log.d("YMC", "onResponse 실패 : $status $message $data")
                            } else {
                                Log.d("YMC", "onResponse 실패 : errorBody is null")
                            }
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