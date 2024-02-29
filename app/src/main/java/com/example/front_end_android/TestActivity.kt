package com.example.front_end_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.front_end_android.databinding.ActivityCallingBinding
import com.example.front_end_android.databinding.ActivityTestBinding
import com.example.front_end_android.models.MessageModel
import java.util.Locale


class TestActivity : AppCompatActivity() {
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var editText:EditText
    private lateinit var binding:ActivityTestBinding

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionIntent: Intent
    private var isTranslateMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editText = binding.editText
        val textToSpeechBtn = binding.textToSpeechBtn

        textToSpeechBtn.setOnClickListener {
            textToSpeech = TextToSpeech(this) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = textToSpeech.setLanguage(Locale.getDefault())
                    if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED
                    ) {
                        Toast.makeText(this, "language is not supported", Toast.LENGTH_LONG).show()
                    }else{
                        if (binding.textView.text.toString().trim().isNotEmpty()) {
                            textToSpeech.speak(
                                binding.textView.text.toString().trim(),
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                            )
                        } else {
                            Toast.makeText(this, "Required", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        // RecognizerIntent 생성
        recognitionIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)    // 여분의 키
        // 실제로는 언어 설정에 내가 설정한 언어가 들어가야함
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")         // 언어 설정
        // 새 SpeechRecognizer 를 만드는 팩토리 메서드
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@TestActivity)
        speechRecognizer.setRecognitionListener(recognitionListener)    // 리스너 설정

        binding.speechToTextBtn.setOnClickListener {
            if(isTranslateMode==false){
                isTranslateMode=true
                startListening()
            }else{
                isTranslateMode=false
            }
        }


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
            Toast.makeText(applicationContext, "에러 발생: $message", Toast.LENGTH_SHORT).show()
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
                binding.textView.text = matches[i]
            }

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

    private fun startListening() {
        //rtcClient?.deleteAudioTrack()
        speechRecognizer.startListening(recognitionIntent) // 듣기 시작
        //rtcClient?.addAudioTrack()
    }

    private fun stopListening() {
        speechRecognizer.stopListening() // 듣기 중지
    }

}