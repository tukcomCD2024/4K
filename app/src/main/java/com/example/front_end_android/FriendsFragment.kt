package com.example.front_end_android

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.front_end_android.databinding.FragmentFriendsBinding
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.105:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        val service = retrofit.create(RetrofitService::class.java);
        val call = service.myFriendsRetrofit(1)

        val friends_scrollView = binding.friendsScrollview
        val friends_scrollView_Linear = binding.frinedsScrollviewLinear

        val textLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val friendsList: List<*>? = response.body() as? List<*>
                    Log.d("YMC", "onResponse 성공: $friendsList")//*

                    if (friendsList != null) {

                        for (item in friendsList) {
                            val textView = TextView(requireContext())
                            textView.layoutParams = textLayoutParams
                            textView.text = item.toString()
                            textView.gravity = Gravity.CENTER_VERTICAL
                            textView.setBackgroundColor(Color.WHITE)
                            friends_scrollView_Linear.addView(textView)
                        }
                    }

                } else {
                    Log.d("YMC", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("YMC", "onFailure 에러: ${t.message}")//*
            }
        })

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

}