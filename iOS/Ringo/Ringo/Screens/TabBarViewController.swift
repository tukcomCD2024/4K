//
//  TabBarViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/1/24.
//

import UIKit
import SnapKit

class TabBarViewController: UITabBarController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .systemBackground
        
        tabBar.backgroundColor = .init(white: 1, alpha: 0.8)
        
        let contactsVC = UINavigationController(rootViewController: ContactsViewController())
        let recentsVC = RecentsViewController()
        let accountVC = AccountViewController()
        
        contactsVC.tabBarItem = UITabBarItem(title: "Contacts", image: UIImage(systemName: "person.3"), tag: 1)
        recentsVC.tabBarItem = UITabBarItem(title: "Recents", image: UIImage(systemName: "clock"), tag: 2)
        accountVC.tabBarItem = UITabBarItem(title: "Account", image: UIImage(systemName: "person.text.rectangle"), tag: 3)

        contactsVC.navigationBar.backgroundColor = .systemBackground
        
        setViewControllers([contactsVC,recentsVC,accountVC], animated: false)
    }
}

// MARK: canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct TabBarViewControllerPreView: PreviewProvider {
    static var previews: some View {
    // 사용할 뷰 컨트롤러를 넣어주세요
        TabBarViewController()
            .toPreview()
    }
}
