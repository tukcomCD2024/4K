//
//  SignalClient.swift
//  WebRTC
//
//  Created by Stasel on 20/05/2018.
//  Copyright © 2018 Stasel. All rights reserved.
//

import Foundation
import WebRTC

protocol SignalClientDelegate: AnyObject {
    func signalClientDidConnect(_ signalClient: SignalingClient)
    func signalClientDidDisconnect(_ signalClient: SignalingClient)
    func signalClient(_ signalClient: SignalingClient, didReceiveRemoteSdp sdp: RTCSessionDescription)
    func signalClient(_ signalClient: SignalingClient, didReceiveCandidate candidate: RTCIceCandidate)
    func signalClient(_ signalClient: SignalingClient, didReceiveCallResponse response: String)
    func signalClient(_ signalClient: SignalingClient, didReceiveTranslation msg: String, language: String)
}

final class SignalingClient {
    
    private let decoder = JSONDecoder()
    private let encoder = JSONEncoder()
    private let webSocket: WebSocketProvider
    weak var delegate: SignalClientDelegate?
    
    init(webSocket: WebSocketProvider) {
        self.webSocket = webSocket
    }
    
    func connect() {
        self.webSocket.delegate = self
        self.webSocket.connect()
    }
    
    func store(id: String) {
        let message = ["type":"store_user","name":id]
        do {
            let dataMessage = try self.encoder.encode(message)
            
//            self.webSocket.send(data: dataMessage)
//            debugPrint(String(data: dataMessage, encoding: .utf8)!)
            
            let stringMessage = String(data: dataMessage, encoding: .utf8)!
            self.webSocket.send(string: stringMessage)
            debugPrint(stringMessage)
        }
        catch {
            debugPrint("Warning: Could not encode store message: \(error)")
        }
    }
    
    func startcall(id: String, target: String) {
        let message = ["type":"start_call","name":id,"target":target]
        do {
            let dataMessage = try self.encoder.encode(message)
            
//            self.webSocket.send(data: dataMessage)
//            debugPrint(String(data: dataMessage, encoding: .utf8)!)
            
            let stringMessage = String(data: dataMessage, encoding: .utf8)!
            self.webSocket.send(string: stringMessage)
            debugPrint(stringMessage)
        }
        catch {
            debugPrint("Warning: Could not encode store message: \(error)")
        }
    }
    
    func send(message: Message) {
        do {
            let dataMessage = try self.encoder.encode(message)
            
//            self.webSocket.send(data: dataMessage)
//            print(String(data: dataMessage, encoding: .utf8)!)
            
            let stringMessage = String(data: dataMessage, encoding: .utf8)!
            self.webSocket.send(string: stringMessage)
//            print("msg: " + stringMessage)
        }
        catch {
            debugPrint("Warning: Could not encode sdp: \(error)")
        }
    }
    
//    func send(candidate rtcIceCandidate: RTCIceCandidate) {
//        let message = Message.candidate(IceCandidate(from: rtcIceCandidate))
//        do {
//            let dataMessage = try self.encoder.encode(message)
////            self.webSocket.send(data: dataMessage)
//            
//            let stringMessage = String(data: dataMessage, encoding: .utf8)!
//            self.webSocket.send(string: stringMessage)
//            print(stringMessage)
//        }
//        catch {
//            debugPrint("Warning: Could not encode candidate: \(error)")
//        }
//    }
}


extension SignalingClient: WebSocketProviderDelegate {
    func webSocketDidConnect(_ webSocket: WebSocketProvider) {
        self.delegate?.signalClientDidConnect(self)
    }
    
    func webSocketDidDisconnect(_ webSocket: WebSocketProvider) {
        self.delegate?.signalClientDidDisconnect(self)
        
        // try to reconnect every two seconds
        DispatchQueue.global().asyncAfter(deadline: .now() + 2) {
            debugPrint("Trying to reconnect to signaling server...")
            self.webSocket.connect()
        }
    }
    
    func webSocket(_ webSocket: WebSocketProvider, didReceiveData data: Data) {
        let message: Message
        do {
            message = try self.decoder.decode(Message.self, from: data)
        }
        catch {
            debugPrint("Warning: Could not decode incoming message: \(error)")
            return
        }
        
//        switch message {
//        case .candidate(let iceCandidate):
//            self.delegate?.signalClient(self, didReceiveCandidate: iceCandidate.rtcIceCandidate)
//        case .sdp(let sessionDescription):
//            self.delegate?.signalClient(self, didReceiveRemoteSdp: sessionDescription.rtcSessionDescription)
//        }
        
        switch message.type {
        case .call_response:
            debugPrint("call_response")
//            debugPrint(message)
            self.delegate?.signalClient(self, didReceiveCallResponse: message.dataString())
        case .offer_received,.answer_received:
            debugPrint("received")
//            debugPrint(message)
            switch message.data {
            case .sdp(let sessionDescription):
                self.delegate?.signalClient(self, didReceiveRemoteSdp: sessionDescription.rtcSessionDescription)
            default:
                print("??")
            }
        case .ice_candidate:
            debugPrint("ice_candidate")
//            debugPrint(message)
            switch message.data {
            case .candidate(let iceCandidate):
                self.delegate?.signalClient(self, didReceiveCandidate: iceCandidate.rtcIceCandidate)
            default:
                print("????")
            }
        case .translate_message:
            debugPrint("receive trans msg")
            debugPrint(message)
            self.delegate?.signalClient(self, didReceiveTranslation: message.dataString(), language: message.target!)
        default:
            debugPrint("message.type is nothing")
            debugPrint(message)
        }

    }
}
