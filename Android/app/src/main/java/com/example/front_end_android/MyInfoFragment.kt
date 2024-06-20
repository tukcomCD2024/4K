package com.example.front_end_android

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.front_end_android.databinding.FragmentFriendsBinding
import com.example.front_end_android.databinding.FragmentMyInfoBinding
import com.example.front_end_android.dataclass.AddFriendResponse
import com.example.front_end_android.dataclass.DeleteRequest
import com.example.front_end_android.dataclass.DeleteResponse
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentMyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMyInfoBinding.inflate(inflater)
        // Inflate the layout for this fragment

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
                    binding.nameTxt.text = name
                    binding.emailTxt.text = userEmail

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

        binding.settingsAc.setOnClickListener {
            val intent = Intent(requireActivity(), MyInfoDetail::class.java)
            startActivity(intent)
        }

        binding.signOutBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("sign out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("check") { dialog, which ->
                    val loginState = "AutoNotChecked"
                    MyApplication.preferences.setString("LoginState",loginState)
                    val intent = Intent(requireActivity(), Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("cancel", null)
                .show()
        }

        binding.withdrawalBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("withdrawal")
                .setMessage("Are you sure you want to withdraw?")
                .setPositiveButton("check") { dialog, which ->
                    val inflater: LayoutInflater = layoutInflater
                    val dialogView = inflater.inflate(R.layout.dialog_password_input, null)
                    val passwordInput = dialogView.findViewById<EditText>(R.id.password_input)

                    AlertDialog.Builder(requireContext())
                        .setTitle("Enter Password")
                        .setView(dialogView)
                        .setPositiveButton("check") { dialog, which ->
                            val password = passwordInput.text.toString()
                            if (password.isNotEmpty()) {
                                val deleteRequest = DeleteRequest(userEmail,password)
                                val call_Delete = service.deleteRetrofit(deleteRequest)
                                call_Delete.enqueue(object : Callback<DeleteResponse> {
                                    override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                                        val jsonResponse = response.body()
                                        val message = jsonResponse?.message
                                        val status = jsonResponse?.status
                                        val data = jsonResponse?.data

                                        if (response.isSuccessful) {
                                            Log.d("YMC", "onResponse 성공: $jsonResponse $response")
                                            Log.d("YMC", "message: $message")
                                            Log.d("YMC", "data: $data")
                                            Log.d("YMC", "status: $status")

                                            val intent = Intent(requireActivity(), Login::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)

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
                                    override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                                        Log.d("YMC", "onFailure 에러: ${t.message}")//*
                                    }
                                })
                            } else {
                                Toast.makeText(requireContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("cancel", null)
                        .show()
                }
                .setNegativeButton("cancel", null)
                .show()
        }

        return binding.root
        //return inflater.inflate(R.layout.fragment_my_info, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}