//
//  AccountViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/1/24.
//

import UIKit

class AccountViewController: UIViewController {

    let mainText = UILabel()
    let userInfo = UIButton()
    let signoutBtn = UIButton()
    let withdrawalBtn = UIButton()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setUpView()
        setUpValue()
        setConstraints()
        // Do any additional setup after loading the view.
    }
    func setUpView() {
        view.addSubview(mainText)
        view.addSubview(userInfo)
        view.addSubview(signoutBtn)
        view.addSubview(withdrawalBtn)
    }
    func setUpValue() {
        view.backgroundColor = .secondarySystemBackground
        
        mainText.text = "Account"
//        mainText.font = .preferredFont(forTextStyle: .largeTitle)
        mainText.font = UIFont.systemFont(ofSize: 35, weight: .bold)
        
        userInfo.configuration = UIButton.Configuration.filled()
        userInfo.configurationUpdateHandler = { btn in
            
            var titleContainer = AttributeContainer()
            titleContainer.font = UIFont.boldSystemFont(ofSize: 20)

            var subtitleContainer = AttributeContainer()
            subtitleContainer.foregroundColor = UIColor.secondaryLabel

            btn.configuration?.attributedTitle = AttributedString( UserManager.getData(type: String.self, forKey: .name) ?? "UserName", attributes: titleContainer)
            btn.configuration?.attributedSubtitle = AttributedString( UserManager.getData(type: String.self, forKey: .email) ?? "email@apple.com", attributes: subtitleContainer)
            
            btn.configuration?.image = UIImage(systemName: "person.crop.square")
            btn.configuration?.preferredSymbolConfigurationForImage = UIImage.SymbolConfiguration(pointSize: 40)
            btn.configuration?.imagePadding = 20
            btn.contentHorizontalAlignment = .leading
            btn.configuration?.contentInsets = .init(top: 30, leading: 20, bottom: 20, trailing: 20)
            btn.configuration?.titlePadding = 10
            
            btn.tintColor = .tertiarySystemBackground
            
            btn.addTarget(self, action: #selector(self.onPressUserInfo(_:)), for: .touchUpInside)
        }
        
        signoutBtn.configuration = UIButton.Configuration.filled()
        signoutBtn.configurationUpdateHandler = { btn in
            btn.configuration?.buttonSize = .large
            btn.configuration?.baseBackgroundColor = .systemRed
            btn.configuration?.title = "Sign out"
            btn.addTarget(self, action: #selector(self.onPressSignout(_:)), for: .touchUpInside)
        }
        
        withdrawalBtn.configuration = UIButton.Configuration.filled()
        withdrawalBtn.configurationUpdateHandler = { btn in
            btn.configuration?.buttonSize = .large
            btn.configuration?.baseBackgroundColor = .systemGray4
            btn.configuration?.title = "Withdrawal"
            btn.addTarget(self, action: #selector(self.onPressWithdrawal(_:)), for: .touchUpInside)
        }
    }
    func setConstraints() {
        mainText.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(45)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-20)
        }
        userInfo.snp.makeConstraints { make in
            make.top.equalTo(mainText.snp.bottom).offset(10)
            make.leading.equalTo(mainText.snp.leading)
            make.trailing.equalTo(mainText.snp.trailing)
        }
        signoutBtn.snp.makeConstraints { make in
            make.bottom.equalTo(withdrawalBtn.snp.top).offset(-30)
            make.leading.equalTo(mainText.snp.leading)
            make.trailing.equalTo(mainText.snp.trailing)
        }
        withdrawalBtn.snp.makeConstraints { make in
            make.bottom.equalTo(view.safeAreaLayoutGuide).inset(30)
            make.leading.equalTo(mainText.snp.leading)
            make.trailing.equalTo(mainText.snp.trailing)
        }
    }
    @objc func onPressUserInfo(_ sender: UIButton) {
        present(UINavigationController(rootViewController: EditProfileViewController()), animated: true)
    }
    @objc func onPressSignout(_ sender: UIButton) {
        signout()
    }
    @objc func onPressWithdrawal(_ sender: UIButton) {
        let alert = UIAlertController(title: "탈퇴하시겠습니까?", message: nil, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "취소", style: .cancel, handler: nil))
        alert.addAction(UIAlertAction(title: "확인", style: .default){ action in
            self.withdrawal()
        })
        self.present(alert, animated: true, completion: nil)
    }
}
// MARK: -  Sign out
extension AccountViewController {
    
    func signout() {
            
        guard let refreshtoken = UserManager.getData(type: String.self, forKey: .refreshToken) else { return }
    
        SigninSercive.shared.signout(refreshToken: refreshtoken) { response in
            switch response {
            case .success(let data):
                    
                guard let data = data as? String else { return }
                if data == "로그아웃 성공"{
                    self.dismiss(animated: true)
                    CallService.shared.signalClient.forceDisconnect()
                    UserManager.resetData()
                    FriendRequestList.shared.reset()
                } else {
                    let alert = UIAlertController(title: data, message: data, preferredStyle: .alert)
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
// MARK: -  Apply
extension AccountViewController {
    
    func withdrawal() {
            
        guard let email = UserManager.getData(type: String.self, forKey: .email) else { return }
        guard let password = UserManager.getData(type: String.self, forKey: .password) else { return }
        
        SigninSercive.shared.delete(email: email, password: password) { response in
            switch response {
            case .success(let data):
                    
                guard let data = data as? SigninDataResponse else {
                    print(data)
                    return
                }
                if data.status == "success"{
                    
                    let alert = UIAlertController(title: "탈퇴하였습니다.", message: nil, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "확인", style: .cancel){ action in
                        self.dismiss(animated: true)
                    })
                    self.present(alert, animated: true, completion: nil)
                    CallService.shared.signalClient.forceDisconnect()
                    UserManager.resetData()
                    FriendRequestList.shared.reset()
                    
                } else {
                    let alert = UIAlertController(title: data.status, message: data.message, preferredStyle: .alert)
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
struct AccountViewPreview: PreviewProvider {
    static var previews: some View {
        AccountViewController().toPreview()
    }
}
