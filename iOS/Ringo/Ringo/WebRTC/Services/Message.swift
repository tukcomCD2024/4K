//
//  Message.swift
//  WebRTC-Demo
//
//  Created by Stasel on 20/02/2019.
//  Copyright © 2019 Stasel. All rights reserved.
//

import Foundation

//enum Message {
//    case sdp(SessionDescription)
//    case candidate(IceCandidate)
//}
//
//extension Message: Codable {
//    init(from decoder: Decoder) throws {
//        let container = try decoder.container(keyedBy: CodingKeys.self)
//        let type = try container.decode(String.self, forKey: .type)
//        switch type {
//        case String(describing: SessionDescription.self):
//            self = .sdp(try container.decode(SessionDescription.self, forKey: .payload))
//        case String(describing: IceCandidate.self):
//            self = .candidate(try container.decode(IceCandidate.self, forKey: .payload))
//        default:
//            throw DecodeError.unknownType
//        }
//    }
//    
//    func encode(to encoder: Encoder) throws {
//        var container = encoder.container(keyedBy: CodingKeys.self)
//        switch self {
//        case .sdp(let sessionDescription):
//            try container.encode(sessionDescription, forKey: .payload)
//            try container.encode(String(describing: SessionDescription.self), forKey: .type)
//        case .candidate(let iceCandidate):
//            try container.encode(iceCandidate, forKey: .payload)
//            try container.encode(String(describing: IceCandidate.self), forKey: .type)
//        }
//    }
//    
//    enum DecodeError: Error {
//        case unknownType
//    }
//    
//    enum CodingKeys: String, CodingKey {
//        case type, payload
//    }
//}



struct Message: Codable {
    
    enum MessageType: String, Codable {
        case call_response, create_offer, offer_received, create_answer, answer_received, ice_candidate, start_call
    }

    enum DataType {
        case sdp(SessionDescription)
        case candidate(IceCandidate)
        case response(String)
        
        func stringValue() -> String {
            switch self {
            case .sdp(let sessionDescription):
                return sessionDescription.sdp
            case .candidate(let iceCandidate):
                return iceCandidate.sdpCandidate
            case .response(let string):
                return string
            }
        }
    }
    
    var type: MessageType
    var name: String?
    var target: String?
    var data: DataType = .response("")
    
    init(type: MessageType, name: String, target: String){
        self.type = type
        self.name = name
        self.target = target
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.type = try container.decode(MessageType.self, forKey: .type)
        self.name = try? container.decode(String.self, forKey: .name)
        self.target = try? container.decode(String.self, forKey: .target)
        switch type {
        case .call_response:
            self.data = .response(try container.decode(String.self, forKey: .data))
        case .create_offer,.offer_received,.create_answer,.answer_received:
            debugPrint("sdp")
            self.data = .sdp(try container.decode(SessionDescription.self, forKey: .data))
        case .ice_candidate:
            debugPrint("ice")
            self.data = .candidate(try container.decode(IceCandidate.self, forKey: .data))
        case .start_call:
            debugPrint("startcall")
        }
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(type, forKey: .type)
        try container.encode(name, forKey: .name)
        try container.encode(target, forKey: .target)
        switch data {
        case .sdp(let sessionDescription):
            try container.encode(sessionDescription, forKey: .data)
        case .candidate(let iceCandidate):
            try container.encode(iceCandidate, forKey: .data)
        case .response(let string):
            try container.encode(string, forKey: .data)
        }
    }
    
    enum CodingKeys: String, CodingKey {
        case type, name, target, data
    }
    
    func dataString() -> String {
        return (self.data.stringValue())
    }
}

//struct Message: Codable {
//    var type: String
//    var name: String?
//    var target: String?
//    var data: String?
//}
