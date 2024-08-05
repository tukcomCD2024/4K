//
//  FriendService.swift
//  Ringo
//
//  Created by 강진혁 on 2/8/24.
//

import Foundation
import Alamofire

class FriendService {
    
    static let shared = FriendService()
    
    private init() {}
    
    func loadFriendsList(email: String, completion: @escaping(NetworkResult<Any>) -> Void)
    {
        let url = "https://4kringo.shop:8080/friendship/findByUserIdAndStatusOrFriendIdAndStatus"
        
        let header : HTTPHeaders = [
            "Content-Type" : "application/json"
        ]
        let body : Parameters = [
            "email" : email
        ]
        
        let dataRequest = AF.request(url,
                                    method: .post,
                                    parameters: body,
                                    encoding: JSONEncoding.default,
                                    headers: header,
        interceptor: AuthInterceptor())
                                    
        dataRequest.responseData{
            response in
            switch response.result {
            case .success:
                guard let statusCode = response.response?.statusCode else {return}
                guard let value = response.value else {return}
                let networkResult = self.judgeStatusLFL(by: statusCode, value)
                completion(networkResult)
                
            case .failure:
                completion(.networkFail)
            }
        }
    }
    func loadRequestList(email: String, completion: @escaping(NetworkResult<Any>) -> Void)
    {
        let url = "https://4kringo.shop:8080/friendship/findByFriendIdAndStatus"
        
        let header : HTTPHeaders = [
            "Content-Type" : "application/json"
        ]
        let body : Parameters = [
            "email" : email
        ]
        
        let dataRequest = AF.request(url,
                                    method: .post,
                                    parameters: body,
                                    encoding: JSONEncoding.default,
                                    headers: header,
        interceptor: AuthInterceptor())
                                    
        dataRequest.responseData{
            response in
            switch response.result {
            case .success:
                guard let statusCode = response.response?.statusCode else {return}
                guard let value = response.value else {return}
                let networkResult = self.judgeStatusLFL(by: statusCode, value)
                completion(networkResult)
                
            case .failure:
                completion(.networkFail)
            }
        }
    }
    private func judgeStatusLFL(by statusCode: Int, _ data: Data) -> NetworkResult<Any> {
        switch statusCode {
        case ..<300 : return isVaildDataLFL(data: data)
        case 400..<500 : return .pathErr
        case 500..<600 : return .serverErr
        default : return .networkFail
        }
    }
    private func isVaildDataLFL(data: Data) -> NetworkResult<Any> {
        let decoder = JSONDecoder()
        guard let decodedData = try? decoder.decode([FriendInfo].self, from: data)
        else { return .pathErr }
        return .success(decodedData as Any)
    }
    
    func sendFriendRequest(sender: String, receiver: String, completion: @escaping(NetworkResult<Any>) -> Void)
    {
        let url = "https://4kringo.shop:8080/friendship/sendFriendRequest"
        
        let header : HTTPHeaders = ["Content-Type" : "application/json"]
        let body : Parameters = [
            "senderEmail" : sender,
            "receiverEmail" : receiver
        ]
        
        let dataRequest = AF.request(url,
                                    method: .post,
                                    parameters: body,
                                    encoding: JSONEncoding.default,
                                    headers: header,
        interceptor: AuthInterceptor())
                                    
        dataRequest.responseData{
            response in
            switch response.result {
            case .success:
                guard let statusCode = response.response?.statusCode else {return}
                guard let value = response.value else {return}
                
                let networkResult = self.judgeStatus(by: statusCode, value)
                completion(networkResult)
                
            case .failure:
                completion(.networkFail)
            }
        }
    }
    func acceptRequest(sender: String, receiver: String, completion: @escaping(NetworkResult<Any>) -> Void)
    {
        let url = "https://4kringo.shop:8080/friendship/acceptFriendRequestById"
        
        let header : HTTPHeaders = ["Content-Type" : "application/json"]
        let body : Parameters = [
            "senderEmail" : sender,
            "receiverEmail" : receiver
        ]
        
        let dataRequest = AF.request(url,
                                    method: .post,
                                    parameters: body,
                                    encoding: JSONEncoding.default,
                                    headers: header,
        interceptor: AuthInterceptor())
                                    
        dataRequest.responseData{
            response in
            switch response.result {
            case .success:
                guard let statusCode = response.response?.statusCode else {return}
                guard let value = response.value else {return}
                
                let networkResult = self.judgeStatus(by: statusCode, value)
                completion(networkResult)
                
            case .failure:
                completion(.networkFail)
            }
        }
    }
    func rejectRequest(sender: String, receiver: String, completion: @escaping(NetworkResult<Any>) -> Void)
    {
        let url = "https://4kringo.shop:8080/friendship/rejectFriendRequestById"
        
        let header : HTTPHeaders = ["Content-Type" : "application/json"]
        let body : Parameters = [
            "senderEmail" : sender,
            "receiverEmail" : receiver
        ]
        
        let dataRequest = AF.request(url,
                                    method: .post,
                                    parameters: body,
                                    encoding: JSONEncoding.default,
                                    headers: header,
        interceptor: AuthInterceptor())
                                    
        dataRequest.responseData{
            response in
            switch response.result {
            case .success:
                guard let statusCode = response.response?.statusCode else {return}
                guard let value = response.value else {return}
                
                let networkResult = self.judgeStatus(by: statusCode, value)
                completion(networkResult)
                
            case .failure:
                completion(.networkFail)
            }
        }
    }
    private func judgeStatus(by statusCode: Int, _ data: Data) -> NetworkResult<Any> {
        print(statusCode)
        switch statusCode {
        case ..<300 : return isVaildData(data: data)
        case 400..<500 : return .pathErr
        case 500..<600 : 
            print(statusCode)
            print(data)
            return .serverErr
        default : return .networkFail
        }
    }
    private func isVaildData(data: Data) -> NetworkResult<Any> {
        guard let decodedData = String(data: data, encoding: .utf8)
        else { return .pathErr }
        return .success(decodedData as Any)
    }
}
