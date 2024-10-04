//
//  STT.swift
//  Ringo
//
//  Created by 강진혁 on 9/17/24.
//

import Foundation
import Speech

class STT {
    
    static let shared = STT(language: "en")
    
    private var speechRecognizer = SFSpeechRecognizer(locale: Locale(identifier: "en"))!
    
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest?
    
    private var recognitionTask: SFSpeechRecognitionTask?
    
    private let audioEngine = AVAudioEngine()
    
    init(language: String) {
        self.speechRecognizer = SFSpeechRecognizer(locale: Locale(identifier: language))!
        self.recognitionRequest = nil
        self.recognitionTask = nil
    }
    
    private func startRecording() throws {
        
        // Cancel the previous task if it's running.
        if let recognitionTask = recognitionTask {
            recognitionTask.cancel()
            self.recognitionTask = nil
        }
        
        // Configure the audio session for the app.
        let audioSession = AVAudioSession.sharedInstance()
        try audioSession.setCategory(.playAndRecord, mode: .measurement, options: .mixWithOthers)
        try audioSession.setActive(true, options: .notifyOthersOnDeactivation)
        let inputNode = audioEngine.inputNode
        
        // Create and configure the speech recognition request.
        recognitionRequest = SFSpeechAudioBufferRecognitionRequest()
        guard let recognitionRequest = recognitionRequest else { fatalError("Unable to created a SFSpeechAudioBufferRecognitionRequest object") }
        recognitionRequest.shouldReportPartialResults = true
        
        // Keep speech recognition data on device
        if #available(iOS 13, *) {
            recognitionRequest.requiresOnDeviceRecognition = true
        }
        
        // Create a recognition task for the speech recognition session.
        // Keep a reference to the task so that it can be canceled.
        recognitionTask = speechRecognizer.recognitionTask(with: recognitionRequest) { result, error in
            var isFinal = false
            
            if let result = result {
                // Update the text view with the results.
                //                self.textView2.text = result.bestTranscription.formattedString
                isFinal = result.isFinal
                debugPrint(result.bestTranscription.formattedString)
                if result.speechRecognitionMetadata != nil {
                    var message = Message(type: .stt_message, name: UserManager.getData(type: String.self, forKey: .email)!, target: UserManager.getData(type: String.self, forKey: .receiver)!)
                    message.data = .response(result.bestTranscription.formattedString)
                    CallService.shared.signalClient.send(message: message)
                    debugPrint("send trans msg")
                }
            }
            
            if error != nil || isFinal {
                // Stop recognizing speech if there is a problem.
                self.audioEngine.stop()
                inputNode.removeTap(onBus: 0)
                
                self.recognitionRequest = nil
                self.recognitionTask = nil
            }
        }
        
        // Configure the microphone input.
        let recordingFormat = inputNode.outputFormat(forBus: 0)
        inputNode.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { (buffer: AVAudioPCMBuffer, when: AVAudioTime) in
            self.recognitionRequest?.append(buffer)
        }
        
        audioEngine.prepare()
        try audioEngine.start()
    }
    
    private func stopRecording() {
//        let inputNode = audioEngine.inputNode
//        audioEngine.stop()
        recognitionRequest?.endAudio()
//        inputNode.removeTap(onBus: 0)
    }
    
    func start() {
        SFSpeechRecognizer.requestAuthorization { authStatus in
            switch authStatus{
            case.authorized:
                do {
                    try self.startRecording()
                    debugPrint("Translate start")
                } catch {
                    debugPrint("Recognition Not Available")
                }
            case.denied:
                debugPrint("User denied access to speech recognition")
            case.restricted:
                debugPrint("Speech recognition restricted on this device")
            case.notDetermined:
                debugPrint("Speech recognition not yet authorized")
            default:
                break
            }
        }
    }
    func stop() {
        stopRecording()
    }
    func reinit(language: String) {
        self.speechRecognizer = SFSpeechRecognizer(locale: Locale(identifier: language))!
    }
}
