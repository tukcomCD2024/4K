package com.example.front_end_android

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.front_end_android.databinding.ActivityCallingBinding
import com.example.front_end_android.models.IceCandidateModel
import com.example.front_end_android.models.MessageModel
import com.example.front_end_android.util.NewMessageInterface
import com.example.front_end_android.util.PeerConnectionObserver
import com.example.front_end_android.util.RTCAudioManager
import com.google.gson.Gson
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.SessionDescription

class Calling : AppCompatActivity(), NewMessageInterface {

    private lateinit var binding:ActivityCallingBinding
    private var userName:String? = null
    private var targetName:String = ""
    private val TAG = "CallingActivity"
    private var socketRepository:SocketRepository?=null
    private var rtcClient : RTCClient?=null
    private val gson = Gson()
    private var isMute = false
    private var isCameraPause = false
    private val rtcAudioManager by lazy { RTCAudioManager.create(this) }
    private var isSpeakerMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        binding.nicknameInit.setOnClickListener {
            binding.callingPeopleContainer.visibility = View.GONE
            binding.linearLayout.visibility = View.GONE
            binding.exitCallBackground.visibility = View.GONE
            binding.callLayout.visibility = View.VISIBLE
        }

        binding.exitCallBackground.setOnClickListener {
            rtcClient?.endCall()
            socketRepository?.closeWebSocket()
            Log.d("YMC", "endCall")//*
            finish()
        }

        binding.translateImg.setOnClickListener {
            binding.translateBackground.setBackgroundResource(R.drawable.mute2white)
        }

    }

    private fun init(){
        userName = intent.getStringExtra("username")//실제로는 intent로 유저 이름을 받아야함
        if(userName == "1"){
            targetName = "2"
        }else{
            targetName = "1"//실제로는 intent로 전화를 거는 상대방을 이름을 받아야함
        }
        socketRepository = SocketRepository(this)
        userName?.let { socketRepository?.initSocket(it) }
        rtcClient = RTCClient(application, userName!!, socketRepository!!, object : PeerConnectionObserver(){
            override fun onIceCandidate(p0: IceCandidate?) {
                super.onIceCandidate(p0)
                rtcClient?.addIceCandidate(p0)
                val candidate = hashMapOf(
                    "sdpMid" to p0?.sdpMid,
                    "sdpMLineIndex" to p0?.sdpMLineIndex,
                    "sdpCandidate" to p0?.sdp
                )

                socketRepository?.sendMessageToSocket(
                    MessageModel("ice_candidate",userName,targetName,candidate)
                )
            }

            override fun onAddStream(p0: MediaStream?) {
                super.onAddStream(p0)
                p0?.videoTracks?.get(0)?.addSink(binding.remoteView)
                Log.d(TAG, "onAddStream: $p0")
            }
        })
        rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)

        binding.apply {
            binding.buttonTest.setOnClickListener {
                socketRepository?.sendMessageToSocket(
                    MessageModel("start_call",userName,targetName,null
                    ))
                binding.buttonTest.visibility = View.GONE
            }
            switchCameraButton.setOnClickListener {
                rtcClient?.switchCamera()
            }

            micButton.setOnClickListener {
                if(isMute){
                    isMute = false
                    micButton.setImageResource(R.drawable.ic_baseline_mic_off_24)
                }else{
                    isMute = true
                    micButton.setImageResource(R.drawable.ic_baseline_mic_24)
                }
                rtcClient?.toggleAudio(isMute)
            }

            videoButton.setOnClickListener {
                if (isCameraPause){
                    isCameraPause = false
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24)
                }else{
                    isCameraPause = true
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_24)
                }
                rtcClient?.toggleCamera(isCameraPause)
            }

            audioOutputButton.setOnClickListener {
                if (isSpeakerMode){
                    isSpeakerMode = false
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_hearing_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.EARPIECE)
                }else{
                    isSpeakerMode = true
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_speaker_up_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)

                }

            }

            endCallButton.setOnClickListener {
                binding.callingPeopleContainer.visibility = View.VISIBLE
                binding.linearLayout.visibility = View.VISIBLE
                binding.exitCallBackground.visibility = View.VISIBLE
                binding.callLayout.visibility = View.GONE
            }
        }
    }

    override fun onNewMessage(message: MessageModel) {
        Log.d(TAG,"onNewMessage: $message")
        when(message.type) {
            "call_response" -> {
                Log.d("YMC", "call_response: $message")//*
                if (message.data == "user is not online") {
                    //user is not reachable
                    runOnUiThread {
                        Toast.makeText(this, "user is not reachable", Toast.LENGTH_LONG).show()
                    }
                } else {
                    //we are ready for call, we started a call
                    runOnUiThread {

                        binding.apply {
                            rtcClient?.initializeSurfaceView(localView)
                            rtcClient?.initializeSurfaceView(remoteView)
                            rtcClient?.startLocalVideo(localView)
                            rtcClient?.call(targetName)
                        }
                    }
                }
            }
            "answer_received" ->{
                Log.d("YMC", "answer_received: $message")//*
                val session = SessionDescription(
                    SessionDescription.Type.ANSWER,
                    message.data.toString()
                )
                rtcClient?.onRemoteSessionReceived(session)
                runOnUiThread {

                    binding.remoteViewLoading.visibility = View.GONE
                }
            }
            "offer_received" -> {
                runOnUiThread {
                    Log.d("YMC", "offer_received: $message")//*
                    setIncomingCallLayoutVisible()
                    binding.incomingNameTV.text = "${message.name.toString()} is calling you"
                    binding.acceptButton.setOnClickListener {
                        binding.buttonTest.visibility = View.GONE
                        setIncomingCallLayoutGone()

                        binding.apply {
                            rtcClient?.initializeSurfaceView(localView)
                            rtcClient?.initializeSurfaceView(remoteView)
                            rtcClient?.startLocalVideo(localView)
                        }
                        val session = SessionDescription(
                            SessionDescription.Type.OFFER,
                            message.data.toString()
                        )
                        rtcClient?.onRemoteSessionReceived(session)
                        rtcClient?.answer(message.name!!)
                        targetName = message.name!!
                        binding.remoteViewLoading.visibility = View.GONE
                    }

                    binding.rejectButton.setOnClickListener {
                        setIncomingCallLayoutGone()
                    }
                }
            }
            "ice_candidate"->{
                Log.d("YMC", "ice_candidate: $message")//*
                try {
                    val receivingCandidate = gson.fromJson(gson.toJson(message.data),
                        IceCandidateModel::class.java)
                    rtcClient?.addIceCandidate(IceCandidate(receivingCandidate.sdpMid,
                        Math.toIntExact(receivingCandidate.sdpMLineIndex.toLong()),receivingCandidate.sdpCandidate))
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setIncomingCallLayoutGone(){
        binding.incomingCallLayout.visibility = View.GONE
    }
    private fun setIncomingCallLayoutVisible() {
        binding.incomingCallLayout.visibility = View.VISIBLE
    }
}