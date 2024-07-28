//
//  FriendRequestViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/2/24.
//

import UIKit

class FriendRequestViewController: UIViewController {

    let input_friendEamil = UITextField()
    let sendRequestBtn = UIButton()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setUpView()
        setUpValue()
        setConstraints()
        // Do any additional setup after loading the view.
    }
    func setUpView() {
        view.addSubview(input_friendEamil)
        view.addSubview(sendRequestBtn)
    }
    func setUpValue() {
        view.backgroundColor = .systemBackground
        view.keyboardLayoutGuide.followsUndockedKeyboard = true
        tabBarController?.tabBar.isHidden = true
        
        self.navigationItem.title = "Request Friend"
        self.navigationItem.largeTitleDisplayMode = .always
        
        input_friendEamil.placeholder = "Enter your name"
        input_friendEamil.borderStyle = .roundedRect
        input_friendEamil.font = .preferredFont(forTextStyle: .title3)
        input_friendEamil.clearButtonMode = .always
        input_friendEamil.autocapitalizationType = .none
        input_friendEamil.delegate = self
        input_friendEamil.autocorrectionType = .no
        input_friendEamil.spellCheckingType = .no
        input_friendEamil.backgroundColor = .systemGray6
        
        sendRequestBtn.configuration = UIButton.Configuration.filled()
        sendRequestBtn.configurationUpdateHandler = { btn in
            
            var titleContainer = AttributeContainer()
            titleContainer.font = UIFont.boldSystemFont(ofSize: 20)

            btn.configuration?.attributedTitle = AttributedString( "Send a friend request", attributes: titleContainer)
            btn.configuration?.image = UIImage(systemName: "paperplane.fill")
            btn.configuration?.imagePadding = 10
            btn.configuration?.buttonSize = .large
            btn.configuration?.baseBackgroundColor = .systemBlue
            btn.addTarget(self, action: #selector(self.sendRequest(_:)), for: .touchUpInside)
        }
    }
    func setConstraints() {
        input_friendEamil.snp.makeConstraints { make in
            make.top.left.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.trailing.equalTo(view.safeAreaLayoutGuide).inset(20)
            make.bottom.equalTo(input_friendEamil.snp.top).offset(45)
        }
        sendRequestBtn.snp.makeConstraints { make in
            make.leading.trailing.equalTo(input_friendEamil)
            make.bottom.equalTo(view.keyboardLayoutGuide.snp.top).offset(-20)
        }
    }
    @objc func sendRequest(_ sender: UIButton) {
        
    }
    // 빈 화면 터치 시 키보드 내리기
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
            view.endEditing(true)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        tabBarController?.tabBar.isAppearedWithAnimation()
    }
}
// MARK: - TabBar Animation
extension UITabBar {
    func isHiddenWithAnimation() {
        let orig = self.frame
        var target = self.frame
        target.origin.x = target.origin.x - target.size.width
        UIView.animate(withDuration: 0.2, animations: {
            self.frame = target
        }) { (true) in
            self.isHidden = true
            self.frame = orig
        }
    }
    func isAppearedWithAnimation() {
        let orig = self.frame
        var target = self.frame
        target.origin.x = target.origin.x - target.size.width
        self.frame = target
        self.isHidden = false
        UIView.animate(withDuration: 0.3, animations: {
            self.frame = orig
        })
    }
}
// MARK: - 키보드 사라지게 하기
extension FriendRequestViewController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
}
// MARK: - canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct FriendRequestViewPreview: PreviewProvider {
    static var previews: some View {
        FriendRequestViewController().toPreview()
    }
}
