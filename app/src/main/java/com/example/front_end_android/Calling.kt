package com.example.front_end_android

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
import com.google.gson.Gson
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.SessionDescription

class Calling : AppCompatActivity(), NewMessageInterface {

    private lateinit var binding:ActivityCallingBinding
    private var userName:String? = null
    private var targetName:String = ""
    private val TAG = "CallActivity"
    private var socketRepository:SocketRepository?=null
    private var rtcClient : RTCClient?=null
    private val gson = Gson()

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
            finish()
        }

    }

    private fun init(){
        userName = intent.getStringExtra("username")//실제로는 intent로 유저 이름을 받아야함
        targetName = "testfriend"//실제로는 intent로 전화를 거는 상대방을 이름을 받아야함
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

        binding.apply {
            binding.buttonTest.setOnClickListener {
                socketRepository?.sendMessageToSocket(
                    MessageModel("start_call",userName,targetName,null
                    ))
            }
        }
    }

    override fun onNewMessage(message: MessageModel) {
        Log.d(TAG,"onNewMessage: $message")
        when(message.type) {
            "call_response" -> {
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
                    setIncomingCallLayoutVisible()
                    binding.incomingNameTV.text = "${message.name.toString()} is calling you"
                    binding.acceptButton.setOnClickListener {
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
                    }
                    binding.rejectButton.setOnClickListener {
                        setIncomingCallLayoutGone()
                    }
                    binding.remoteViewLoading.visibility = View.GONE
                }
            }
            "ice_candidate"->{
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