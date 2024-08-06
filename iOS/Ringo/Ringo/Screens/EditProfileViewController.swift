//
//  EditProfileViewController.swift
//  Ringo
//
//  Created by 강진혁 on 8/5/24.
//

import UIKit

class EditProfileViewController: UIViewController {

    let scrollView = UIScrollView()
    let contentView = UIView()
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
    let editBtn = UIButton()
    let applyBtn = UIButton()
    
    var boolConfirm: Bool = false
    var selectedLang: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpView()
        setUpValue()
        setConstraints()
    }
    func setUpView(){
        view.addSubview(scrollView)
        scrollView.addSubview(contentView)
        
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
        
        contentView.addSubview(editBtn)
        contentView.addSubview(applyBtn)
    }
    func setUpValue(){
        email.text = "E-mail"
        email.font = .preferredFont(forTextStyle: .body)
        
        input_email.text = UserManager.getData(type: String.self, forKey: .email) ?? "email"
        input_email.borderStyle = .roundedRect
        input_email.keyboardType = .emailAddress
        input_email.autocapitalizationType = .none
        input_email.autocorrectionType = .no
        input_email.spellCheckingType = .no
        input_email.clearButtonMode = .whileEditing
        input_email.backgroundColor = .systemGray6
        input_email.delegate = self
//        input_email.addTarget(self, action: #selector(checkEmail(_:)), for: .editingDidEnd)
        input_email.isEnabled = false
        
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
        
        input_passwd.text = UserManager.getData(type: String.self, forKey: .password) ?? "password"
        input_passwd.borderStyle = .roundedRect
        input_passwd.isSecureTextEntry = true
        input_passwd.rightView = showBtn
        input_passwd.rightViewMode = .always
        input_passwd.autocapitalizationType = .none
        input_passwd.delegate = self
        input_passwd.autocorrectionType = .no
        input_passwd.spellCheckingType = .no
        input_passwd.backgroundColor = .systemGray6
        input_passwd.isEnabled = false
        
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
        input_cfpw.isEnabled = false
        
        error2.isHidden = true
        error2.setTitle(" Passwords do not match.", for: .normal)
        error2.setTitleColor(.red, for: .normal)
        error2.titleLabel?.font = .systemFont(ofSize: 13)
        error2.setImage(UIImage(systemName: "info.circle"), for: .normal)
        error2.setImage(UIImage(systemName: "info.circle"), for: .highlighted)
        error2.tintColor = .red
        
        name.text = "User Name"
        name.font = .preferredFont(forTextStyle: .body)
        
        input_name.text = UserManager.getData(type: String.self, forKey: .name) ?? "name"
        input_name.borderStyle = .roundedRect
        input_name.clearButtonMode = .whileEditing
        input_name.autocapitalizationType = .none
        input_name.delegate = self
        input_name.autocorrectionType = .no
        input_name.spellCheckingType = .no
        input_name.backgroundColor = .systemGray6
        input_name.isEnabled = false
        
        language.text = "Language"
        language.font = .preferredFont(forTextStyle: .body)
        
        input_language.text = UserManager.getData(type: String.self, forKey: .language) ?? "language"
        input_language.borderStyle = .roundedRect
        input_language.delegate = self
        input_language.tintColor = .clear
        input_language.backgroundColor = .systemGray6
        input_language.isEnabled = false
        
        input_language.inputView = pickerLang
        input_language.inputAccessoryView = toolbar
        
        pickerLang.delegate = self
        pickerLang.dataSource = self
        
        toolbar.sizeToFit()
        let space = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        let button = UIBarButtonItem(title: "Done", style: .plain, target: self, action: #selector(view.endEditing(_:)))
        toolbar.setItems([space,button], animated: true)
        toolbar.isUserInteractionEnabled = true
        
        editBtn.configuration = UIButton.Configuration.tinted()
        editBtn.configurationUpdateHandler = { btn in
            btn.configuration?.buttonSize = .large
            btn.configuration?.baseBackgroundColor = .systemBlue
            btn.addTarget(self, action: #selector(self.onPressEditBtn(_:)), for: .touchUpInside)
            switch btn.state {
            case .selected:
                btn.configuration?.title = "Editing..."
                btn.configuration?.baseBackgroundColor = .systemBlue.withAlphaComponent(0.7)
                self.input_email.isEnabled = true
                self.input_passwd.isEnabled = true
                self.input_cfpw.isEnabled = true
                self.input_name.isEnabled = true
                self.input_language.isEnabled = true
                self.applyBtn.isEnabled = false
            default:
                btn.configuration?.title = "Edit"
                self.input_email.isEnabled = false
                self.input_passwd.isEnabled = false
                self.input_cfpw.isEnabled = false
                self.input_name.isEnabled = false
                self.input_language.isEnabled = false
                self.checkAll()
            }
        }
        applyBtn.configuration = UIButton.Configuration.filled()
        applyBtn.configurationUpdateHandler = { btn in
            btn.configuration?.title = "Apply"
            btn.configuration?.buttonSize = .large
            btn.configuration?.baseBackgroundColor = .systemBlue
            btn.addTarget(self, action: #selector(self.onPressApplyBtn(_:)), for: .touchUpInside)
        }
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
        email.snp.makeConstraints { make in
            make.top.equalTo(contentView.safeAreaLayoutGuide).offset(30)
            make.leading.equalTo(contentView.safeAreaLayoutGuide).offset(20)
            make.trailing.equalTo(contentView.safeAreaLayoutGuide).offset(-20)
        }
        input_email.snp.makeConstraints { make in
            make.top.equalTo(email.snp.bottom).offset(10)
            make.leading.equalTo(email.snp.leading)
            make.trailing.equalTo(email.snp.trailing)
        }
        stackView.snp.makeConstraints { make in
            make.top.equalTo(input_email.snp.bottom).offset(30)
            make.leading.equalTo(input_email.snp.leading)
            make.trailing.equalTo(input_email.snp.trailing)
        }
        input_passwd.snp.makeConstraints { make in
            make.trailing.equalTo(stackView.snp.trailing)
        }
        stackView2.snp.makeConstraints { make in
            make.top.equalTo(stackView.snp.bottom).offset(30)
            make.leading.equalTo(stackView.snp.leading)
            make.trailing.equalTo(stackView.snp.trailing)
        }
        input_cfpw.snp.makeConstraints { make in
            make.trailing.equalTo(stackView2.snp.trailing)
        }
        name.snp.makeConstraints { make in
            make.top.equalTo(stackView2.snp.bottom).offset(30)
            make.leading.equalTo(input_cfpw.snp.leading)
            make.trailing.equalTo(input_cfpw.snp.trailing)
        }
        input_name.snp.makeConstraints { make in
            make.top.equalTo(name.snp.bottom).offset(10)
            make.leading.equalTo(name.snp.leading)
            make.trailing.equalTo(name.snp.trailing)
        }
        language.snp.makeConstraints { make in
            make.top.equalTo(input_name.snp.bottom).offset(30)
            make.leading.equalTo(input_name.snp.leading)
            make.trailing.equalTo(input_name.snp.trailing)
        }
        input_language.snp.makeConstraints { make in
            make.top.equalTo(language.snp.bottom).offset(10)
            make.leading.equalTo(language.snp.leading)
            make.trailing.equalTo(language.snp.trailing)
        }
        editBtn.snp.makeConstraints { make in
            make.top.equalTo(input_language.snp.bottom).offset(50)
            make.leading.equalTo(input_language.snp.leading)
            make.trailing.equalTo(view.snp.centerX).offset(-10)
        }
        applyBtn.snp.makeConstraints { make in
            make.top.equalTo(input_language.snp.bottom).offset(50)
            make.leading.equalTo(view.snp.centerX).offset(10)
            make.trailing.equalTo(input_language.snp.trailing)
            make.bottom.equalToSuperview().inset(30)
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
    func checkAll() {
        if !(input_email.text?.isEmpty ?? true) && !(input_passwd.text?.isEmpty ?? true) && boolConfirm && !(input_name.text?.isEmpty ?? true) &&  !(input_language.text?.isEmpty ?? true) {
            applyBtn.isEnabled = true
        } else {
            applyBtn.isEnabled = false
        }
    }
    @objc func onPressEditBtn(_ sender: UIButton){
        editBtn.isSelected.toggle()
    }
    @objc func onPressApplyBtn(_ sender: UIButton){
        
    }
}
// MARK: - 키보드 사라지게 하기
extension EditProfileViewController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
}
// MARK: - PickerView
extension EditProfileViewController: UIPickerViewDelegate, UIPickerViewDataSource {
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
// MARK: - canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct EditProfileViewPreview: PreviewProvider {
    static var previews: some View {
        EditProfileViewController().toPreview()
    }
}
