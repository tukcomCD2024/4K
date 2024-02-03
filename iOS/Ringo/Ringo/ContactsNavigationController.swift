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

// MARK: canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct ContactsNavigationControllerPreView: PreviewProvider {
    static var previews: some View {
    // 사용할 뷰 컨트롤러를 넣어주세요
        ContactsViewController()
            .toPreview()
    }
}
