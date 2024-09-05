//
//  Model.swift
//  Ringo
//
//  Created by 강진혁 on 2/6/24.
//

import Foundation

struct SigninData: Codable {
    let language: String
    let accessToken: String
    let refreshToken: String
}

struct SigninDataResponse: Codable {
    let status: String
    let data: SigninData?
    let message: String?
}

struct FriendInfo: Codable {
    let name: String
    let language: String
    let email: String
}

enum NetworkResult<T> {
    case success(T)
    case requestErr(T)
    case pathErr
    case serverErr
    case networkFail
    case dataErr
}
