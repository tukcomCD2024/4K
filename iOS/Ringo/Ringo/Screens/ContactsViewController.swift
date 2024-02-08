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
    
    var randomNames = [String]()

    let searchController = UISearchController()
    var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //navigation
        self.navigationItem.title = "Contacts"
        self.navigationController?.navigationBar.prefersLargeTitles = true
        self.navigationItem.searchController = searchController
        self.navigationItem.hidesSearchBarWhenScrolling = false
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(systemName: "person.badge.plus"), style: .plain, target: self, action: #selector(requestFriend))

        //table
        tableView = UITableView()
        tableView.register(ContactsTableViewCell.self, forCellReuseIdentifier: contactsTableViewCell)
        tableView.delegate = self
        tableView.dataSource = self
        self.view.addSubview(tableView)
        
        setConstraints()
        
        loadFriends()
    }
    
    func setConstraints(){
        tableView.snp.makeConstraints { make in
            make.edges.equalTo(view.safeAreaLayoutGuide)
        }
    }
    //navigaion rightBtn action
    @objc private func requestFriend(_ sender: UIBarButtonItem) {
        self.navigationController?.pushViewController(FriendRequestViewController(), animated: true)
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
        return randomNames.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: contactsTableViewCell, for: indexPath) as! ContactsTableViewCell
        
        cell.name.text = randomNames[indexPath.row]
        cell.selectionStyle = .none
                
        return cell
    }
}
// MARK: -  Sign in
extension ContactsViewController {
    
    func loadFriends() {
        //id 3인 유저의 친구목록
        FriendService.shared.loadFriendsList(userId: 3) { response in
            switch response {
            case .success(let data):
                    
                guard let data = data as? [String] else { return }
                if !data.isEmpty {
                    self.randomNames = data
                    self.tableView.reloadData()
                } else {
                    let alert = UIAlertController(title: "친구가 없습니다", message: "", preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "확인", style: .cancel, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    }
                    
            case .requestErr(let err):
                print(err)
            case .pathErr:
                print("pathErr")
            case .serverErr:
                print("serverErr")
            case .networkFail:
                print("networkFail")
            }
        }
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
