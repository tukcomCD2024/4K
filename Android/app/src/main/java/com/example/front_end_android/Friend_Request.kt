package com.example.front_end_android

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.front_end_android.databinding.ActivityFriendRequestBinding
import com.example.front_end_android.dataclass.AddFriendRequest
import com.example.front_end_android.dataclass.AddFriendResponse
import com.example.front_end_android.dataclass.FriendRequestListRequest
import com.example.front_end_android.dataclass.FriendRequestListResponse
import com.example.front_end_android.util.AuthInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Friend_Request : AppCompatActivity() {

    private lateinit var binding: ActivityFriendRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accessToken = MyApplication.preferences.getString("AccessToken",".")
        val senderEmail = MyApplication.preferences.getString("email",".")

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

        val friendRequestListRequest = FriendRequestListRequest(senderEmail)
        val call = service.friendRequestListRetrofit(friendRequestListRequest)

        val friend_Request_ScrollView_Linear = binding.frinedsScrollviewLinear

        call.enqueue(object : Callback<List<FriendRequestListResponse>> {
            override fun onResponse(call: Call<List<FriendRequestListResponse>>, response: Response<List<FriendRequestListResponse>>) {
                val jsonResponse = response.body()

                if (response.isSuccessful) {

                    Log.d("YMC", "onResponse 성공: $response")
                    Log.d("YMC", "jsonResponse: $jsonResponse")

                    if (jsonResponse != null) {
                        val friendRequestScrollView = ScrollView(this@Friend_Request)
                        val friendRequestLinearLayout = LinearLayout(this@Friend_Request)
                        friendRequestLinearLayout.orientation = LinearLayout.VERTICAL

                        for (request in jsonResponse) {
                            val name = request.name
                            val email = request.email

                            // 수평으로 배치되는 레이아웃
                            val horizontalLayout = LinearLayout(this@Friend_Request)
                            horizontalLayout.orientation = LinearLayout.HORIZONTAL

                            // 이메일과 이름 텍스트뷰
                            val emailTextView = TextView(this@Friend_Request)
                            val emailNameText = "$name ($email)"
                            emailTextView.text = emailNameText
                            emailTextView.setTextColor(Color.BLACK)
                            emailTextView.textSize = 14f
                            val emailTextViewParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            emailTextViewParams.weight = 1f // 폭을 균등하게 설정
                            emailTextViewParams.gravity = Gravity.CENTER_VERTICAL
                            emailTextView.layoutParams = emailTextViewParams

                            // 확인 이미지뷰
                            val checkImageView = ImageView(this@Friend_Request)
                            checkImageView.setImageResource(R.drawable.baseline_check_24)
                            val checkImageViewParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            checkImageView.layoutParams = checkImageViewParams

                            // 거부 이미지뷰
                            val clearImageView = ImageView(this@Friend_Request)
                            clearImageView.setImageResource(R.drawable.baseline_clear_24)
                            val clearImageViewParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            clearImageView.layoutParams = clearImageViewParams

                            // 수평 레이아웃에 추가
                            horizontalLayout.addView(emailTextView)
                            horizontalLayout.addView(checkImageView)
                            horizontalLayout.addView(clearImageView)

                            // 수평 레이아웃을 수직 레이아웃에 추가
                            friendRequestLinearLayout.addView(horizontalLayout)

                            // 수평선 추가
                            val emailLineView = View(this@Friend_Request)
                            val emailLineParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                1
                            )
                            emailLineParams.topMargin = 25
                            emailLineView.layoutParams = emailLineParams
                            emailLineView.setBackgroundColor(Color.GRAY)
                            friendRequestLinearLayout.addView(emailLineView)

                            Log.d("YMC", "Name: $name, Email: $email")
                        }

                        friendRequestScrollView.addView(friendRequestLinearLayout)
                        friend_Request_ScrollView_Linear.addView(friendRequestScrollView)
                    }
                } else {

                    Log.d("YMC", "onResponse 실패")//*
                    Log.d("YMC", "onResponse 성공: $jsonResponse $response")
                    Log.d("YMC", "jsonResponse: $jsonResponse")
                }

            }
            override fun onFailure(call: Call<List<FriendRequestListResponse>>, t: Throwable) {
                Log.d("YMC", "onFailure 에러: ${t.message}")//*
            }
        })

    }
}