package com.example.front_end_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.front_end_android.databinding.FragmentFriendsBinding
import com.example.front_end_android.databinding.FragmentMyInfoBinding
import com.example.front_end_android.util.AuthInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
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