package com.example.front_end_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.front_end_android.databinding.ActivityCallingBinding
import com.example.front_end_android.models.MessageModel
import com.example.front_end_android.util.NewMessageInterface
import com.example.front_end_android.util.PeerConnectionObserver
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection

class Calling : AppCompatActivity(), NewMessageInterface {

    private lateinit var binding:ActivityCallingBinding
    private var userName:String? = null
    private var targetName:String? = null
    private val TAG = "CallActivity"
    private var socketRepository:SocketRepository?=null
    private var rtcClient : RTCClient?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        //테스트 버튼
        binding.buttonTest.setOnClickListener {
            /*
            binding.callingPeople1.visibility = View.VISIBLE
            binding.callingPeople2.visibility = View.VISIBLE
            binding.nickname1.visibility = View.VISIBLE
            binding.nickname2.visibility = View.VISIBLE
            binding.callingPeopleInit.visibility = View.GONE
            binding.nicknameInit.visibility = View.GONE*/
        }

        binding.nicknameInit.setOnClickListener {
            binding.callingPeopleContainer.visibility = View.GONE
            binding.linearLayout.visibility = View.GONE
            binding.exitCallBackground.visibility = View.GONE
            binding.callLayout.visibility = View.VISIBLE
        }

        binding.exitCallBackground.setOnClickListener {
            finish()
        }

    }

    private fun init(){
        userName = "seongmin"//실제로는 intent로 유저 이름을 받아야함
        targetName = "testfriend"//실제로는 intent로 전화를 거는 상대방을 이름을 받아야함
        socketRepository = SocketRepository(this)
        userName?.let { socketRepository?.initSocket(it) }
        rtcClient = RTCClient(application, userName!!, socketRepository!!, object : PeerConnectionObserver(){
            override fun onIceCandidate(p0: IceCandidate?) {
                super.onIceCandidate(p0)
            }

            override fun onAddStream(p0: MediaStream?) {
                super.onAddStream(p0)
            }
        })

        binding.apply {
            socketRepository?.sendMessageToSocket(
                MessageModel("start_call",userName,targetName,null
                ))
        }
    }

    override fun onNewMessage(message: MessageModel) {

    }
}