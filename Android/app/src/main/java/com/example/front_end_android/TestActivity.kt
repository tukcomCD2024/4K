package com.example.front_end_android

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.front_end_android.databinding.ActivityTestBinding


class TestActivity : AppCompatActivity() {

    private lateinit var binding:ActivityTestBinding
    private val PERMISSION_REQUEST_CODE = 5000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun requestPermission(view: View){
        
    }

}