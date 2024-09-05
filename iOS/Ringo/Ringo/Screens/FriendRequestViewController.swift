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
        
        self.navigationItem.title = "Request Friend"
        
        input_friendEamil.becomeFirstResponder()
        
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
            btn.addTarget(self, action: #selector(self.onPressSendBtn(_:)), for: .touchUpInside)
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
    @objc func onPressSendBtn(_ sender: UIButton) {
        print("btn press")
        sendFriendRequest()
    }
    // 빈 화면 터치 시 키보드 내리기
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
            view.endEditing(true)
    }
}
// MARK: - 키보드 사라지게 하기
extension FriendRequestViewController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
}
// MARK: - firend request
extension FriendRequestViewController {
    
    func sendFriendRequest() {
            
        guard let sender = UserManager.getData(type: String.self, forKey: .email) else { return }
        guard let receiver = input_friendEamil.text else {
            print("receiver empty")
            return }
        print("send request")
        FriendService.shared.sendFriendRequest(sender: sender, receiver: receiver) { response in
            switch response {
            case .success(let data):
                    
                guard let data = data as? String else { return }
                if data == "{\"message\":\"success\"}" {
                    let alert = UIAlertController(title: "요청을 보냈습니다.", message: nil, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "확인", style: .cancel, handler: { action in
                        self.dismiss(animated: true)
                    }))
                    self.present(alert, animated: true, completion: nil)
                    print(data)
                } else {
                    let alert = UIAlertController(title: "존재하지 않은 이메일입니다.", message: nil, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "확인", style: .cancel, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    print(data)
                    }
                    
            case .requestErr(let err):
                print(err)
            case .pathErr:
                print("pathErr")
            case .serverErr:
                print("serverErr")
            case .networkFail:
                print("networkFail")
            case .dataErr:
                print("dataErr")
            }
        }
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
