package com.example.front_end_android

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.front_end_android.databinding.FragmentFriendsBinding
import com.google.gson.GsonBuilder
import com.permissionx.guolindev.PermissionX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.Manifest
import com.example.front_end_android.dataclass.CallFCMRequest
import com.example.front_end_android.dataclass.CallFCMResponse
import com.example.front_end_android.dataclass.ErrorResponse
import com.example.front_end_android.dataclass.FindMyFriendsRequest
import com.example.front_end_android.dataclass.FindMyFriendsResponse
import com.example.front_end_android.dataclass.FindResponse
import com.example.front_end_android.dataclass.FriendRequestListRequest
import com.example.front_end_android.util.AuthInterceptor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.converter.scalars.ScalarsConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding:FragmentFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentFriendsBinding.inflate(inflater)

        binding.addFriendImg.setOnClickListener {
            val intent = Intent(requireActivity(), AddFriend::class.java)
            startActivity(intent)

        }
        binding.addFriendTxt.setOnClickListener {
            val intent = Intent(requireActivity(), AddFriend::class.java)
            startActivity(intent)

        }

        binding.friendRequestTxt.setOnClickListener {
            val intent = Intent(requireActivity(), Friend_Request::class.java)
            startActivity(intent)
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
            .build();
        val service = retrofit.create(RetrofitService::class.java)
        val findMyFriendRequest = FindMyFriendsRequest(userEmail)
        val call = service.findMyFriendsRetrofit(findMyFriendRequest)

        //val friends_scrollView = binding.friendsScrollview
        /*val textLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )*/

        val friends_scrollView_Linear = binding.frinedsScrollviewLinear

        call.enqueue(object : Callback<List<FindMyFriendsResponse>> {
            override fun onResponse(call: Call<List<FindMyFriendsResponse>>, response: Response<List<FindMyFriendsResponse>>) {
                if (response.isSuccessful) {
                    val friendsList: List<*>? = response.body()
                    Log.d("YMC", "onResponse 성공: $friendsList")//*

                    val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

                    for (letter in alphabet) {
                        val groupLayout = LinearLayout(requireContext())
                        groupLayout.orientation = LinearLayout.VERTICAL

                        val headerTextView = TextView(requireContext())
                        headerTextView.text = letter.toString()
                        headerTextView.textSize = 20f

                        val header_params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        header_params.topMargin = 60 // 위쪽 간격 설정
                        headerTextView.layoutParams = header_params
                        groupLayout.addView(headerTextView)

                        val header_lineView = View(requireContext())
                        val header_line_params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                        )
                        header_line_params.topMargin = 30
                        header_lineView.layoutParams = header_line_params
                        header_lineView.setBackgroundColor(Color.GRAY)
                        groupLayout.addView(header_lineView)

                        if (friendsList != null) {
                            val jsonResponse = response.body()
                            if (jsonResponse != null) {
                                for (request in jsonResponse) {
                                    val email = request.email
                                    if (email.startsWith(letter, ignoreCase = true)) {
                                        val emailTextView = TextView(requireContext())
                                        emailTextView.text = email
                                        emailTextView.setTextColor(Color.BLACK)
                                        emailTextView.textSize = 20f

                                        val email_params = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        )
                                        email_params.topMargin = 30 // 위쪽 간격 설정
                                        emailTextView.layoutParams = email_params
                                        groupLayout.addView(emailTextView)

                                        val email_lineView = View(requireContext())
                                        val email_line_params = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1
                                        )
                                        email_line_params.topMargin = 30
                                        email_lineView.layoutParams = email_line_params
                                        email_lineView.setBackgroundColor(Color.GRAY)
                                        groupLayout.addView(email_lineView)

                                        val containerLayout = RelativeLayout(requireContext())
                                        containerLayout.setBackgroundColor(Color.parseColor("#E2E8F0"))

                                        val container_layoutParams = RelativeLayout.LayoutParams(
                                            RelativeLayout.LayoutParams.MATCH_PARENT, // 너비를 매칭하도록 설정
                                            140
                                        )
                                        containerLayout.layoutParams = container_layoutParams

                                        containerLayout.visibility = View.GONE // 초기에는 숨김 상태로 설정
                                        groupLayout.addView(containerLayout)

                                        emailTextView.setOnClickListener {
                                            // 클릭 시 컨테이너의 가시성을 토글
                                            containerLayout.visibility = if (containerLayout.visibility == View.VISIBLE) {
                                                View.GONE
                                            } else {
                                                View.VISIBLE
                                            }
                                        }

                                        val container_lineView = View(requireContext())
                                        val container_line_params = RelativeLayout.LayoutParams(
                                            RelativeLayout.LayoutParams.MATCH_PARENT,
                                            1
                                        )

                                        container_line_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM) // 콘테이너 아래에 추가되도록 설정
                                        container_lineView.layoutParams = container_line_params
                                        container_lineView.setBackgroundColor(Color.GRAY)
                                        containerLayout.addView(container_lineView)

                                        val imageView = ImageView(requireContext())
                                        imageView.setImageResource(R.drawable.baseline_call_24) // 이미지 리소스 설정
                                        imageView.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_background) // 배경 설정

                                        val image_params = RelativeLayout.LayoutParams(
                                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                                            RelativeLayout.LayoutParams.WRAP_CONTENT
                                        )
                                        image_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT) // 오른쪽 끝에 배치되도록 설정
                                        image_params.addRule(RelativeLayout.CENTER_VERTICAL)// 위아래 중앙 배치
                                        imageView.layoutParams = image_params
                                        imageView.setOnClickListener {
                                            MyApplication.preferences.setString("targetName",email)
                                            val callingState = "sender"
                                            MyApplication.preferences.setString("callingState",callingState)
                                            sendCallFCM(email)

                                            PermissionX.init(requireActivity())
                                                .permissions(
                                                    Manifest.permission.RECORD_AUDIO,
                                                    Manifest.permission.CAMERA
                                                ).request{ allGranted, _ ,_ ->
                                                    if (allGranted){
                                                        val intent = Intent(requireActivity(), Calling::class.java)
                                                        startActivity(intent)
                                                    } else {
                                                        Toast.makeText(requireContext(),"you should accept all permissions",Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                        }
                                        containerLayout.addView(imageView)

                                    }
                                }
                            }
                        }

                        friends_scrollView_Linear.addView(groupLayout)
                    }

                } else {
                    Log.d("YMC", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<List<FindMyFriendsResponse>>, t: Throwable) {
                Log.d("YMC", "onFailure 에러: ${t.message}")//*
            }
        })

        //테스트 버튼
        binding.callingtestbtn.setOnClickListener {
            PermissionX.init(requireActivity())
                .permissions(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
                ).request{ allGranted, _ ,_ ->
                    if (allGranted){
                        startActivity(
                            Intent(requireActivity(),Calling::class.java)
                                .putExtra("username","asdf@naver.com")
                        )
                    } else {
                        Toast.makeText(requireContext(),"you should accept all permissions",Toast.LENGTH_LONG).show()
                    }
                }
        }
        binding.callingtestbtn2.setOnClickListener {
            PermissionX.init(requireActivity())
                .permissions(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
                ).request{ allGranted, _ ,_ ->
                    if (allGranted){
                        startActivity(
                            Intent(requireActivity(),Calling::class.java)
                                .putExtra("username","asdfg@naver.com")
                        )
                    } else {
                        Toast.makeText(requireContext(),"you should accept all permissions",Toast.LENGTH_LONG).show()
                    }
                }
        }

        return binding.root
        //return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FriendsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    private fun sendCallFCM(target: String){
        val accessToken = MyApplication.preferences.getString("AccessToken",".")
        val refreshToken = MyApplication.preferences.getString("RefreshToken",".")
        val userEmail = MyApplication.preferences.getString("email",".")
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

        val callFCMRequest = CallFCMRequest(target,"Ringo", userEmail)
        val callFCM = service.callFCMRetrofit(callFCMRequest)

        callFCM.enqueue(object : Callback<CallFCMResponse> {
            override fun onResponse(call: Call<CallFCMResponse>, response: Response<CallFCMResponse>) {
                val jsonResponse = response.body()
                val message = jsonResponse?.message
                //val status = jsonResponse?.status
                //val data = jsonResponse?.data

                if (response.isSuccessful) {
                    Log.d("YMC", "onResponse 성공: $jsonResponse $response")
                    Log.d("YMC", "message: $message")
                    //Log.d("YMC", "data: $data")
                    //Log.d("YMC", "status: $status")

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
            override fun onFailure(call: Call<CallFCMResponse>, t: Throwable) {
                Log.d("YMC", "onFailure 에러: ${t.message}")//*
            }
        })

    }

}