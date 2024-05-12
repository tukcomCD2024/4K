package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.front_end_android.databinding.ActivityFindPasswordBinding

class FindPassword : AppCompatActivity() {

    private lateinit var binding:ActivityFindPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}