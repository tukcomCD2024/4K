//
//  VideoViewController.swift
//  Ringo
//
//  Created by 강진혁 on 9/3/24.
//

import UIKit
import SnapKit
import WebRTC

class VideoViewController: UIViewController {

    private let localVideoView = UIView()
    private let backBtn = UIButton()
    private let state = UILabel()
    private let webRTCClient: WebRTCClient
    
    init(webRTCClient: WebRTCClient) {
        self.webRTCClient = webRTCClient
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let localRenderer = RTCMTLVideoView(frame: self.localVideoView.frame)
        let remoteRenderer = RTCMTLVideoView(frame: self.view.frame)
        localRenderer.videoContentMode = .scaleAspectFill
        remoteRenderer.videoContentMode = .scaleAspectFill

        self.webRTCClient.startCaptureLocalVideo(renderer: localRenderer)
        self.webRTCClient.renderRemoteVideo(to: remoteRenderer)
        
        localVideoView.addSubview(localRenderer)
        self.view.addSubview(remoteRenderer)
        self.view.addSubview(state)
        self.view.sendSubviewToBack(state)
        view.addSubview(localVideoView)
        view.addSubview(backBtn)
        
        view.backgroundColor = .black
        localVideoView.backgroundColor = .systemBlue
        localVideoView.layer.cornerRadius = 20
        state.text = "The caller's camera is turned off."
        state.textColor = .lightGray
        backBtn.configuration = .filled()
        backBtn.configurationUpdateHandler = { btn in
            btn.configuration?.image = UIImage(systemName: "x.circle.fill")?.applyingSymbolConfiguration(UIImage.SymbolConfiguration(paletteColors: [.white,.clear]))
            btn.configuration?.preferredSymbolConfigurationForImage = UIImage.SymbolConfiguration(pointSize: 40)
            btn.configuration?.buttonSize = .mini
            btn.configuration?.cornerStyle = .capsule
            btn.configuration?.baseBackgroundColor = .systemRed
            btn.addTarget(self, action: #selector(self.backDidTap(_:)), for: .touchUpInside)
        }
        
        localVideoView.snp.makeConstraints({ make in
            make.trailing.equalTo(view.safeAreaLayoutGuide).inset(20)
            make.bottom.equalTo(view.safeAreaLayoutGuide).inset(20)
            make.width.equalToSuperview().multipliedBy(0.4)
            make.height.equalToSuperview().multipliedBy(0.3)
        })
        localRenderer.snp.makeConstraints { make in
            make.edges.equalTo(localVideoView.safeAreaLayoutGuide)
        }
        backBtn.snp.makeConstraints { make in
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.bottom.equalTo(view.safeAreaLayoutGuide).inset(20)
        }
        state.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.centerY.equalToSuperview()
        }

    }
    
    @IBAction private func backDidTap(_ sender: Any) {
        self.webRTCClient.hideVideo()
        self.dismiss(animated: true)
    }
}
