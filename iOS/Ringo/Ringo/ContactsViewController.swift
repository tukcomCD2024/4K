//
//  ContactsViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/1/24.
//

import UIKit
import SnapKit

class ContactsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    let randomNames = ["민준", "서준", "예준", "도윤", "시우", "주원", "하준", "지호", "지후", "준서", "준우", "현우", "도현", "지훈", "건우", "우진", "선우", "서진", "민재", "현준", "연우", "유준", "정우", "승우", "승현", "시윤", "준혁", "은우", "지환", "승민", "지우", "유찬", "윤우", "민성", "준영", "시후", "진우", "지수", "서연", "서윤", "지우", "서현", "민서", "하은", "하윤", "윤서", "지유", "지민", "채원", "지윤", "은서", "수아", "다은", "예은", "지아", "수빈", "소율", "예린", "예원", "지원", "소윤", "지안", "하린", "시은", "유진", "채은"]
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return randomNames.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        cell.textLabel?.text = randomNames[indexPath.row]
                
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
        tableView = UITableView(frame: CGRect(x: 0, y: 0, width: self.view.bounds.width, height: self.view.bounds.height))
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "Cell")
        tableView.delegate = self
        tableView.dataSource = self
        self.view.addSubview(tableView)
        
    }
    
    //navigaion rightBtn action
    @objc private func requestFriend(_ sender: UIBarButtonItem) {
        self.navigationController?.pushViewController(FriendRequestViewController(), animated: true)
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
// canvas 이용하기
import SwiftUI

struct ContactsViewControllerRepresentable: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> ContactsViewController {
        return ContactsViewController()
    }
    
    func updateUIViewController(_ uiViewController: ContactsViewController, context: Context) {
    }
    
    typealias UIViewControllerType = ContactsViewController
    
}
@available(iOS 13.0.0, *)
struct ContactsViewPreview: PreviewProvider {
    static var previews: some View {
        ContactsViewControllerRepresentable().edgesIgnoringSafeArea(.all)
    }
}
