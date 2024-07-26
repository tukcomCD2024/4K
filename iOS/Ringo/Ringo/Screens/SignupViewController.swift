//
//  SignupViewController.swift
//  Ringo
//
//  Created by 강진혁 on 7/22/24.
//

import UIKit
import SnapKit

class SignupViewController: UIViewController {
    
    let scrollView = UIScrollView()
    let contentView = UIView()
    let Text1 = UILabel()
    let Text2 = UILabel()
    let Text3 = UILabel()
    let signinBtn = UIButton()
    let email = UILabel()
    let input_email = UITextField()
    let passwd = UILabel()
    let input_passwd = UITextField()
    var showBtn = UIButton()
    let error = UIButton(type: .custom)
    let stackView = UIStackView()
    let confirmPw = UILabel()
    let input_cfpw = UITextField()
    let error2 = UIButton(type: .custom)
    let stackView2 = UIStackView()
    let name = UILabel()
    let input_name = UITextField()
    let language = UILabel()
    let input_language = UITextField()
    let pickerLang = UIPickerView()
    let toolbar = UIToolbar()
    let signupBtn = UIButton()
    
    var boolConfirm: Bool = false
    var selectedLang: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setUpView()
        setUpValue()
        setConstraints()
        // Do any additional setup after loading the view.
    }

    func setUpView(){
        
        view.addSubview(scrollView)
        scrollView.addSubview(contentView)
        
        contentView.addSubview(Text1)
        contentView.addSubview(Text2)
        contentView.addSubview(Text3)
        contentView.addSubview(signinBtn)
        contentView.addSubview(email)
        contentView.addSubview(input_email)
        
        contentView.addSubview(stackView)
        stackView.addArrangedSubview(passwd)
        stackView.addArrangedSubview(input_passwd)
        stackView.addArrangedSubview(error)
        
        contentView.addSubview(stackView2)
        stackView2.addArrangedSubview(confirmPw)
        stackView2.addArrangedSubview(input_cfpw)
        stackView2.addArrangedSubview(error2)
        
        contentView.addSubview(name)
        contentView.addSubview(input_name)
        
        contentView.addSubview(language)
        contentView.addSubview(input_language)
        
        contentView.addSubview(signupBtn)
    }
    
    func setUpValue(){
        view.backgroundColor = .systemBackground

        //스크롤뷰는 스크롤때문에 한 번 터치 무시, 평소에 썼던 빈 곳 터치시 키보드 내리기는 불가, 따로 추가 설정해야 함
        let tap = UITapGestureRecognizer(target: view, action: #selector(UIView.endEditing))
        tap.cancelsTouchesInView = false
        tap.addTarget(self, action: #selector(checkAll))
        scrollView.addGestureRecognizer(tap)
        
        Text1.text = "Sign up"
        Text1.font = .preferredFont(forTextStyle: .title1)
        
        Text2.text = "If you already have an account register"
        Text2.font = .preferredFont(forTextStyle: .body)
        
        Text3.text = "You can"
        Text3.font = .preferredFont(forTextStyle: .body)
        
        signinBtn.setTitle("Sign in here!", for: .normal)
        signinBtn.setTitleColor(.systemBlue, for: .normal)
        signinBtn.titleLabel?.font = .systemFont(ofSize: 18)
        signinBtn.setTitleColor(.systemBlue.withAlphaComponent(0.5), for: .highlighted)
        signinBtn.addTarget(self, action: #selector(onPressSignin(_:)), for: .touchUpInside)
        
        email.text = "E-mail"
        email.font = .preferredFont(forTextStyle: .body)
        
        input_email.placeholder = "Enter your email"
        input_email.borderStyle = .roundedRect
        input_email.keyboardType = .emailAddress
        input_email.autocapitalizationType = .none
        input_email.autocorrectionType = .no
        input_email.spellCheckingType = .no
        input_email.clearButtonMode = .always
        input_email.backgroundColor = .systemGray6
        input_email.delegate = self
//        input_email.addTarget(self, action: #selector(checkEmail(_:)), for: .editingDidEnd)
        
        stackView.axis = .vertical
        stackView.alignment = .leading
        stackView.distribution = .equalSpacing
        stackView.spacing = 10
        
        passwd.text = "Password"
        passwd.font = .preferredFont(forTextStyle: .body)
        
        showBtn = UIButton.init(primaryAction: UIAction(handler: { _ in
            self.input_passwd.isSecureTextEntry.toggle()
            self.showBtn.isSelected.toggle()
        }))
        
        showBtn.configuration = UIButton.Configuration.plain()
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
        input_passwd.backgroundColor = .systemGray6
        
        error.layer.isHidden = true
        error.setTitle(" Invaild password. Please follow the password rules.", for: .normal)
        error.setTitleColor(.red, for: .normal)
        error.titleLabel?.font = .systemFont(ofSize: 13)
        error.setImage(UIImage(systemName: "info.circle"), for: .normal)
        error.setImage(UIImage(systemName: "info.circle"), for: .highlighted)
        error.tintColor = .red
        
        stackView2.axis = .vertical
        stackView2.alignment = .leading
        stackView2.distribution = .equalSpacing
        stackView2.spacing = 10
        
        confirmPw.text = "Confirm Password"
        confirmPw.font = .preferredFont(forTextStyle: .body)
        
        input_cfpw.placeholder = "Enter password accurately"
        input_cfpw.borderStyle = .roundedRect
        input_cfpw.isSecureTextEntry = true
        input_cfpw.autocapitalizationType = .none
        input_cfpw.delegate = self
        input_cfpw.autocorrectionType = .no
        input_cfpw.spellCheckingType = .no
        input_cfpw.clearButtonMode = .always
        input_cfpw.backgroundColor = .systemGray6
        input_cfpw.addTarget(self, action: #selector(checkPasswd(_:)), for: .editingDidEnd)
        
        error2.isHidden = true
        error2.setTitle(" Passwords do not match.", for: .normal)
        error2.setTitleColor(.red, for: .normal)
        error2.titleLabel?.font = .systemFont(ofSize: 13)
        error2.setImage(UIImage(systemName: "info.circle"), for: .normal)
        error2.setImage(UIImage(systemName: "info.circle"), for: .highlighted)
        error2.tintColor = .red
        
        name.text = "User Name"
        name.font = .preferredFont(forTextStyle: .body)
        
        input_name.placeholder = "Enter your name"
        input_name.borderStyle = .roundedRect
        input_name.clearButtonMode = .always
        input_name.autocapitalizationType = .none
        input_name.delegate = self
        input_name.autocorrectionType = .no
        input_name.spellCheckingType = .no
        input_name.backgroundColor = .systemGray6
        
        language.text = "Language"
        language.font = .preferredFont(forTextStyle: .body)
        
        input_language.placeholder = "Choose your language"
        input_language.borderStyle = .roundedRect
        input_language.delegate = self
        input_language.tintColor = .clear
        input_language.backgroundColor = .systemGray6
        
        input_language.inputView = pickerLang
        input_language.inputAccessoryView = toolbar
        
        pickerLang.delegate = self
        pickerLang.dataSource = self
        
        toolbar.sizeToFit()
        let space = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        let button = UIBarButtonItem(title: "Done", style: .plain, target: self, action: #selector(selectLang(_:)))
        toolbar.setItems([space,button], animated: true)
        toolbar.isUserInteractionEnabled = true
        
        signupBtn.configuration = UIButton.Configuration.filled()
        signupBtn.configuration?.buttonSize = .large
        signupBtn.setTitle("Sign up", for: .normal)
        signupBtn.layer.shadowColor = UIColor.systemBlue.cgColor
        signupBtn.layer.shadowRadius = 15
        signupBtn.layer.shadowOpacity = 0.5
        signupBtn.layer.shadowOffset = CGSize(width: 0, height: 0)
        signupBtn.layer.masksToBounds = false
        signupBtn.addTarget(self, action: #selector(onPressSignup(_:)), for: .touchUpInside)
        signupBtn.isEnabled = false
    }
    
    func setConstraints(){
        
        scrollView.snp.makeConstraints { make in
            make.top.leading.trailing.equalTo(view.safeAreaLayoutGuide)
            make.bottom.equalTo(view.keyboardLayoutGuide.snp.top) // 키보드 크기 고려
        }
        contentView.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.centerX.top.bottom.equalToSuperview()
        }
        
        Text1.snp.makeConstraints { make in
            make.top.equalTo(contentView.safeAreaLayoutGuide).offset(30)
            make.leading.equalTo(contentView.safeAreaLayoutGuide).offset(30)
        }
        Text2.snp.makeConstraints { make in
            make.top.equalTo(Text1.snp.bottom).offset(20)
            make.leading.equalTo(Text1.snp.leading)
        }
        Text3.snp.makeConstraints { make in
            make.top.equalTo(Text2.snp.bottom).offset(10)
            make.leading.equalTo(Text2.snp.leading)
        }
        signinBtn.snp.makeConstraints { make in
            make.top.equalTo(Text2.snp.top).offset(24)
            make.leading.equalTo(Text2.snp.leading).offset(70)
        }
        
        email.snp.makeConstraints { make in
            make.top.equalTo(Text2.snp.bottom).offset(100)
            make.leading.equalTo(contentView.safeAreaLayoutGuide).offset(20)
        }
        input_email.snp.makeConstraints { make in
            make.top.equalTo(email.snp.bottom).offset(10)
            make.leading.equalTo(email.snp.leading)
            make.trailing.equalTo(contentView.safeAreaLayoutGuide).offset(-20)
        }
        
        stackView.snp.makeConstraints { make in
            make.top.equalTo(input_email.snp.bottom).offset(20)
            make.leading.equalTo(input_email.snp.leading)
            make.trailing.equalTo(input_email.snp.trailing)
        }
        input_passwd.snp.makeConstraints { make in
            make.trailing.equalTo(stackView.snp.trailing)
        }
        
        stackView2.snp.makeConstraints { make in
            make.top.equalTo(stackView.snp.bottom).offset(20)
            make.leading.equalTo(stackView.snp.leading)
            make.trailing.equalTo(stackView.snp.trailing)
        }
        input_cfpw.snp.makeConstraints { make in
            make.trailing.equalTo(stackView2.snp.trailing)
        }
        
        name.snp.makeConstraints { make in
            make.top.equalTo(stackView2.snp.bottom).offset(20)
            make.leading.equalTo(contentView.safeAreaLayoutGuide).offset(20)
        }
        input_name.snp.makeConstraints { make in
            make.top.equalTo(name.snp.bottom).offset(10)
            make.leading.equalTo(name.snp.leading)
            make.trailing.equalTo(contentView.safeAreaLayoutGuide).offset(-20)
        }
        
        language.snp.makeConstraints { make in
            make.top.equalTo(input_name.snp.bottom).offset(20)
            make.leading.equalTo(contentView.safeAreaLayoutGuide).offset(20)
        }
        input_language.snp.makeConstraints { make in
            make.top.equalTo(language.snp.bottom).offset(10)
            make.leading.equalTo(language.snp.leading)
            make.trailing.equalTo(contentView.safeAreaLayoutGuide).offset(-20)
        }
        
        signupBtn.snp.makeConstraints { make in
            make.top.equalTo(input_language.snp.bottom).offset(90)
            make.leading.equalTo(input_language.snp.leading)
            make.trailing.equalTo(input_language.snp.trailing)
            make.bottom.equalToSuperview().inset(30) //scrollview contentview의 높이를 지정함
        }
    }
    
    @objc func checkEmail(_ sender: UITextField) {
        let regex = "^([a-zA-Z0-9._-])+@[a-zA-Z0-9.-]+.[a-zA-Z]$"
        if ( sender.text?.range(of: regex,options: .regularExpression) != nil ) {
            input_email.layer.borderColor = UIColor.clear.cgColor
        } else {
            input_email.layer.borderWidth = 1
            input_email.layer.cornerRadius = 5
            input_email.layer.borderColor = UIColor.systemRed.cgColor
        }
    }
    func isSamePasswd(_ first: UITextField, _ second: UITextField) -> Bool {
        if(first.text == second.text) {
            return true
        } else {
            return false
        }
    }
    @objc func checkPasswd(_ sender: UITextField) {
        if isSamePasswd(input_passwd, input_cfpw){
            input_cfpw.layer.borderColor = UIColor.clear.cgColor
            error2.isHidden = true
            boolConfirm = true
        } else {
            input_cfpw.layer.borderWidth = 1
            input_cfpw.layer.cornerRadius = 5
            input_cfpw.layer.borderColor = UIColor.systemRed.cgColor
            error2.isHidden = false
            boolConfirm = false
        }
    }
    
    @objc func onPressSignin(_ sender: UIButton) {
        dismiss(animated: true)
    }
    
    @objc func selectLang(_ sender: UIButton) {
        if input_language.text?.isEmpty ?? false {
            pickerView(pickerLang, didSelectRow: 0, inComponent: 0)
        }
        input_language.resignFirstResponder()
        checkAll()
    }
    @objc func checkAll() {
        if !(input_email.text?.isEmpty ?? true) && !(input_passwd.text?.isEmpty ?? true) && boolConfirm && !(input_name.text?.isEmpty ?? true) &&  !(input_language.text?.isEmpty ?? true) {
            signupBtn.isEnabled = true
        } else {
            signupBtn.isEnabled = false
        }
    }
    @objc func onPressSignup(_ sender: UIButton) {
        signup()
    }
}

// MARK: - 키보드 사라지게 하기
extension SignupViewController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        checkAll()
        return true
    }
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        signupBtn.isEnabled = false
        return true
    }
    func textFieldDidChangeSelection(_ textField: UITextField) {
        signupBtn.isEnabled = false
    }
}

// MARK: - PickerView
extension SignupViewController: UIPickerViewDelegate, UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return Language.shared.getCount()
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        let languages = Language.shared.getList()
        return Language.shared.getLanguageByCode(key: languages[row])
    }
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        let languages = Language.shared.getList()
        input_language.text = Language.shared.getLanguageByCode(key: languages[row])
        selectedLang = languages[row]
    }
}
// MARK: -  Sign up
extension SignupViewController {
    
    func signup() {
            
        guard let email = input_email.text else { return }
        guard let password = input_passwd.text else { return }
        guard let name = input_name.text else { return }
        guard let languageCode = selectedLang else { return }
//        let languageCode = Language.shared.getCode(key: language)
        
        print(email,password,name,languageCode)
        
        SigninSercive.shared.signup(email: email, password: password, name: name, languageCode: languageCode) { response in
            switch response {
            case .success(let data):
                    
                guard let data = data as? SigninDataResponse else { return }
                if data.status == "success"{
                    
                    let alert = UIAlertController(title: data.status, message: data.message, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "회원가입 성공", style: .cancel){ action in
                        self.dismiss(animated: true)
                    })
                    self.present(alert, animated: true, completion: nil)
                    
                } else {
                    let alert = UIAlertController(title: data.status, message: data.message, preferredStyle: .alert)
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
            case .dataErr:
                print("dataErr")
            }
        }
    }
}
// MARK: - canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct SignupViewPreview: PreviewProvider {
    static var previews: some View {
        SignupViewController().toPreview()
    }
}
