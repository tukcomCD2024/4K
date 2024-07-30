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
//        list = [FriendInfo(name:"name1",language:"language1",email:"email1"),
//                FriendInfo(name:"name2",language:"language2",email:"email2")]
    }
}
