//
//  FriendRequestList.swift
//  Ringo
//
//  Created by 강진혁 on 7/30/24.
//

import Foundation

class FriendRequestList {
    static let shared = FriendRequestList()
    private var list: [FriendInfo] = [FriendInfo(name:"name1",language:"language1",email:"email1"),
                                       FriendInfo(name:"name2",language:"language2",email:"email2")]
    func getList() -> [FriendInfo] {
        return list
    }
    func remove(at index: Int) {
        list.remove(at: index)
    }
    func reload() {
        guard let email = UserManager.getData(type: String.self, forKey: .email) else { return }
        FriendService.shared.loadRequestList(email: email) { response in
            switch response {
            case .success(let data):
                    
                guard let data = data as? [FriendInfo] else { return }
                if !data.isEmpty {
                    self.list = data
                    print(data)
                } else {
                    self.list = data
                    print(data)
                }
                    
            case .requestErr(let err):
                print(err)
            case .pathErr:
                print("pathErr")
            case .serverErr:
                print("serverErr")
            case .networkFail:
                print("networkFail")
            case .dataErr:
                print("dataErr")
            }
        }
    }
}
