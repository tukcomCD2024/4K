package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.front_end_android.databinding.ActivityFriendRequestBinding

class Friend_Request : AppCompatActivity() {

    private lateinit var binding: ActivityFriendRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.testBtn.setOnClickListener {



        }

    }
}