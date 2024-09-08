package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.front_end_android.databinding.ActivityTranslationSubtitlesBinding

class TranslationSubtitles : AppCompatActivity() {
    lateinit var binding: ActivityTranslationSubtitlesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTranslationSubtitlesBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}