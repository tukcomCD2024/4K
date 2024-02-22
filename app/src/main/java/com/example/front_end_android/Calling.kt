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
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Calling : AppCompatActivity(), NewMessageInterface {

    private lateinit var binding:ActivityCallingBinding
    private var userName:String? = null
    private var targetName:String = ""
    private val TAG = "CallingActivity"
    private var socketRepository:SocketRepository?=null
    private var rtcClient : RTCClient?=null
    private val gson = Gson()
    private var isMute = true
    private var isCameraPause = false
    private val rtcAudioManager by lazy { RTCAudioManager.create(this) }
    private var isSpeakerMode = true
    private var isTranslateMode = false

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionIntent: Intent

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
            isTranslateMode = false
            stopListening()
            Log.d("YMC", "endCall")//*
            finish()
        }


        // RecognizerIntent 생성
        recognitionIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)    // 여분의 키
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")         // 언어 설정
        // 새 SpeechRecognizer 를 만드는 팩토리 메서드
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@Calling)
        speechRecognizer.setRecognitionListener(recognitionListener)    // 리스너 설정

        binding.translateImg.setOnClickListener {
            if(isTranslateMode == false){
                isTranslateMode = true
                binding.translateBackground.setBackgroundResource(R.drawable.mute2white)
                binding.translateImg.setImageResource(R.drawable.translate_black)
                //rtcClient?.deleteAudioTrack()
                //rtcClient?.deleteLocalStream()
                //rtcClient?.reConnectLocalStream(true)
                startListening()
            }else{
                isTranslateMode = false
                binding.translateBackground.setBackgroundResource(R.drawable.mute2)
                binding.translateImg.setImageResource(R.drawable.translate)
                stopListening()
                //rtcClient?.addAudioTrack()
                //rtcClient?.deleteLocalStream()
                //rtcClient?.reConnectLocalStream(false)
            }
            //val intent = Intent(this@Calling, MainActivity::class.java)
            //startActivity(intent)
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

    // 듣기 시작
    private fun startListening() {
        rtcClient?.deleteAudioTrack()
        speechRecognizer.startListening(recognitionIntent) // 듣기 시작
        rtcClient?.addAudioTrack()
    }

    private fun stopListening() {
        speechRecognizer.stopListening() // 듣기 중지
    }

    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            Toast.makeText(applicationContext, "음성인식 시작", Toast.LENGTH_SHORT).show()
            //binding.tvState.text = "이제 말씀하세요!"
        }
        // 말하기 시작했을 때 호출
        override fun onBeginningOfSpeech() {
            //binding.tvState.text = "잘 듣고 있어요."
        }
        // 입력받는 소리의 크기를 알려줌
        override fun onRmsChanged(rmsdB: Float) {}
        // 말을 시작하고 인식이 된 단어를 buffer에 담음
        override fun onBufferReceived(buffer: ByteArray) {}
        // 말하기를 중지하면 호출
        override fun onEndOfSpeech() {
            //binding.tvState.text = "끝!"
            // 듣기 재시작
            if(isTranslateMode == true){
                startListening()
            }else{
                stopListening()
                //binding.tvState.text = "종료"
            }
        }
        // 오류 발생했을 때 호출
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
            Toast.makeText(applicationContext, "에러 발생: $message", Toast.LENGTH_SHORT).show()
            //binding.tvState.text = "에러 발생: $message"
            // 에러 발생 시 듣기 재시작
            if(isTranslateMode == true){
                startListening()
            }else{
                stopListening()
                //binding.tvState.text = "종료"
            }
        }
        // 인식 결과가 준비되면 호출
        override fun onResults(results: Bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            for (i in matches!!.indices) {
                binding.sttTestTxtview.text = matches[i]
            }

            // 인식 결과가 나오면 듣기 재시작
            if(isTranslateMode == true){
                startListening()
            }else{
                stopListening()
                //binding.tvState.text = "종료"
            }
        }
        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {}
        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {}
    }

}