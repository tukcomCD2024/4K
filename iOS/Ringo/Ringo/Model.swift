//
//  Model.swift
//  Ringo
//
//  Created by 강진혁 on 2/6/24.
//

import Foundation

struct SigninData: Codable {
    let id: Int
    let eamil: String
    let jwtToken: String
    let refreshToken: String
}

struct SigninDataResponse: Codable {
    let data: SigninData?
    let message: String
    let result: String
}

enum NetworkResult<T> {
    case success(T)
    case requestErr(T)
    case pathErr
    case serverErr
    case networkFail
}
