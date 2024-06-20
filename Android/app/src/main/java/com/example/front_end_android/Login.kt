package com.example.front_end_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.front_end_android.databinding.ActivityLoginBinding
import com.example.front_end_android.dataclass.ErrorResponse
import com.example.front_end_android.dataclass.LoginRequest
import com.example.front_end_android.dataclass.LoginResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var token:String
    private lateinit var loginState:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(MyApplication.preferences.getString("LoginState","AutoNotChecked") == "AutoChecked"){
            Log.d("YMC", "dddd")
            val intent = Intent(this@Login, NaviActivity::class.java)
            startActivity(intent)
        }

        /*binding.autoLoginCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                loginState = "AutoChecked"
                MyApplication.preferences.setString("LoginState",loginState)
                Log.d("YMC", loginState)
            }else{
                loginState = "AutoNotChecked"
                MyApplication.preferences.setString("LoginState",loginState)
                Log.d("YMC", loginState)
            }
        }*/

        binding.goSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.forgotPass.setOnClickListener {
            val intent = Intent(this, FindPassword::class.java)
            startActivity(intent)
        }

        // 토큰 가져오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // FCM 등록 토큰 가져오기
            token = task.result

            val msg = "FCM Registration token: " + token
            Log.d("YMC", msg)
        })

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
            val loginRequest = LoginRequest(loginemail, loginpassword, token)
            val call = service.loginRetrofit(loginRequest)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val jsonResponse = response.body()
                        Log.d("YMC", "onResponse 성공 jsonResponse: $jsonResponse")
                        Log.d("YMC", "onResponse 성공 response: $response")

                        val status = jsonResponse?.status

                        if (status == "success") {
                            val access_token = jsonResponse.data?.accessToken
                            val refresh_token = jsonResponse.data?.refreshToken
                            var language = " "
                            if(jsonResponse.data?.language == "ko"){
                                language = "ko-KR"
                            }else if(jsonResponse.data?.language == "en"){
                                language = "en-US"
                            }else if(jsonResponse.data?.language == "zh-CN"){
                                language = "zh-CN"
                            }else if(jsonResponse.data?.language == "de"){
                                language = "de-DE"
                            }else if(jsonResponse.data?.language == "es"){
                                language = "es-ES"
                            }else if(jsonResponse.data?.language == "fr"){
                                language = "fr-FR"
                            }
                            MyApplication.preferences.setString("AccessToken",access_token.toString())
                            MyApplication.preferences.setString("RefreshToken",refresh_token.toString())
                            MyApplication.preferences.setString("SttLanguage", language)
                            MyApplication.preferences.setString("email",loginemail)
                            Log.d("YMC", "access token : $access_token")
                            Log.d("YMC", "Refresh token : $refresh_token")

                            if(binding.autoLoginCb.isChecked){
                                loginState = "AutoChecked"
                                MyApplication.preferences.setString("LoginState",loginState)
                                Log.d("YMC", loginState)
                            }else{
                                loginState = "AutoNotChecked"
                                MyApplication.preferences.setString("LoginState",loginState)
                                Log.d("YMC", loginState)
                            }

                            val intent = Intent(this@Login, NaviActivity::class.java)
                            startActivity(intent)
                        }

                    } else {
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
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("YMC", "onFailure 에러: ${t.message}")//*
                }
            })

        }

        val state = intent.getStringExtra("State")

        /*if(state == "calling"){
            val intent = Intent(this, Calling::class.java)
            startActivity(intent)
        }*/

    }
}