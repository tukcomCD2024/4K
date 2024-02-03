//
//  ContactsViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/1/24.
//

import UIKit
import SnapKit

class ContactsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    let contactsTableViewCell = ContactsTableViewCell.identifier
    
    let randomNames = ["민준", "서준", "예준", "도윤", "시우", "주원", "하준", "지호", "지후", "준서", "준우", "현우", "도현", "지훈", "건우", "우진", "선우", "서진", "민재", "현준", "연우", "유준", "정우", "승우", "승현", "시윤", "준혁", "은우", "지환", "승민", "지우", "유찬", "윤우", "민성", "준영", "시후", "진우", "지수", "서연", "서윤", "지우", "서현", "민서", "하은", "하윤", "윤서", "지유", "지민", "채원", "지윤", "은서", "수아", "다은", "예은", "지아", "수빈", "소율", "예린", "예원", "지원", "소윤", "지안", "하린", "시은", "유진", "채은"]
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return randomNames.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: contactsTableViewCell, for: indexPath) as! ContactsTableViewCell
        
        cell.name.text = randomNames[indexPath.row]
        cell.selectionStyle = .none
                
        return cell
    }
    

    let searchController = UISearchController()
    var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
        
//        navigationItem.largeTitleDisplayMode = .inline
        
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
//        tableView.rowHeight = UITableView.automaticDimension
        self.view.addSubview(tableView)
        
        setConstraints()
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

extension ContactsViewController {
    
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
    
//    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        
//        tableView.deselectRow(at: indexPath, animated: false)
//    }

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
