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
        
        let contactsVC = UINavigationController(rootViewController: ContactsViewController())
        let recentsVC = RecentsViewController()
        let accountVC = AccountViewController()
        
        contactsVC.title = "Contacts"
        recentsVC.title = "Recents"
        accountVC.title = "Account"
        
        contactsVC.tabBarItem.image = UIImage(systemName: "person.3")
        recentsVC.tabBarItem.image = UIImage(systemName: "clock")
        accountVC.tabBarItem.image = UIImage(systemName: "person.text.rectangle")
        
        setViewControllers([contactsVC,recentsVC,accountVC], animated: true)
    }
    
}
