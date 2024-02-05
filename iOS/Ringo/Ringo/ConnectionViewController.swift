//
//  ConnectionViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/6/24.
//

import UIKit
import SnapKit

class ConnectionViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

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
// MARK: - canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct ConnectionViewPreview: PreviewProvider {
    static var previews: some View {
        ConnectionViewController().toPreview()
    }
}
