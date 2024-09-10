//
//  CallService.swift
//  Ringo
//
//  Created by 강진혁 on 2/13/24.
//

import Foundation
import AVFoundation

class CallService {
    
    static let shared = CallService()
    
    private let config = Config.default
    var signalClient: SignalingClient
    var webRTCClient: WebRTCClient
    
    private let AudioSession =  AVAudioSession.sharedInstance()
    var player: AVAudioPlayer?
    
    init() {
        self.signalClient = SignalingClient(webSocket: NativeWebSocket(url: self.config.signalingServerUrl))
        self.webRTCClient = WebRTCClient(iceServers: self.config.webRTCIceServers)
    }
    func refresh() {
        self.webRTCClient = WebRTCClient(iceServers: self.config.webRTCIceServers)
    }
    
    func playSound(_ soundName: String) {
        guard let url = Bundle.main.url(forResource: soundName, withExtension: "mp3") else { return }
        do {
            player = try AVAudioPlayer(contentsOf: url)
            player!.numberOfLoops = -1
            player!.prepareToPlay()
            player!.play()
        } catch let error {
            print(error.localizedDescription)
        }
    }
    func speakerOff() {
        DispatchQueue.global().async { [weak self] in
            guard let self = self else {
                return
            }
            do {
                try self.AudioSession.overrideOutputAudioPort(.none)
            } catch let error {
                debugPrint("Error setting AVAudioSession Output Port: \(error)")
            }
        }
    }

    func speakerOn() {
        DispatchQueue.global().async { [weak self] in
            guard let self = self else {
                return
            }
            do {
                try self.AudioSession.overrideOutputAudioPort(.speaker)
                try self.AudioSession.setActive(true)
            } catch let error {
                debugPrint("Couldn't force audio to speaker: \(error)")
            }
        }
    }
}
