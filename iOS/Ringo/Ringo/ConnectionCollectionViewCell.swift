//
//  ConnectionCollectionViewCell.swift
//  Ringo
//
//  Created by 강진혁 on 2/7/24.
//

import UIKit
import SnapKit

class ConnectionCollectionViewCell: UICollectionViewCell {
    
    static let identifier = "ConnectionCollectionViewCell"
    var nameLabel = UILabel()
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setUpView()
        setUpValue()
        setConstraints()
    }
        
    override init(frame: CGRect) {
        super.init(frame: .zero)
        setUpView()
        setUpValue()
        setConstraints()
    }
    
    func setUpView() {
        contentView.addSubview(nameLabel)
    }
    
    func setUpValue() {
        nameLabel.text = "name"
        nameLabel.textColor = .white
        nameLabel.font = .preferredFont(forTextStyle: .title2)
        nameLabel.textAlignment = .center
        nameLabel.backgroundColor = .black.withAlphaComponent(0.5)
        nameLabel.layer.cornerRadius = 15
        nameLabel.clipsToBounds = true

//        nameLabel.layer.borderColor = UIColor.systemGreen.cgColor
//        nameLabel.layer.borderWidth = 2
    }
    
    func setConstraints() {
        nameLabel.snp.makeConstraints { make in
            make.edges.equalTo(contentView.safeAreaLayoutGuide)
        }
    }
}
// MARK: - canvas 이용하기
import SwiftUI
struct ConnectionViewCellPreview<View: UIView>: UIViewRepresentable {
let view: View

init(_ builder: @escaping () -> View) {
    view = builder()
}

func makeUIView(context: Context) -> UIView {
    return view
}

func updateUIView(_ view: UIView, context: Context) {
    view.setContentHuggingPriority(.defaultHigh, for: .horizontal)
    view.setContentHuggingPriority(.defaultHigh, for: .vertical)
}
}

struct ConnectionCollectionViewCellPreview: PreviewProvider{
static var previews: some View {
    UIViewPreview {
        let button = ConnectionCollectionViewCell(frame: .zero)
        return button
    }
}
}
