//
//  FriendRequestListTableViewCell.swift
//  Ringo
//
//  Created by 강진혁 on 7/29/24.
//

import UIKit

protocol FriendRequestListTableViewCellDelegate: AnyObject {
    func pressedAcceptBtn()
    func pressedRejectBtn()
}

class FriendRequestListTableViewCell: UITableViewCell {

    static let identifier = "FriendRequestListTableViewCell"
    var delegate: FriendRequestListTableViewCellDelegate?
    
    let name = UILabel()
    let acceptBtn = UIButton()
    let rejectBtn = UIButton()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setUpView()
        setUpValue()
        setConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func setUpView(){
        contentView.addSubview(name)
        contentView.addSubview(acceptBtn)
        contentView.addSubview(rejectBtn)
    }
    func setUpValue(){
        name.text = "Name"
        name.font = .preferredFont(forTextStyle: .title2)
        
        acceptBtn.setImage(UIImage(systemName: "person"), for: .normal)
        acceptBtn.addTarget(self, action: #selector(acceptBtnAction), for: .touchUpInside)
        
        rejectBtn.setImage(UIImage(systemName: "person"), for: .normal)
        rejectBtn.addTarget(self, action: #selector(rejectBtnAction), for: .touchUpInside)
    }
    func setConstraints(){
        name.snp.makeConstraints { make in
            make.leading.equalTo(contentView.safeAreaLayoutGuide)
        }
        acceptBtn.snp.makeConstraints { make in
            make.trailing.equalTo(rejectBtn.snp.leading)
        }
        rejectBtn.snp.makeConstraints { make in
            make.trailing.equalTo(contentView.safeAreaLayoutGuide)
        }
    }
    
    @objc func acceptBtnAction() {
        delegate?.pressedAcceptBtn()
    }
    @objc func rejectBtnAction() {
        delegate?.pressedRejectBtn()
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }

}
// MARK: - canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct FriendRequestListTableViewCellPreView: PreviewProvider {
    static var previews: some View {
        UIViewPreview {
            let button = FriendRequestListTableViewCell(frame: .zero)
            return button
        }
    }
}
