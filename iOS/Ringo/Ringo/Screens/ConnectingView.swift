//
//  ConnectingView.swift
//  Ringo
//
//  Created by 강진혁 on 8/12/24.
//

import UIKit
import SnapKit

final class ConnectingView: UIView {
    
    static let shared = ConnectingView()
    let gradientLayer = CAGradientLayer()
    let name = UILabel()
    private let loadingView = UIActivityIndicatorView(style: .large)
    let hangUpBtn = UIButton()
    
    var isLoading = true {
      didSet {
        self.isHidden = !self.isLoading
        self.isLoading ? self.loadingView.startAnimating() : self.loadingView.stopAnimating()
      }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.backgroundColor = .clear
        self.gradientLayer.frame = self.bounds
        self.gradientLayer.type = .radial
        self.gradientLayer.startPoint = CGPoint(x: -0.3, y: -0.15)
        self.gradientLayer.endPoint = CGPoint(x: 2.0, y: 1.0)
        self.gradientLayer.locations = [0,0.1,0.8,1]
        self.gradientLayer.colors = [
            CGColor(red: 111/255.0, green: 122/255.0, blue: 130/255.0, alpha: 1),
            CGColor(red: 93/255.0, green: 114/255.0, blue: 126/255.0, alpha: 1),
            CGColor(red: 17/255.0, green: 43/255.0, blue: 74/255.0, alpha: 1),
            CGColor(red: 13/255.0, green: 36/255.0, blue: 69/255.0, alpha: 1)
        ]

        name.text = "Name"
        name.font = .systemFont(ofSize: 40)
        name.textColor = .white
        
        loadingView.color = .white
        
        hangUpBtn.configuration = .filled()
        hangUpBtn.configurationUpdateHandler = { btn in
            btn.configuration?.image = UIImage(systemName: "phone.down.circle")?.applyingSymbolConfiguration(UIImage.SymbolConfiguration(paletteColors: [.white,.clear]))
            btn.configuration?.preferredSymbolConfigurationForImage = UIImage.SymbolConfiguration(pointSize: 50)
            btn.configuration?.buttonSize = .mini
            btn.configuration?.cornerStyle = .capsule
            btn.configuration?.baseBackgroundColor = .systemRed
            btn.addTarget(self, action: #selector(self.onPressHangUpBtn(_:)), for: .touchUpInside)
        }
        
        self.layer.addSublayer(gradientLayer)
        self.addSubview(name)
        self.addSubview(loadingView)
        self.addSubview(hangUpBtn)
        
        name.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalToSuperview().offset(80)
        }
        loadingView.snp.makeConstraints { make in
            make.center.equalToSuperview()
        }
        hangUpBtn.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.bottom.equalToSuperview().inset(80)
        }
    }
    
    @objc func onPressHangUpBtn(_ sender: UIButton) {
        CallService.shared.webRTCClient.endCall()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        // 그라데이션 레이어가 뷰 크기에 맞게 조정되도록 프레임 업데이트
        gradientLayer.frame = self.bounds
    }
    func show() {
        guard let window = SceneDelegate.shared?.window else { 
            print("UIWindow를 찾을 수 없습니다.")
            return
        }
        window.addSubview(self)
        self.snp.makeConstraints {
            $0.edges.equalToSuperview()
        }
        isLoading = true
        self.layoutIfNeeded()
    }
    func hide(completion: @escaping () -> () = {}) {
        isLoading = false
        self.removeFromSuperview()
        completion()
      }
}
