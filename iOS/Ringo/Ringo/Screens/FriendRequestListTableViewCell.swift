//
//  FriendRequestListTableViewCell.swift
//  Ringo
//
//  Created by 강진혁 on 7/29/24.
//

import UIKit

class FriendRequestListTableViewCell: UITableViewCell {

    static let identifier = "FriendRequestListTableViewCell"
    
    let name = UILabel()
    let acceptBtn = UIButton()
    let rejectBtn = UIButton()
    
    var pressedAcceptBtn: (() -> Void)?
    var pressedRejectBtn: (() -> Void)?
    
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
        
        acceptBtn.configuration = UIButton.Configuration.plain()
        acceptBtn.configurationUpdateHandler = { btn in
            btn.configuration?.image = UIImage(systemName: "checkmark.circle.fill")
            btn.configuration?.baseForegroundColor = .systemGreen
            btn.configuration?.preferredSymbolConfigurationForImage = UIImage.SymbolConfiguration(pointSize: 25)
            btn.addTarget(self, action: #selector(self.acceptBtnAction), for: .touchUpInside)
        }
        
        rejectBtn.configuration = UIButton.Configuration.plain()
        rejectBtn.configurationUpdateHandler = { btn in
            btn.configuration?.image = UIImage(systemName: "xmark.circle.fill")
            btn.configuration?.baseForegroundColor = .systemRed
            btn.configuration?.preferredSymbolConfigurationForImage = UIImage.SymbolConfiguration(pointSize: 25)
            btn.addTarget(self, action: #selector(self.rejectBtnAction), for: .touchUpInside)
        }
    }
    func setConstraints(){
        name.snp.makeConstraints { make in
            make.top.equalTo(contentView.safeAreaLayoutGuide).offset(15)
            make.bottom.equalTo(contentView.safeAreaLayoutGuide).inset(15)
            make.leading.equalTo(contentView.safeAreaLayoutGuide).offset(20)
        }
        acceptBtn.snp.makeConstraints { make in
            make.top.equalTo(contentView.safeAreaLayoutGuide).offset(15)
            make.bottom.equalTo(contentView.safeAreaLayoutGuide).inset(15)
            make.trailing.equalTo(rejectBtn.snp.leading)
        }
        rejectBtn.snp.makeConstraints { make in
            make.top.equalTo(contentView.safeAreaLayoutGuide).offset(15)
            make.bottom.equalTo(contentView.safeAreaLayoutGuide).inset(15)
            make.trailing.equalTo(contentView.safeAreaLayoutGuide).inset(10)
        }
    }
    
    @objc func acceptBtnAction() {
        pressedAcceptBtn?()
    }
    @objc func rejectBtnAction() {
        pressedRejectBtn?()
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
