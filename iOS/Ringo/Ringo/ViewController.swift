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
        
        view.backgroundColor = .white
        view.addSubview(sampleLabel)
        sampleLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        sampleLabel.centerYAnchor.constraint(equalTo: view.centerYAnchor).isActive = true
        
    }


}

