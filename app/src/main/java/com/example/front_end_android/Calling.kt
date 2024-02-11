package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.front_end_android.databinding.ActivityCallingBinding

class Calling : AppCompatActivity() {

    private lateinit var binding:ActivityCallingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallingBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_calling)

        binding.buttonTest.setOnClickListener {
            binding.callingPeople1.visibility = View.VISIBLE
            binding.callingPeople2.visibility = View.VISIBLE
            binding.nickname1.visibility = View.VISIBLE
            binding.nickname2.visibility = View.VISIBLE
            binding.callingPeopleInit.visibility = View.GONE
            binding.nicknameInit.visibility = View.GONE
        }

        binding.exitCallBackground.setOnClickListener {
            finish()
        }

    }
}