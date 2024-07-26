//
//  Language.swift
//  Ringo
//
//  Created by 강진혁 on 7/22/24.
//

import Foundation

class Language {
    static let shared = Language()
    private let list = ["ko":"Korean","en":"English","zh":"Chinese","fr":"French","de":"German","es":"Spanish"]
    func getCount() -> Int {
        return list.count
    }
    func getList() -> Array<String> {
        let languages = list.keys.sorted()
        return languages
    }
    func getLanguageByCode(key:String) -> String {
        return list[key] ?? ""
    }
}
