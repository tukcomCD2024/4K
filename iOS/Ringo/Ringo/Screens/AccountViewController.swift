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
    let signout = UIButton()
    let withdrawal = UIButton()
    
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
        view.addSubview(signout)
        view.addSubview(withdrawal)
    }
    func setUpValue() {
        view.backgroundColor = .secondarySystemBackground
        
        mainText.text = "Account"
//        mainText.font = .preferredFont(forTextStyle: .largeTitle)
        mainText.font = UIFont.boldSystemFont(ofSize: 35)
        
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
//            btn.configuration?.buttonSize = .large
            
            btn.tintColor = .tertiarySystemBackground
        }
        
        signout.configuration = UIButton.Configuration.filled()
        signout.configurationUpdateHandler = { btn in
            btn.configuration?.buttonSize = .large
            btn.configuration?.baseBackgroundColor = .systemRed
            btn.configuration?.title = "Sign out"
            btn.addTarget(self, action: #selector(self.signout(_:)), for: .touchUpInside)
        }
        
        withdrawal.configuration = UIButton.Configuration.filled()
        withdrawal.configurationUpdateHandler = { btn in
            btn.configuration?.buttonSize = .large
            btn.configuration?.baseBackgroundColor = .systemGray4
            btn.configuration?.title = "Withdrawal"
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
        signout.snp.makeConstraints { make in
            make.bottom.equalTo(withdrawal.snp.top).offset(-30)
            make.leading.equalTo(mainText.snp.leading)
            make.trailing.equalTo(mainText.snp.trailing)
        }
        withdrawal.snp.makeConstraints { make in
            make.bottom.equalTo(view.safeAreaLayoutGuide).inset(30)
            make.leading.equalTo(mainText.snp.leading)
            make.trailing.equalTo(mainText.snp.trailing)
        }
    }
    @objc func signout(_ sender: UIButton) {
        dismiss(animated: true)
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
