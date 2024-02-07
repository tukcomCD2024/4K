package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.front_end_android.databinding.ActivityCallingBinding

class Calling : AppCompatActivity() {

    private lateinit var binding:ActivityCallingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallingBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_calling)

        binding.exitCallBackground.setOnClickListener {
            finish()
        }

    }
}