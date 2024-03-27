//
//  TTS.swift
//  Ringo
//
//  Created by 강진혁 on 3/20/24.
//

import Foundation
import AVFoundation

class TTS {
    static let shared = TTS()
    
    private var synthesizer = AVSpeechSynthesizer()
    
    func play(_ string: String, _ language: String) {
        debugPrint("문장 : " + string)
        let utterance = AVSpeechUtterance(string: string)
        utterance.voice = AVSpeechSynthesisVoice(language: language)
        utterance.rate = 0.4
        synthesizer.stopSpeaking(at: .immediate)
        synthesizer.speak(utterance)
    }
        
    func stop() {
        synthesizer.stopSpeaking(at: .immediate)
    }
    
}
