//
//  CallService.swift
//  Ringo
//
//  Created by 강진혁 on 2/13/24.
//

import Foundation

class CallService {
    
    static let shared = CallService()
    
    private let config = Config.default
    var signalClient: SignalingClient
    var webRTCClient: WebRTCClient
    
    init() {
        self.signalClient = SignalingClient(webSocket: NativeWebSocket(url: self.config.signalingServerUrl))
        self.webRTCClient = WebRTCClient(iceServers: self.config.webRTCIceServers)
    }
}
