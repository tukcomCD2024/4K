//
//  STTViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/21/24.
//

import UIKit
import SnapKit
import Speech

class STTViewController: UIViewController, SFSpeechRecognizerDelegate {

    static let shared = STTViewController()
    
    private var speechRecognizer = SFSpeechRecognizer(locale: Locale(identifier: "en"))!
    
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest?
    
    private var recognitionTask: SFSpeechRecognitionTask?
    
    private let audioEngine = AVAudioEngine()
    
    let backBtn = UIButton()
    let name = UILabel()
    let textView = UITextView()
    let textView2 = UITextView()
//    let recordButton = UIButton()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setUpView()
        setUpValue()
        setConstraints()
    }
    
    func setUpView() {
        view.addSubview(backBtn)
        view.addSubview(name)
        view.addSubview(textView)
        view.addSubview(textView2)
//        view.addSubview(recordButton)
    }
    
    func setUpValue() {
        
        view.backgroundColor = .systemBackground
        
        backBtn.configuration = UIButton.Configuration.plain()
        backBtn.configurationUpdateHandler = { btn in
            btn.configuration?.image = .init(systemName: "chevron.backward")
            btn.configuration?.title = "Back"
            btn.configuration?.baseBackgroundColor = .systemBlue
            btn.addTarget(self, action: #selector(self.backButtonTapped), for: .touchUpInside)
        }

        name.text = "Name"
        name.font = .systemFont(ofSize: 20)
        
        textView.font = .systemFont(ofSize: 20)
        textView.layer.cornerRadius = 25
        textView.backgroundColor = .secondarySystemFill
        textView.textContainerInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
        
        textView2.textColor = .white
        textView2.font = .systemFont(ofSize: 20)
        textView2.layer.cornerRadius = 25
        textView2.backgroundColor = .systemBlue
        textView2.textContainerInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
        
//        recordButton.configuration = .plain()
//        recordButton.isEnabled = false
//        recordButton.addTarget(self, action: #selector(recordButtonTapped), for: .touchUpInside)
//        recordButton.setTitle("말하기", for: .disabled)
//        recordButton.titleLabel?.font = .systemFont(ofSize: 50)
    }
    
    func setConstraints() {
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.height.equalToSuperview().multipliedBy(0.03)
        }
        name.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.centerX.equalToSuperview()
            make.centerY.equalTo(backBtn.snp.centerY)
        }
        textView.snp.makeConstraints { make in
            make.top.equalTo(backBtn.snp.bottom).offset(20)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.trailing.equalTo(view.safeAreaLayoutGuide).inset(120)
            make.bottom.equalTo(view.snp.centerY).offset(15)
        }
        textView2.snp.makeConstraints { make in
            make.top.equalTo(textView.snp.bottom).offset(20)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(120)
            make.trailing.equalTo(view.safeAreaLayoutGuide).inset(20)
            make.bottom.equalTo(view.safeAreaLayoutGuide).offset(-20)
        }
//        recordButton.snp.makeConstraints { make in
//            make.bottom.equalTo(view.safeAreaLayoutGuide).inset(30)
//            make.centerX.equalTo(view.center.x)
//        }
    }
    
    override public func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // Configure the SFSpeechRecognizer object already
        // stored in a local member variable.
        speechRecognizer.delegate = self
        
        // Make the authorization request.
        SFSpeechRecognizer.requestAuthorization { authStatus in

            // Divert to the app's main thread so that the UI
            // can be updated.
            OperationQueue.main.addOperation {
                switch authStatus {
                case .authorized:
//                        self.recordButton.isEnabled = true
                    DispatchQueue.global().async {
                        do {
                            try self.startRecording()
                            self.textView2.text = "Translate start"
                        } catch {
                            self.textView2.text = "Recognition Not Available"
                        }
                    }
                case .denied:
//                    self.recordButton.isEnabled = false
//                    self.recordButton.setTitle("User denied access to speech recognition", for: .disabled)
                    self.textView2.text = "User denied access to speech recognition"
                    
                case .restricted:
//                    self.recordButton.isEnabled = false
//                    self.recordButton.setTitle("Speech recognition restricted on this device", for: .disabled)
                    self.textView2.text = "Speech recognition restricted on this device"
                case .notDetermined:
//                    self.recordButton.isEnabled = false
//                    self.recordButton.setTitle("Speech recognition not yet authorized", for: .disabled)
                    self.textView2.text = "Speech recognition not yet authorized"
                default:
//                    self.recordButton.isEnabled = false
                    break
                }
            }
        }
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
                self.textView2.text = result.bestTranscription.formattedString
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
                
//                self.recordButton.isEnabled = true
//                self.recordButton.setTitle("말하기", for: [])
            }
        }

        // Configure the microphone input.
        let recordingFormat = inputNode.outputFormat(forBus: 0)
        inputNode.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { (buffer: AVAudioPCMBuffer, when: AVAudioTime) in
            self.recognitionRequest?.append(buffer)
        }
        
        audioEngine.prepare()
        try audioEngine.start()
        
        // Let the user know to start talking.
        textView.text = "..."
        textView2.text = "..."
    }
    
    // MARK: SFSpeechRecognizerDelegate
    
    public func speechRecognizer(_ speechRecognizer: SFSpeechRecognizer, availabilityDidChange available: Bool) {
        if available {
//            recordButton.isEnabled = true
//            recordButton.setTitle("Translate start", for: [])
            self.textView2.text = "Translate start"
        } else {
//            recordButton.isEnabled = false
//            recordButton.setTitle("Recognition Not Available", for: .disabled)
            self.textView2.text = "Recognition Not Available"
        }
    }
    
    // MARK: Interface Builder actions
    
    @objc func recordButtonTapped() {
        if audioEngine.isRunning {
            audioEngine.stop()
            recognitionRequest?.endAudio()
//            recordButton.isEnabled = false
//            recordButton.setTitle("Stopping", for: .disabled)
        } else {
            do {
                try startRecording()
//                recordButton.setTitle("Translate stop", for: [])
            } catch {
//                recordButton.setTitle("Recording Not Available", for: [])
            }
        }
    }
    
    func reinit(lang: String, callerName: String){
        speechRecognizer = SFSpeechRecognizer(locale: Locale(identifier: lang))!
        name.text = callerName
    }
    
    @objc func backButtonTapped(){
        audioEngine.stop()
        recognitionRequest?.endAudio()
        dismiss(animated: true)
    }
}
// MARK: - canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct TestSTTViewPreview: PreviewProvider {
    static var previews: some View {
        STTViewController().toPreview()
    }
}
