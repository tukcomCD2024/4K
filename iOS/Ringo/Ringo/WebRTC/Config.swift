//
//  Config.swift
//  WebRTC-Demo
//
//  Created by Stasel on 30/01/2019.
//  Copyright © 2019 Stasel. All rights reserved.
//

import Foundation
import WebRTC

// Set this to the machine's address which runs the signaling server. Do not use 'localhost' or '127.0.0.1'
//fileprivate let defaultSignalingServerUrl = URL(string: "ws://localhost:8080/signal")!
//fileprivate let defaultSignalingServerUrl = URL(string: "ws://192.168.0.7:7080/signal")!
fileprivate let defaultSignalingServerUrl = URL(string: "wss://4kringo.shop:8080/signal")!

// We use Google's public stun servers. For production apps you should deploy your own stun/turn servers.
fileprivate let stunServers = ["stun:stun.l.google.com:19302",
                               "stun:stun1.l.google.com:19302",
                               "stun:stun2.l.google.com:19302",
                               "stun:stun3.l.google.com:19302",
                               "stun:stun4.l.google.com:19302"]

fileprivate let defaultStunServers = RTCIceServer(urlStrings: stunServers)

fileprivate let defaultTurnServers = [
    RTCIceServer(urlStrings: ["turn:13.125.213.128"], username: "ringo", credential: "qaz7410")
]

fileprivate let defaultIceServers = [defaultStunServers] + defaultTurnServers

struct Config {
    let signalingServerUrl: URL
    let webRTCIceServers: [RTCIceServer]
    
    static let `default` = Config(signalingServerUrl: defaultSignalingServerUrl, webRTCIceServers: defaultIceServers)
}
