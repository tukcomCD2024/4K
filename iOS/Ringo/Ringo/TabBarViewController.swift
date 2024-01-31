//
//  TabBarViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/1/24.
//

import UIKit

class TabBarViewController: UITabBarController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let contactsVC = ContactsViewController()
        let recentsVC = RecentsViewController()
        let AccountVC = AccountViewController()
        
    }
    
}
