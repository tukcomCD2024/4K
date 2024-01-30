//
//  ViewController.swift
//  Ringo
//
//  Created by 강진혁 on 1/29/24.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let sampleLabel: UILabel = {
            let label = UILabel()
            label.text = "스토리보드 없이 구현한 ViewController"
            label.font = .preferredFont(forTextStyle: .title3)
            label.translatesAutoresizingMaskIntoConstraints = false
            return label
        }()
        
        let Text1: UILabel = {
            let label = UILabel()
            label.text = "Sign in"
            label.font = .preferredFont(forTextStyle: .title1)
            label.translatesAutoresizingMaskIntoConstraints = false
            return label
        }()
        let Text2: UILabel = {
            let label = UILabel()
            label.text = "If you don't have an account register \nYou can Sign up here!"
            label.font = .preferredFont(forTextStyle: .body)
            label.numberOfLines = 2
            label.translatesAutoresizingMaskIntoConstraints = false
            return label
        }()
        
        let email: UILabel = {
            let label = UILabel()
            label.text = "E-mail"
            label.font = .preferredFont(forTextStyle: .body)
            label.translatesAutoresizingMaskIntoConstraints = false
            return label
        }()
        
        let input_email: UITextField = {
            let input = UITextField(frame: CGRect(x: 0, y: 0, width: 100, height: 0))
            input.text = "ddd"
            input.borderStyle = .roundedRect
            input.backgroundColor = .black
            return input
        }()
        
        view.backgroundColor = .white
        view.addSubview(sampleLabel)
        view.addSubview(Text1)
        view.addSubview(Text2)
        view.addSubview(email)
        view.addSubview(input_email)
        
        sampleLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        sampleLabel.centerYAnchor.constraint(equalTo: view.centerYAnchor).isActive = true
        
        Text1.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 30).isActive = true
        Text1.topAnchor.constraint(equalTo: view.topAnchor, constant: 50).isActive = true
        
        Text2.leadingAnchor.constraint(equalTo: Text1.leadingAnchor).isActive = true
        Text2.topAnchor.constraint(equalTo: Text1.bottomAnchor, constant: 10).isActive = true
        
        email.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 20).isActive = true
        email.topAnchor.constraint(equalTo: Text2.bottomAnchor, constant: 100).isActive = true
        
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
