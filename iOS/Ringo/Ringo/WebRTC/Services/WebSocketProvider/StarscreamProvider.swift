//
//  StarscreamProvider.swift
//  WebRTC-Demo
//
//  Created by stasel on 15/07/2019.
//  Copyright © 2019 stasel. All rights reserved.
//

import Foundation
import Starscream

class StarscreamWebSocket: WebSocketProvider {

    var delegate: WebSocketProviderDelegate?
    private let socket: WebSocket
    
    init(url: URL) {
        self.socket = WebSocket(request: URLRequest(url: url))
        self.socket.delegate = self
    }
    
    func connect() {
        self.socket.connect()
    }
    
    func send(data: Data) {
        self.socket.write(data: data)
    }
    
    func send(string: String) {
        self.socket.write(string: string)
    }
    
    func forceDisconnect() {
        self.socket.forceDisconnect()
    }
}

extension StarscreamWebSocket: Starscream.WebSocketDelegate {
    func didReceive(event: Starscream.WebSocketEvent, client: Starscream.WebSocketClient) {
        print("idk")
    }
    
    
    func didReceive(event: Starscream.WebSocketEvent, client: Starscream.WebSocket) {
        switch event {
        case .connected:
            self.delegate?.webSocketDidConnect(self)
        case .disconnected:
            self.delegate?.webSocketDidDisconnect(self)
        case .text:
            debugPrint("Warning: Expected to receive data format but received a string. Check the websocket server config.")
        case .binary(let data):
            self.delegate?.webSocket(self, didReceiveData: data)
        default:
            break
        }
    }
}
