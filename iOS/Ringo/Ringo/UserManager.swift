//
//  UserManager.swift
//  Ringo
//
//  Created by 강진혁 on 7/3/24.
//

import Foundation

class UserManager {
    enum UserDefaultsKeys: String, CaseIterable {
        case email
        case name
        case language
        case password
        case accessToken
        case refreshToken
        case receiver
    }
    
    static func setData<T>(value: T, key: UserDefaultsKeys) {
        UserDefaults.standard.set(value, forKey: key.rawValue)
    }
    
    static func getData<T>(type: T.Type, forKey: UserDefaultsKeys) -> T? {
        let value = UserDefaults.standard.object(forKey: forKey.rawValue) as? T
        return value
    }
    
    static func removeData(key: UserDefaultsKeys) {
        UserDefaults.standard.removeObject(forKey: key.rawValue)
    }
    static func resetData() {
        for key in UserDefaults.standard.dictionaryRepresentation().keys {
            UserDefaults.standard.removeObject(forKey: key.description)
        }
    }
}
