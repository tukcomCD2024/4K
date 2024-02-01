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
        
        tabBar.backgroundColor = .init(white: 1, alpha: 0.8)
        
//        let contactsVC = UINavigationController(rootViewController: ContactsViewController())
        let contactsVC = ContactsNavigationController()
        let recentsVC = RecentsViewController()
        let accountVC = AccountViewController()
        
        contactsVC.tabBarItem = UITabBarItem(title: "Contacts", image: UIImage(systemName: "person.3"), tag: 1)
        recentsVC.tabBarItem = UITabBarItem(title: "Recents", image: UIImage(systemName: "clock"), tag: 2)
        accountVC.tabBarItem = UITabBarItem(title: "Account", image: UIImage(systemName: "person.text.rectangle"), tag: 3)
        
        setViewControllers([contactsVC,recentsVC,accountVC], animated: false)
    }
}

// canvas 이용하기
import SwiftUI

struct TabBarViewControllerRepresentable: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> TabBarViewController {
        return TabBarViewController()
    }
    
    func updateUIViewController(_ uiViewController: TabBarViewController, context: Context) {
    }
    
    typealias UIViewControllerType = TabBarViewController
    
}
@available(iOS 13.0.0, *)
struct TabBarViewPreview: PreviewProvider {
    static var previews: some View {
        TabBarViewControllerRepresentable().edgesIgnoringSafeArea(.all)
    }
}
