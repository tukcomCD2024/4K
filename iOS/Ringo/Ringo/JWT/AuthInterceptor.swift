//
//  AuthInterceptor.swift
//  Ringo
//
//  Created by 강진혁 on 7/2/24.
//

import Foundation
import Alamofire
import UIKit

final class AuthInterceptor : RequestInterceptor {
    
    private let retryLimit = 2
    
    func adapt(_ urlRequest: URLRequest, for session: Session, completion: @escaping (Result<URLRequest, any Error>) -> Void) {
        guard let accessToken = UserManager.getData(type: String.self, forKey: .accessToken) else {
            print("accessToken error")
            return
        }
        var urlRequest = urlRequest
        urlRequest.headers.add(.authorization(bearerToken: accessToken))
    }
    func retry(_ request: Request, for session: Session, dueTo error: any Error, completion: @escaping (RetryResult) -> Void) {
        guard let response = request.task?.response as? HTTPURLResponse, response.statusCode == 401 else {
                return completion(.doNotRetryWithError(error))
            }
            guard request.retryCount < retryLimit else { return completion(.doNotRetryWithError(error)) }
            Task {
                guard let refreshToken = UserManager.getData(type: String.self, forKey: .refreshToken) else {
                    print("refreshToken error")
                    return completion(.doNotRetryWithError(error))
                }
                //let api = API.refreshToken(refreshToken)
                //let token = try await apiManager.request(api, type: TokenReissueResponse.self) refreshToken 갱신 api 호출 후 결과 받아오기
                var newAccessToken:String = ""
                SigninSercive.shared.refresh(token: refreshToken) { response in
                    switch response {
                    case .success(let data):
                        newAccessToken = data as! String
                    case .requestErr(let err):
                        print(err)
                        return completion(.doNotRetryWithError(error))
                    case .pathErr:
                        print("pathErr")
                        return completion(.doNotRetryWithError(error))
                    case .serverErr:
                        print("serverErr")
                        return completion(.doNotRetryWithError(error))
                    case .networkFail:
                        print("networkFail")
                        return completion(.doNotRetryWithError(error))
                    }
                }
                UserManager.setData(value: newAccessToken, key: .accessToken) // 토큰 갱신 코드
                return completion(.retry)
            }
    }
}
