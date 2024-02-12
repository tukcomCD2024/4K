package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.front_end_android.databinding.ActivityCallingBinding
import com.example.front_end_android.models.MessageModel
import com.example.front_end_android.util.NewMessageInterface

class Calling : AppCompatActivity(), NewMessageInterface {

    private lateinit var binding:ActivityCallingBinding
    private var userName:String?=null
    private var socketRepository:SocketRepository?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

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

    private fun init(){
        userName = "seongmin"//실제로는 intent로 유저 이름을 받아야함
        socketRepository = SocketRepository(this)
        userName?.let { socketRepository?.initSocket(it) }
    }

    override fun onNewMessage(message: MessageModel) {

    }
}