//
//  ViewController.swift
//  Ringo
//
//  Created by 강진혁 on 1/29/24.
//

import UIKit
import SnapKit

class ViewController: UIViewController {

    let sampleLabel = UILabel()
    let Text1 = UILabel()
    let Text2 = UILabel()
    let email = UILabel()
    let input_email = UITextField()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setUpValue()
        setUpView()
        setConstraints()
    }
    func setUpValue() {
        view.backgroundColor = .white
        
        sampleLabel.text = "스토리보드 없이 구현한 ViewController"
        sampleLabel.font = .preferredFont(forTextStyle: .title3)
        
        Text1.text = "Sign in"
        Text1.font = .preferredFont(forTextStyle: .title1)
        
        Text2.text = "If you don't have an account register \nYou can Sign up here!"
        Text2.font = .preferredFont(forTextStyle: .body)
        Text2.numberOfLines = 2
        
        email.text = "E-mail"
        email.font = .preferredFont(forTextStyle: .body)
    }
    
    func setUpView() {
        view.addSubview(sampleLabel)
        view.addSubview(Text1)
        view.addSubview(Text2)
        view.addSubview(email)
        view.addSubview(input_email)
    }
    
    func setConstraints(){
        sampleLabel.snp.makeConstraints{make in make.center.equalToSuperview()}
        
        Text1.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(30)
            make.leading.equalToSuperview().offset(30)
        }
        
        Text2.snp.makeConstraints { make in
            make.top.equalTo(Text1.snp.bottom)
            make.leading.equalTo(Text1.snp.leading)
        }
        
        email.snp.makeConstraints { make in
            make.top.equalTo(Text2.snp.bottom).offset(100)
            make.leading.equalTo(Text1.snp.leading)
        }
    }
}

// canvas 이용하기
import SwiftUI

struct ViewControllerRepresentable: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> ViewController {
        return ViewController()
    }
    
    func updateUIViewController(_ uiViewController: ViewController, context: Context) {
    }
    
    typealias UIViewControllerType = ViewController
    
}
@available(iOS 13.0.0, *)
struct ViewPreview: PreviewProvider {
    static var previews: some View {
        ViewControllerRepresentable()
    }
}
