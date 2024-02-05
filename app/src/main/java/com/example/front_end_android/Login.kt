package com.example.front_end_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.front_end_android.databinding.ActivityLoginBinding
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

        binding.loginBtn.setOnClickListener {

            var loginemail = binding.emailEdt.text.toString()
            var loginpassword = binding.passwordEdt.text.toString()

            if(loginemail == null || loginpassword == null){

                //아이디 비밀번호 입력 안했을때
                Toast.makeText(this@Login, "아디비번입력", Toast.LENGTH_SHORT).show()

            }else{

                // Retrofit 객체 생성
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://172.30.1.28:8080/") // 서버의 기본 URL을 지정
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                // RetrofitService 인터페이스 구현체 생성
                val service = retrofit.create(RetrofitService::class.java)

                // POST 요청 보내기
                val callLogin = service.loginRetrofit(loginemail, loginpassword)

                // 비동기적으로 요청 수행
                callLogin.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@Login, "성공", Toast.LENGTH_SHORT).show()
                            // 성공적으로 응답을 받았을 때 처리
                            val result = response.body()
                            val intent = Intent(this@Login, NaviActivity::class.java)
                            startActivity(intent)
                            //println("Response: $result")
                        } else {
                            // 에러 응답 처리
                            // response.errorBody() 등을 사용하여 에러 상세 정보를 확인
                            //println("Error: ${response.code()} - ${response.message()}")
                            Toast.makeText(this@Login, "1번에러", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Toast.makeText(this@Login, "${t.message}", Toast.LENGTH_SHORT).show()
                        // 요청 실패 처리
                        // 예외 상황을 처리
                        Log.e("Faillogin", "Request failed: ${t.message}", t)
                    }
                })
            }

        }

    }
}