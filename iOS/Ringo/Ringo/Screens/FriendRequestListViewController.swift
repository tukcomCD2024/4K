//
//  FriendRequestListViewController.swift
//  Ringo
//
//  Created by 강진혁 on 7/29/24.
//

import UIKit

class FriendRequestListViewController: UIViewController {

    let friendRequestListTableViewCell = FriendRequestListTableViewCell.identifier
    var requestsList: [FriendInfo] = [FriendInfo(name:"name1",language:"language1",email:"email1"),
                                      FriendInfo(name:"name2",language:"language2",email:"email2")]
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
        navigationItem.title = "Request List"
        navigationController?.navigationBar.prefersLargeTitles = false
        
        tableView.register(FriendRequestListTableViewCell.self, forCellReuseIdentifier: friendRequestListTableViewCell)
        tableView.dataSource = self
        tableView.delegate = self
     
        view.backgroundColor = .gray
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
        cell.pressedAcceptBtn = {
            let index = indexPath.row
            print("accept btn")
            print(self.requestsList[index].email)
        }
        cell.pressedRejectBtn = {
            let index = indexPath.row
            print("reject btn")
            print(self.requestsList[index].email)
        }
        
        return cell
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
