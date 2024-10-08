//
//  ContactsViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/1/24.
//

import UIKit
import SnapKit

class ContactsViewController: UIViewController {
    
    let contactsTableViewCell = ContactsTableViewCell.identifier
    
    var friendsList = [FriendInfo]()

    let searchController = UISearchController()
    var tableView: UITableView!
    let ifEmptyList = UILabel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .systemBackground
        
        //navigation
        self.navigationItem.title = "Contacts"
        self.navigationController?.navigationBar.prefersLargeTitles = true
        self.navigationItem.searchController = searchController
        self.navigationItem.hidesSearchBarWhenScrolling = false
        self.navigationItem.leftBarButtonItem = UIBarButtonItem(image: UIImage(systemName: "person.badge.plus"), style: .plain, target: self, action: #selector(requestFriend))
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(systemName: "envelope"), style: .plain, target: self, action: #selector(requestList))
        
        
        //table
        tableView = UITableView()
        tableView.register(ContactsTableViewCell.self, forCellReuseIdentifier: contactsTableViewCell)
        tableView.delegate = self
        tableView.dataSource = self
        self.view.addSubview(tableView)
        
        ifEmptyList.text = "No friends yet"
        ifEmptyList.textColor = .systemGray2
        ifEmptyList.textAlignment = .center
        ifEmptyList.sizeToFit()
        tableView.backgroundView = ifEmptyList
        
        setConstraints()
        
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        loadFriends()
        FriendRequestList.shared.reload()
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        checkRequestList()
    }
    func setConstraints(){
        tableView.snp.makeConstraints { make in
            make.edges.equalTo(view.safeAreaLayoutGuide)
        }
    }
    //navigaion rightBtn action
    @objc private func requestFriend(_ sender: UIBarButtonItem) {
        present(UINavigationController(rootViewController: FriendRequestViewController()), animated: true)
//        self.navigationController?.pushViewController(FriendRequestViewController(), animated: true)
    }
    
    @objc private func requestList() {
        // 네비게이션 Push
        self.navigationController?.pushViewController(FriendRequestListViewController(), animated: true)
        // modal
//        present(UINavigationController(rootViewController: FriendRequestListViewController()), animated: true)
    }
    private func checkRequestList() {
        print(FriendRequestList.shared.getList())
        if FriendRequestList.shared.getList().isEmpty {
            navigationItem.rightBarButtonItem?.image = UIImage(systemName: "envelope")
        } else { navigationItem.rightBarButtonItem?.image = UIImage(systemName: "envelope.badge")?.applyingSymbolConfiguration(UIImage.SymbolConfiguration(paletteColors: [.systemRed,.systemBlue])) }
    }
}

// MARK: - UITableViewDelegate

extension ContactsViewController: UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, willSelectRowAt indexPath: IndexPath) -> IndexPath? {
        let cell = tableView.cellForRow(at: indexPath) as! ContactsTableViewCell
        if cell.isSelected == true {
            cell.isSelected = false
            cell.moreStackView.isHidden = true
            tableView.beginUpdates()
            tableView.setNeedsDisplay()
            tableView.endUpdates()
            return nil
        }
        return indexPath
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.beginUpdates()
        tableView.setNeedsDisplay()
        tableView.endUpdates()
    }
    
    func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
        tableView.beginUpdates()
        tableView.setNeedsDisplay()
        tableView.endUpdates()
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }

}
// MARK: - UITableViewDataSource

extension ContactsViewController: UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return friendsList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: contactsTableViewCell, for: indexPath) as! ContactsTableViewCell
        
        cell.name.text = friendsList[indexPath.row].name
        cell.selectionStyle = .none
        cell.delegate = self
                
        return cell
    }
}
// MARK: -  Load Friends
extension ContactsViewController {
    
    func loadFriends() {
        //id ??인 유저의 친구목록
        FriendService.shared.loadFriendsList(email: UserManager.getData(type: String.self, forKey: .email) ?? "") { response in
            switch response {
            case .success(let data):
                    
                guard let data = data as? [FriendInfo] else { return }
                if !data.isEmpty {
                    self.friendsList = data
                    self.tableView.backgroundView?.isHidden = true
                    self.tableView.reloadData()
                } else {
                    self.tableView.backgroundView?.isHidden = false
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

// MARK: -  Cell Delegate
extension ContactsViewController: ContactsTableViewCellDelegate {
    
    func pressedButton() {
        let rowNum = tableView.indexPathForSelectedRow!.row
        CallService.shared.signalClient.startcall(user: UserManager.getData(type: String.self, forKey: .email)!, target: friendsList[rowNum].email)
        UserManager.setData(value: friendsList[rowNum].email, key: .receiver)
    }
    
}
// MARK: - canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct ContactsViewControllerPreView: PreviewProvider {
    static var previews: some View {
    // 사용할 뷰 컨트롤러를 넣어주세요
        ContactsViewController()
            .toPreview()
    }
}
