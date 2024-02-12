//
//  TabBarViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/1/24.
//

import UIKit
import SnapKit
import WebRTC

class TabBarViewController: UITabBarController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        CallService.shared.webRTCClient.delegate = self
        CallService.shared.signalClient.delegate = self
        CallService.shared.signalClient.connect()
        
        view.backgroundColor = .systemBackground
        
        tabBar.backgroundColor = .init(white: 1, alpha: 0.8)
        
        let contactsVC = UINavigationController(rootViewController: ContactsViewController())
        let recentsVC = RecentsViewController()
        let accountVC = AccountViewController()
        
        contactsVC.tabBarItem = UITabBarItem(title: "Contacts", image: UIImage(systemName: "person.3"), tag: 1)
        recentsVC.tabBarItem = UITabBarItem(title: "Recents", image: UIImage(systemName: "clock"), tag: 2)
        accountVC.tabBarItem = UITabBarItem(title: "Account", image: UIImage(systemName: "person.text.rectangle"), tag: 3)

        contactsVC.navigationBar.backgroundColor = .systemBackground
        
        setViewControllers([contactsVC,recentsVC,accountVC], animated: false)
    }
}

extension TabBarViewController: SignalClientDelegate {
    func signalClientDidConnect(_ signalClient: SignalingClient) {
        print("signal connect")
    }
    
    func signalClientDidDisconnect(_ signalClient: SignalingClient) {
        print("signal disconnect")
    }
    
    func signalClient(_ signalClient: SignalingClient, didReceiveRemoteSdp sdp: RTCSessionDescription) {
        print("Received remote sdp")
        CallService.shared.webRTCClient.set(remoteSdp: sdp) { (error) in
            DispatchQueue.main.async {
                let alert = UIAlertController(title: "Call", message: "", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "Refuse", style: .destructive, handler: nil))
                alert.addAction(UIAlertAction(title: "Accept", style: .default, handler: { action in
                    self.acceptCall()
                }))
                self.present(alert, animated: true, completion: nil)
            }
        }
    }
    
    func signalClient(_ signalClient: SignalingClient, didReceiveCandidate candidate: RTCIceCandidate) {
        CallService.shared.webRTCClient.set(remoteCandidate: candidate) { error in
            print("Received remote candidate : \(candidate)")
        }
    }
    
    func acceptCall() {
        CallService.shared.webRTCClient.answer { (localSdp) in
            CallService.shared.signalClient.send(sdp: localSdp)
        }
        let connectionVC = ConnectionViewController()
        connectionVC.modalPresentationStyle = .fullScreen
        self.present(connectionVC, animated: true,completion: nil)
    }
    
}

extension TabBarViewController: WebRTCClientDelegate {
    func webRTCClient(_ client: WebRTCClient, didDiscoverLocalCandidate candidate: RTCIceCandidate) {
        print("discovered local candidate")
        CallService.shared.signalClient.send(candidate: candidate)
    }
    
    func webRTCClient(_ client: WebRTCClient, didChangeConnectionState state: RTCIceConnectionState) {
        print("change connection state : \(state)")
    }
    
    func webRTCClient(_ client: WebRTCClient, didReceiveData data: Data) {
        DispatchQueue.main.async {
            let message = String(data: data, encoding: .utf8) ?? "(Binary: \(data.count) bytes)"
            let alert = UIAlertController(title: "Message from WebRTC", message: message, preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
}

//// MARK: canvas 이용하기
//import SwiftUI
//@available(iOS 13.0.0, *)
//struct TabBarViewControllerPreView: PreviewProvider {
//    static var previews: some View {
//    // 사용할 뷰 컨트롤러를 넣어주세요
//        TabBarViewController()
//            .toPreview()
//    }
//}
