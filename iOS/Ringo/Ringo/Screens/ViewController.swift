//
//  ViewController.swift
//  Ringo
//
//  Created by 강진혁 on 1/29/24.
//

import UIKit
import SnapKit

class ViewController: UIViewController {

    let Text1 = UILabel()
    let Text2 = UILabel()
    let Text3 = UILabel()
    let signupBtn = UIButton()
    let email = UILabel()
    let input_email = UITextField()
    let passwd = UILabel()
    let input_passwd = UITextField()
    var showBtn = UIButton()
    let forgotPwBtn = UIButton()
    let signinBtn = UIButton()
    let Text4 = UILabel()
    let google = UIButton()
    let apple = UIButton()
    let facebook = UIButton()
    let error = UIButton(type: .custom)
    let stackView = UIStackView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setUpValue()
        setUpView()
        setConstraints()
    }
    
    func setUpView() {
        view.addSubview(Text1)
        view.addSubview(Text2)
        view.addSubview(Text3)
        view.addSubview(signupBtn)
        view.addSubview(email)
        view.addSubview(input_email)
        view.addSubview(forgotPwBtn)
        view.addSubview(signinBtn)
        view.addSubview(Text4)
        view.addSubview(google)
        view.addSubview(apple)
        view.addSubview(facebook)
        
        view.addSubview(stackView)
        stackView.addArrangedSubview(passwd)
        stackView.addArrangedSubview(input_passwd)
        stackView.addArrangedSubview(error)
    }
    
    func setUpValue() {
        view.backgroundColor = .systemBackground
        let button_plain = UIButton.Configuration.plain()
        let button_filled = UIButton.Configuration.filled()
        
        Text1.text = "Sign in"
        Text1.font = .preferredFont(forTextStyle: .title1)
        
        Text2.text = "If you don't have an account register \nYou can"
        Text2.font = .preferredFont(forTextStyle: .body)
        
        Text3.text = "You can"
        Text3.font = .preferredFont(forTextStyle: .body)
        
        signupBtn.setTitle("Sign up here!", for: .normal)
        signupBtn.setTitleColor(.systemBlue, for: .normal)
        signupBtn.titleLabel?.font = .systemFont(ofSize: 18)
        signupBtn.setTitleColor(.systemBlue.withAlphaComponent(0.5), for: .highlighted)
        
        email.text = "E-mail"
        email.font = .preferredFont(forTextStyle: .body)
        
        input_email.placeholder = "Enter your email"
        input_email.borderStyle = .roundedRect
        input_email.layer.borderWidth = 1.5
        input_email.layer.borderColor = UIColor(hexCode: "E2E8F0").cgColor
        input_email.layer.cornerRadius = 5
        input_email.keyboardType = .emailAddress
        input_email.autocapitalizationType = .none
        input_email.autocorrectionType = .no
        input_email.spellCheckingType = .no
        
        stackView.axis = .vertical
        stackView.alignment = .fill
        stackView.distribution = .equalSpacing
        stackView.spacing = 10
        
        passwd.text = "Password"
        passwd.font = .preferredFont(forTextStyle: .body)
        
        showBtn = UIButton.init(primaryAction: UIAction(handler: { _ in
            self.input_passwd.isSecureTextEntry.toggle()
            self.showBtn.isSelected.toggle()
        }))
        
        showBtn.configuration = button_plain
        showBtn.configuration?.baseBackgroundColor = .clear
        showBtn.configurationUpdateHandler = { btn in
            switch btn.state {
                case .selected:
                    btn.configuration?.image = UIImage(systemName: "eye")
                default:
                    btn.configuration?.image = UIImage(systemName: "eye.slash")
            }
        }
        
        input_passwd.placeholder = "Enter your password"
        input_passwd.borderStyle = .roundedRect
        input_passwd.isSecureTextEntry = true
        input_passwd.rightView = showBtn
        input_passwd.rightViewMode = .always
        input_passwd.autocapitalizationType = .none
        input_passwd.delegate = self
        input_passwd.autocorrectionType = .no
        input_passwd.spellCheckingType = .no
        
        error.layer.isHidden = true
        error.setTitle(" Incorrect password. Please check your password.", for: .normal)
        error.setTitleColor(.red, for: .normal)
        error.titleLabel?.font = .systemFont(ofSize: 13)
        error.setImage(UIImage(systemName: "info.circle"), for: .normal)
        error.setImage(UIImage(systemName: "info.circle"), for: .highlighted)
        error.tintColor = .red
 
        forgotPwBtn.setTitle("Forgot Password?", for: .normal)
        forgotPwBtn.setTitleColor(.systemBlue, for: .normal)
        forgotPwBtn.titleLabel?.font = .systemFont(ofSize: 13)
        forgotPwBtn.setTitleColor(.systemBlue.withAlphaComponent(0.5), for: .highlighted)
        
        signinBtn.configuration = button_filled
        signinBtn.configuration?.buttonSize = .large
        signinBtn.setTitle("Sign in", for: .normal)
        signinBtn.layer.shadowColor = UIColor.systemBlue.cgColor
        signinBtn.layer.shadowRadius = 15
        signinBtn.layer.shadowOpacity = 0.5
        signinBtn.layer.shadowOffset = CGSize(width: 0, height: 0)
        signinBtn.layer.masksToBounds = false
        signinBtn.addTarget(self, action: #selector(onPressSignin(_:)), for: .touchUpInside)
//        signinBtn.addAction(UIAction{_ in self.login()}, for: .touchUpInside)
    
        Text4.text = "or continue with"
        Text4.font = .preferredFont(forTextStyle: .body)
        Text4.textColor = .lightGray
        
        google.setImage(UIImage(named: "Google"), for: .normal)
        apple.setImage(UIImage(named: "Apple"), for: .normal)
        facebook.setImage(UIImage(named: "Facebook"), for: .normal)
    }
    
    func setConstraints(){
        Text1.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(30)
            make.leading.equalToSuperview().offset(30)
        }
        
        Text2.snp.makeConstraints { make in
            make.top.equalTo(Text1.snp.bottom).offset(20)
            make.leading.equalTo(Text1.snp.leading)
        }
        
        Text3.snp.makeConstraints { make in
            make.top.equalTo(Text2.snp.bottom).offset(10)
            make.leading.equalTo(Text2.snp.leading)
        }
        
        signupBtn.snp.makeConstraints { make in
            make.top.equalTo(Text2.snp.top).offset(24)
            make.leading.equalTo(Text2.snp.leading).offset(70)
        }
        
        email.snp.makeConstraints { make in
            make.top.equalTo(Text2.snp.bottom).offset(100)
            make.leading.equalToSuperview().offset(20)
        }
        
        input_email.snp.makeConstraints { make in
            make.top.equalTo(email.snp.bottom).offset(10)
            make.leading.equalTo(email.snp.leading)
            make.trailing.equalToSuperview().offset(-20)
        }
        
        stackView.snp.makeConstraints { make in
            make.top.equalTo(input_email.snp.bottom).offset(20)
            make.leading.equalTo(email.snp.leading)
            make.trailing.equalTo(input_email.snp.trailing)
        }
     
        forgotPwBtn.snp.makeConstraints { make in
            make.top.equalTo(stackView.snp.bottom).offset(10)
            make.trailing.equalTo(stackView.snp.trailing)
        }
        
        signinBtn.snp.makeConstraints { make in
            make.top.equalTo(input_passwd.snp.bottom).offset(90)
            make.leading.equalTo(stackView.snp.leading)
            make.trailing.equalTo(stackView.snp.trailing)
        }
        
        Text4.snp.makeConstraints { make in
            make.bottom.equalTo(view.safeAreaLayoutGuide).offset(-110)
            make.centerX.equalToSuperview()
        }
        
        google.snp.makeConstraints { make in
            make.top.equalTo(Text4.snp.bottom).offset(20)
            make.leading.equalTo(apple.snp.trailing).offset(20)
        }
        
        apple.snp.makeConstraints { make in
            make.top.equalTo(Text4.snp.bottom).offset(20)
            make.centerX.equalToSuperview()
        }
        
        facebook.snp.makeConstraints { make in
            make.top.equalTo(Text4.snp.bottom).offset(20)
            make.trailing.equalTo(apple.snp.leading).offset(-20)
        }
    }
    // 빈 화면 터치 시 키보드 내리기
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
            view.endEditing(true)
        }
    
    @objc func onPressSignin(_ sender: UIButton) {
        login()
    }
}

// MARK: - canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct ViewPreview: PreviewProvider {
    static var previews: some View {
        ViewController().toPreview()
    }
}
// MARK: -  hexcode로 색 지정하기
extension UIColor {
    
    convenience init(hexCode: String, alpha: CGFloat = 1.0) {
        var hexFormatted: String = hexCode.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines).uppercased()
        
        if hexFormatted.hasPrefix("#") {
            hexFormatted = String(hexFormatted.dropFirst())
        }
        
        assert(hexFormatted.count == 6, "Invalid hex code used.")
        
        var rgbValue: UInt64 = 0
        Scanner(string: hexFormatted).scanHexInt64(&rgbValue)
        
        self.init(red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
                  green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
                  blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
                  alpha: alpha)
    }
}
// MARK: -  Sign in
extension ViewController {
    
    func login() {
            
        guard let email = input_email.text else { return }
        guard let passward = input_passwd.text else { return }
        
        SigninSercive.shared.login(email: email, password: passward) { response in
            switch response {
            case .success(let data):
                    
                guard let data = data as? String else { return }
                if data == "success"{
//                    UserDefaults.standard.set(data.data?.jwtToken, forKey: "jwtToken")
                    let nav = UINavigationController()
                    nav.modalPresentationStyle = .fullScreen
                    
                    //네비게이션 중복 수정 1/31
                    nav.navigationBar.isHidden = true
                        
                    let controller = TabBarViewController()
                    nav.viewControllers = [controller]
                    self.present(nav, animated: true, completion: nil)
                } else {
                    let alert = UIAlertController(title: data, message: "", preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "확인", style: .cancel, handler: nil))
            //            alert.addAction(UIAlertAction(title: "DEFAULT", style: .default, handler: nil))
            //            alert.addAction(UIAlertAction(title: "DESTRUCTIVE", style: .destructive, handler: nil))
                        
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
            }
        }
    }
}
// MARK: - canvas 이용하기
extension ViewController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
}
