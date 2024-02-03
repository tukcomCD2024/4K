//
//  ContactsTableViewCell.swift
//  Ringo
//
//  Created by 강진혁 on 2/2/24.
//

import UIKit
import SnapKit

class ContactsTableViewCell: UITableViewCell {
    
    static let identifier = "ContactsTableViewCell"
    
    let expanded = false
    let stackView = UIStackView()
    let name = UILabel()
    let moreStackView = UIStackView()
    let call = UIButton()
    let info = UIButton()
    let paddingView = UIView()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setUpView()
        setUpValue()
        setConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setUpView() {
        paddingView.addSubview(name)
        contentView.addSubview(stackView)
        stackView.addArrangedSubview(paddingView)
        stackView.addArrangedSubview(moreStackView)
        moreStackView.addArrangedSubview(call)
    }
    
    private func setUpValue() {
        
        paddingView.layoutMargins = UIEdgeInsets(top: 10, left: 30, bottom: 10, right: 30)
        
        name.text = "Name"
        name.font = .preferredFont(forTextStyle: .title2)
        
        let imgConfig = UIImage.SymbolConfiguration(pointSize: 25)
        call.setImage(UIImage(systemName: "phone.circle.fill", withConfiguration: imgConfig), for: .normal)
        call.configuration = .plain()
        call.configuration?.imagePadding = 10
        call.configuration?.baseForegroundColor = .systemGreen
        
        stackView.axis = .vertical
        stackView.alignment = .leading
        stackView.distribution = .fillEqually
//        stackView.layoutMargins = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
//        stackView.isLayoutMarginsRelativeArrangement = true
        
        moreStackView.backgroundColor = .systemGray6
        moreStackView.axis = .horizontal
        moreStackView.alignment = .center
        moreStackView.distribution = .equalSpacing
        moreStackView.addBorders(edges: [.top], color: .systemGray4, thickness: 1)
    }
    
    private func setConstraints() {
        
        paddingView.snp.makeConstraints { make in
            make.top.equalTo(contentView.safeAreaLayoutGuide)
            make.leading.equalTo(contentView.safeAreaLayoutGuide)
            make.trailing.equalTo(contentView.safeAreaLayoutGuide)
        }
        name.snp.makeConstraints { make in
            make.leading.equalTo(paddingView.layoutMarginsGuide)
            make.top.equalTo(paddingView.layoutMarginsGuide)
            make.bottom.equalTo(paddingView.layoutMarginsGuide)
        }
        stackView.snp.makeConstraints { make in
            make.edges.equalTo(contentView.safeAreaLayoutGuide)
        }
        moreStackView.snp.makeConstraints { make in
            make.leading.equalTo(contentView.safeAreaLayoutGuide)
            make.trailing.equalTo(contentView.safeAreaLayoutGuide)
        }
        call.snp.makeConstraints { make in
            make.leading.equalTo(contentView.safeAreaLayoutGuide).offset(330)
            make.trailing.equalTo(contentView.safeAreaLayoutGuide).offset(-30)
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        moreStackView.isHidden = !selected
        
//        invalidateIntrinsicContentSize()
        // Configure the view for the selected state
    }

//    override func prepareForReuse() {
//        invalidateIntrinsicContentSize()
//        super.prepareForReuse()
//        self.setNeedsLayout()
//        self.layoutIfNeeded()
//    }
}

// MARK: - stackview border
extension UIStackView {
    @discardableResult
    func addBorders(edges: UIRectEdge,
                    color: UIColor,
                    inset: CGFloat = 0.0,
                    thickness: CGFloat = 1.0) -> [UIView] {

        var borders = [UIView]()

        @discardableResult
        func addBorder(formats: String...) -> UIView {
            let border = UIView(frame: .zero)
            border.backgroundColor = color
            border.translatesAutoresizingMaskIntoConstraints = false
            addSubview(border)
            addConstraints(formats.flatMap {
                NSLayoutConstraint.constraints(withVisualFormat: $0,
                                               options: [],
                                               metrics: ["inset": inset, "thickness": thickness],
                                               views: ["border": border]) })
            borders.append(border)
            return border
        }


        if edges.contains(.top) || edges.contains(.all) {
            addBorder(formats: "V:|-0-[border(==thickness)]", "H:|-inset-[border]-inset-|")
        }

        if edges.contains(.bottom) || edges.contains(.all) {
            addBorder(formats: "V:[border(==thickness)]-0-|", "H:|-inset-[border]-inset-|")
        }

        if edges.contains(.left) || edges.contains(.all) {
            addBorder(formats: "V:|-inset-[border]-inset-|", "H:|-0-[border(==thickness)]")
        }

        if edges.contains(.right) || edges.contains(.all) {
            addBorder(formats: "V:|-inset-[border]-inset-|", "H:[border(==thickness)]-0-|")
        }

        return borders
    }
}

    // MARK: - canvas 이용하기
import SwiftUI
struct UIViewPreview<View: UIView>: UIViewRepresentable {
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

struct ContactsTableViewCellPreview: PreviewProvider{
    static var previews: some View {
        UIViewPreview {
            let button = ContactsTableViewCell(frame: .zero)
            return button
        }
    }
}
