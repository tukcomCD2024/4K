//
//  FriendRequestListViewController.swift
//  Ringo
//
//  Created by 강진혁 on 7/29/24.
//

import UIKit

class FriendRequestListViewController: UIViewController {

    let friendRequestListTableViewCell = FriendRequestListTableViewCell.identifier
    var tableView = UITableView()
    let ifEmptyList = UILabel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpView()
        setUpValue()
        setConstraints()
    }
    func setUpView(){
        view.addSubview(tableView)
    }
    func setUpValue(){
        navigationItem.title = "Request List"
//        navigationController?.navigationBar.prefersLargeTitles = false
        tabBarController?.tabBar.isHidden = true
        view.backgroundColor = .systemBackground
        
        tableView.register(FriendRequestListTableViewCell.self, forCellReuseIdentifier: friendRequestListTableViewCell)
        tableView.dataSource = self
        tableView.delegate = self
        
        ifEmptyList.text = "No requests"
        ifEmptyList.textColor = .systemGray2
        ifEmptyList.textAlignment = .center
        ifEmptyList.sizeToFit()
        tableView.backgroundView = ifEmptyList
    }
    func setConstraints(){
        tableView.snp.makeConstraints { make in
            make.edges.equalTo(view.safeAreaLayoutGuide)
        }
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        tabBarController?.tabBar.isAppearedWithAnimation()
    }
}
// MARK: - UITableViewDataSource
extension FriendRequestListViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if FriendRequestList.shared.getList().isEmpty {
            tableView.backgroundView?.isHidden = false
        } else {
            tableView.backgroundView?.isHidden = true
        }
        return FriendRequestList.shared.getList().count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: friendRequestListTableViewCell, for: indexPath) as! FriendRequestListTableViewCell
        
        cell.name.text = FriendRequestList.shared.getList()[indexPath.row].name
        cell.selectionStyle = .none
        cell.pressedAcceptBtn = {
            print("accept btn")
            self.acceptRequest(receiver: FriendRequestList.shared.getList()[indexPath.row].email)
            self.deleteRow(index: indexPath.row)
        }
        cell.pressedRejectBtn = {
            print("reject btn")
            self.rejectRequest(receiver: FriendRequestList.shared.getList()[indexPath.row].email)
            self.deleteRow(index: indexPath.row)
        }
        
        return cell
    }
    func deleteRow(index: Int){
        FriendRequestList.shared.remove(at: index)
        tableView.deleteRows(at: [IndexPath(row: index, section: 0)], with: .automatic)
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            print("loading")
            self.tableView.reloadData()
        }
    }
}
// MARK: - UITableViewDelegate
extension FriendRequestListViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
}
// MARK: - TabBar Animation
extension UITabBar {
    func isHiddenWithAnimation() {
        let orig = self.frame
        var target = self.frame
        target.origin.x = target.origin.x - target.size.width
        UIView.animate(withDuration: 0.2, animations: {
            self.frame = target
        }) { (true) in
            self.isHidden = true
            self.frame = orig
        }
    }
    func isAppearedWithAnimation() {
        let orig = self.frame
        var target = self.frame
        target.origin.x = target.origin.x - target.size.width
        self.frame = target
        self.isHidden = false
        UIView.animate(withDuration: 0.3, animations: {
            self.frame = orig
        })
    }
}
// MARK: - firend request
extension FriendRequestListViewController {
    
    func acceptRequest(receiver: String) {
        
        guard let sender = UserManager.getData(type: String.self, forKey: .email) else { return }
        
        FriendService.shared.acceptRequest(sender: sender, receiver: receiver) { response in
            switch response {
            case .success(let data):
                guard let data = data as? String else { return }
                if data == "{\"message\":\"성공\"}" {
                    let alert = UIAlertController(title: "요청 수락", message: nil, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "확인", style: .cancel, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                } else {
                    let alert = UIAlertController(title: data, message: nil, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "확인", style: .cancel, handler: nil))
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
    func rejectRequest(receiver: String) {
        
        guard let sender = UserManager.getData(type: String.self, forKey: .email) else { return }
        
        FriendService.shared.rejectRequest(sender: sender, receiver: receiver) { response in
            switch response {
            case .success(let data):
                guard let data = data as? String else { return }
                if data == "{\"message\":\"성공\"}" {
                    let alert = UIAlertController(title: "요청 거절", message: nil, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "확인", style: .cancel, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                } else {
                    let alert = UIAlertController(title: data, message: nil, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "확인", style: .cancel, handler: nil))
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
struct FriendRequestListViewControllerPreView: PreviewProvider {
    static var previews: some View {
    // 사용할 뷰 컨트롤러를 넣어주세요
        FriendRequestListViewController()
            .toPreview()
    }
}
