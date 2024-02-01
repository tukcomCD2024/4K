//
//  ContactsNavigationController.swift
//  Ringo
//
//  Created by 강진혁 on 2/1/24.
//

import UIKit

class ContactsNavigationController: UINavigationController {
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setViewControllers([ContactsViewController()], animated: true)
        // Do any additional setup after loading the view.
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
// canvas 이용하기
import SwiftUI

struct ContactsNavigationControllerRepresentable: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> ContactsNavigationController {
        return ContactsNavigationController()
    }
    
    func updateUIViewController(_ uiViewController: ContactsNavigationController, context: Context) {
    }
    
    typealias UIViewControllerType = ContactsNavigationController
    
}
@available(iOS 13.0.0, *)
struct ContactsNavigationViewPreview: PreviewProvider {
    static var previews: some View {
        ContactsNavigationControllerRepresentable().edgesIgnoringSafeArea(.all)
    }
}
