package com.example.front_end_android

import android.app.AlertDialog
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.front_end_android.databinding.ActivityCallingBinding
import com.example.front_end_android.models.IceCandidateModel
import com.example.front_end_android.models.MessageModel
import com.example.front_end_android.util.NewMessageInterface
import com.example.front_end_android.util.PeerConnectionObserver
import com.example.front_end_android.util.RTCAudioManager
import com.google.gson.Gson
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription
import java.util.Locale

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
    private lateinit var stt_message:String
    private lateinit var textToSpeech: TextToSpeech

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionIntent: Intent

    private lateinit var audioManager: AudioManager

    //private lateinit var translationSubtitlesLinearLayout:LinearLayout
    //private lateinit var translationSubtitlesScrollView:ScrollView
    //private lateinit var messageContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        binding.linearLayout.visibility = View.GONE
        val callingState  = MyApplication.preferences.getString("callingState",".")
        if(callingState == "receiver"){
            binding.waitTxt.visibility = View.VISIBLE
            binding.buttonTest.visibility = View.GONE
        }
        //binding.exitCallBackground.visibility = View.GONE

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

        binding.translationSubtitleBackTxt.setOnClickListener {
            endTranslationSubtitles()
        }

        binding.translationSubtitleImg.setOnClickListener {
            endTranslationSubtitles()
        }


        // RecognizerIntent 생성
        recognitionIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)    // 여분의 키
        // 실제로는 언어 설정에 내가 설정한 언어가 들어가야함
        val sttLanguage = MyApplication.preferences.getString("SttLanguage","ko-KR")
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, sttLanguage)         // 언어 설정
        // 새 SpeechRecognizer 를 만드는 팩토리 메서드
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@Calling)
        speechRecognizer.setRecognitionListener(recognitionListener)    // 리스너 설정

        binding.translateImg.setOnClickListener {
            if(isTranslateMode == false){
                isTranslateMode = true
                binding.translateBackground.setBackgroundResource(R.drawable.mute2white)
                binding.translateImg.setImageResource(R.drawable.translate_black)
                rtcClient?.deleteAudioTrack()
                AlertDialog.Builder(this)
                    .setTitle("Translation Subtitles")
                    .setMessage("Are you sure you want to use translation subtitles?")
                    .setPositiveButton("yes") { dialog, which ->
                        startTranslationSubtitles()
                    }
                    .setNegativeButton("no", null)
                    .show()
                startListening()
            }else{
                AlertDialog.Builder(this)
                    .setTitle("Translation Call")
                    .setMessage("Do you want to end the translation call?")
                    .setPositiveButton("yes") { dialog, which ->
                        isTranslateMode = false
                        binding.translateBackground.setBackgroundResource(R.drawable.mute2)
                        binding.translateImg.setImageResource(R.drawable.translate)
                        stopListening()
                        rtcClient?.addAudioTrack()
                    }
                    .setNegativeButton("no"){ dialog, which ->
                        startTranslationSubtitles()
                    }
                    .show()
            }
        }

    }

    private fun init(){
        userName = MyApplication.preferences.getString("email",".")
        targetName = MyApplication.preferences.getString("targetName",".")

        //translationSubtitlesScrollView = ScrollView(this@Calling)
        //translationSubtitlesLinearLayout = LinearLayout(this@Calling)
        //translationSubtitlesLinearLayout.orientation = LinearLayout.VERTICAL

        binding.nicknameInit.text = targetName

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
                //상대방이들어오면
                socketRepository?.sendMessageToSocket(
                    MessageModel("start_call",userName,targetName,null
                    ))
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
                            binding.waitTxt.visibility = View.GONE
                            binding.buttonTest.visibility = View.GONE
                            binding.linearLayout.visibility = View.VISIBLE
                            binding.exitCallBackground.visibility = View.VISIBLE
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

                val data11 = message.data
                Log.d("YMC", "data: $data11")//*

                val sdpStartIndex = message.data.toString().indexOf("sdp=")
                val sdpEndIndex = message.data.toString().indexOf("}", startIndex = sdpStartIndex)
                val sdpValueAnswer = message.data.toString().substring(sdpStartIndex + 4, sdpEndIndex) // "+ 4" to skip "sdp="

                Log.d("YMC", "sdpValueAnswer: $sdpValueAnswer")
                val session = SessionDescription(
                    SessionDescription.Type.ANSWER,
                    sdpValueAnswer.toString()
                    //message.data.toString()
                )
                rtcClient?.onRemoteSessionReceived(session)
                runOnUiThread {

                    binding.remoteViewLoading.visibility = View.GONE
                }
            }
            "offer_received" -> {
                runOnUiThread {
                    Log.d("YMC", "offer_received: $message")//*

                    val data11 = message.data
                    Log.d("YMC", "data: $data11")//*

                    val sdpStartIndex = message.data.toString().indexOf("sdp=")
                    val sdpEndIndex = message.data.toString().indexOf("}", startIndex = sdpStartIndex)
                    val sdpValueOffer = message.data.toString().substring(sdpStartIndex + 4, sdpEndIndex) // "+ 4" to skip "sdp="

                    Log.d("YMC", "sdpValueOffer: $sdpValueOffer")
                    setIncomingCallLayoutVisible()
                    binding.incomingNameTV.text = "${message.name.toString()} is calling you"
                    binding.acceptButton.setOnClickListener {
                        binding.buttonTest.visibility = View.GONE
                        binding.waitTxt.visibility = View.GONE
                        binding.linearLayout.visibility = View.VISIBLE
                        binding.exitCallBackground.visibility = View.VISIBLE
                        setIncomingCallLayoutGone()

                        binding.apply {
                            rtcClient?.initializeSurfaceView(localView)
                            rtcClient?.initializeSurfaceView(remoteView)
                            rtcClient?.startLocalVideo(localView)
                        }
                        val session = SessionDescription(
                            SessionDescription.Type.OFFER,
                            sdpValueOffer.toString()
                            //message.data.toString()
                        )
                        rtcClient?.onRemoteSessionReceived(session)
                        Log.d("YMC", "@@@@@@@@@@@@@@@")
                        rtcClient?.answer(message.name!!)
                        Log.d("YMC", "###############")
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
            "translate_message"->{
                /*if(isTranslateMode == true){
                    isTranslateMode = false
                    stopListening()
                }*/
                textToSpeech = TextToSpeech(this) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        val result: Int = setTTSLanguage(message)
                        //val result = textToSpeech.setLanguage(Locale.US)
                        if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED
                        ) {
                            Toast.makeText(this, "Language is not supported", Toast.LENGTH_LONG).show()
                        } else {
                            binding.sttTestTxtview.text = message.target.toString()
                            //audioManager.mode = AudioManager.MODE_IN_CALL

                            textToSpeech.speak(
                                message.data.toString().trim(),
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                            )
                        }
                    }
                }
                /*if(isTranslateMode == false){
                    isTranslateMode = true
                    startListening()
                }*/
            }
        }
    }

    private fun setTTSLanguage(message: MessageModel): Int {
        return if(message.target.toString().trim() == "ko"){
            textToSpeech.setLanguage(Locale.KOREAN)
        }else if(message.target.toString().trim() == "en"){
            textToSpeech.setLanguage(Locale.US)
        }else if(message.target.toString().trim() == "zh-CN"){
            textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE)
        }else if(message.target.toString().trim() == "de"){
            textToSpeech.setLanguage(Locale.GERMANY)
        }else if(message.target.toString().trim() == "es"){
            textToSpeech.setLanguage(Locale("es", "ES"))
        }else if(message.target.toString().trim() == "fr"){
            textToSpeech.setLanguage(Locale.FRANCE)
        }else{
            TextToSpeech.LANG_NOT_SUPPORTED
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
        //rtcClient?.deleteAudioTrack()
        speechRecognizer.startListening(recognitionIntent) // 듣기 시작
        //rtcClient?.addAudioTrack()
    }

    private fun stopListening() {
        speechRecognizer.stopListening() // 듣기 중지
    }

    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            //Toast.makeText(applicationContext, "음성인식 시작", Toast.LENGTH_SHORT).show()
            //binding.tvState.text = "이제 말씀하세요!"
        }
        // 말하기 시작했을 때 호출
        override fun onBeginningOfSpeech() {}
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
                //stopListening()
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
            //Toast.makeText(applicationContext, "에러 발생: $message", Toast.LENGTH_SHORT).show()
            //binding.tvState.text = "에러 발생: $message"
            // 에러 발생 시 듣기 재시작
            if(isTranslateMode == true){
                startListening()
            }else{
                //stopListening()
                //binding.tvState.text = "종료"
            }
        }
        // 인식 결과가 준비되면 호출
        override fun onResults(results: Bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            for (i in matches!!.indices) {
                stt_message = matches[i]
            }

            addTranslationSubtitlesSTT(stt_message)

            socketRepository?.sendMessageToSocket(
                MessageModel("stt_message",userName,targetName,stt_message)
            )

            // 인식 결과가 나오면 듣기 재시작
            if(isTranslateMode == true){
                startListening()
            }else{
                //stopListening()
                //binding.tvState.text = "종료"
            }
        }
        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {}
        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {}
    }

    private fun startTranslationSubtitles(){
        binding.translationSubtitleImg.visibility = View.VISIBLE
        binding.translationSubtitleBackTxt.visibility = View.VISIBLE
        binding.translationSubtitleNameTxt.visibility = View.VISIBLE
        binding.translationSubtitleScrollView.visibility = View.VISIBLE
    }

    private fun endTranslationSubtitles(){
        binding.translationSubtitleImg.visibility = View.GONE
        binding.translationSubtitleBackTxt.visibility = View.GONE
        binding.translationSubtitleNameTxt.visibility = View.GONE
        binding.translationSubtitleScrollView.visibility = View.GONE
    }

    private fun addTranslationSubtitlesSTT(STTMessage: String) {
        val textView = TextView(this@Calling).apply {
            text = STTMessage
            textSize = 16f
            // 여기에 메시지 스타일을 추가할 수 있습니다 (예: 패딩, 배경색 등)
        }

        binding.messageContainer.addView(textView)

        binding.translationSubtitleScrollView.post {
            binding.translationSubtitleScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun addTranslationSubtitlesTTS(TTSMessage: String){

    }

}