package com.example.front_end_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.front_end_android.databinding.ActivityAddFriendBinding

class AddFriend : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backFriendsImg.setOnClickListener {

            finish()

        }
        binding.backFriendsTxt.setOnClickListener {

            finish()

        }

    }
}