//
//  FriendRequestListViewController.swift
//  Ringo
//
//  Created by 강진혁 on 7/29/24.
//

import UIKit

class FriendRequestListViewController: UIViewController {

    let friendRequestListTableViewCell = FriendRequestListTableViewCell.identifier
    var requestsList: [FriendInfo] = [FriendInfo(name:"name",language:"language",email:"email"),
                                      FriendInfo(name:"name",language:"language",email:"email")]
    var tableView = UITableView()
    
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
        tableView.register(FriendRequestListTableViewCell.self, forCellReuseIdentifier: friendRequestListTableViewCell)
        tableView.dataSource = self
        tableView.delegate = self
    }
    func setConstraints(){
        tableView.snp.makeConstraints { make in
            make.edges.equalTo(view.safeAreaLayoutGuide)
        }
    }
}
// MARK: - UITableViewDataSource
extension FriendRequestListViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        requestsList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: friendRequestListTableViewCell, for: indexPath) as! FriendRequestListTableViewCell
        
        cell.name.text = requestsList[indexPath.row].name
        cell.selectionStyle = .none
        cell.delegate = self
        
        return cell
    }
    
    
}
// MARK: - UITableViewDelegate
extension FriendRequestListViewController: UITableViewDelegate {
    
}
// MARK: - Cell Delegate
extension FriendRequestListViewController: FriendRequestListTableViewCellDelegate {
    func pressedAcceptBtn() {
        print("accept btn")
    }
    
    func pressedRejectBtn() {
        print("reject btn")
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
