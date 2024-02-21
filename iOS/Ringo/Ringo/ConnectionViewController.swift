//
//  ConnectionViewController.swift
//  Ringo
//
//  Created by 강진혁 on 2/6/24.
//

import UIKit
import SnapKit

class ConnectionViewController: UIViewController {

    let connectionCollectionViewCell = ConnectionCollectionViewCell.identifier
    let gradientLayer = CAGradientLayer()
    let collectionView = UICollectionView(frame: .zero, collectionViewLayout: UICollectionViewFlowLayout())
    let vStack = UIStackView()
    let hStack1 = UIStackView()
    let hStack2 = UIStackView()
    let muteBtn = UIButton()
    let speakerBtn = UIButton()
    let translateBtn = UIButton()
    let hangUpBtn = UIButton()
    
    let randomNames = ["example2"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUpView()
        setUpValue()
        setConstraints()
    }
    
    func setUpView() {
        view.layer.addSublayer(gradientLayer)
        view.addSubview(collectionView)
        view.addSubview(vStack)
        vStack.addArrangedSubview(hStack1)
        vStack.addArrangedSubview(hStack2)
        hStack1.addArrangedSubview(muteBtn)
        hStack1.addArrangedSubview(speakerBtn)
        hStack1.addArrangedSubview(translateBtn)
        hStack2.addArrangedSubview(hangUpBtn)
    }

    func setUpValue() {
        view.backgroundColor = .clear
        gradientLayer.frame = view.bounds
        gradientLayer.type = .radial
        gradientLayer.startPoint = CGPoint(x: -0.3, y: -0.15)
        gradientLayer.endPoint = CGPoint(x: 2.0, y: 1.0)
        gradientLayer.locations = [0,0.1,0.8,1]
        gradientLayer.colors = [
            CGColor(red: 111/255.0, green: 122/255.0, blue: 130/255.0, alpha: 1),
            CGColor(red: 93/255.0, green: 114/255.0, blue: 126/255.0, alpha: 1),
            CGColor(red: 17/255.0, green: 43/255.0, blue: 74/255.0, alpha: 1),
            CGColor(red: 13/255.0, green: 36/255.0, blue: 69/255.0, alpha: 1)
        ]
        
        collectionView.register(ConnectionCollectionViewCell.self, forCellWithReuseIdentifier: connectionCollectionViewCell)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.backgroundColor = .clear
        
        vStack.layoutMargins = UIEdgeInsets(top: 30, left: 40, bottom: 30, right: 40)
        vStack.isLayoutMarginsRelativeArrangement = true
        vStack.axis = .vertical
        vStack.alignment = .center
        vStack.distribution = .fillEqually
        vStack.spacing = 40
        
        hStack1.axis = .horizontal
        hStack1.alignment = .center
        hStack1.distribution = .equalCentering
        
        let imageSize = UIImage.SymbolConfiguration(font: .systemFont(ofSize: 25))
        
        muteBtn.backgroundColor = .white.withAlphaComponent(0.16)
        muteBtn.tintColor = .white
        muteBtn.configuration = .plain()
        muteBtn.setImage(UIImage(systemName: "mic.slash.fill",withConfiguration: imageSize), for: .normal)
        muteBtn.clipsToBounds = true
        muteBtn.invalidateIntrinsicContentSize()
        muteBtn.configuration?.contentInsets = NSDirectionalEdgeInsets(top: 25, leading: 25, bottom: 25, trailing: 25)
        muteBtn.layer.cornerRadius = 40
        
        speakerBtn.backgroundColor = .white.withAlphaComponent(0.16)
        speakerBtn.tintColor = .white
        speakerBtn.configuration = .plain()
        speakerBtn.setImage(UIImage(systemName: "speaker.wave.3.fill",withConfiguration: imageSize), for: .normal)
        speakerBtn.clipsToBounds = true
        speakerBtn.invalidateIntrinsicContentSize()
        speakerBtn.configuration?.contentInsets = NSDirectionalEdgeInsets(top: 25, leading: 15, bottom: 25, trailing: 15)
        speakerBtn.layer.cornerRadius = 40
        
        translateBtn.backgroundColor = .white.withAlphaComponent(0.16)
        translateBtn.tintColor = .white
        translateBtn.configuration = .plain()
        translateBtn.setImage(UIImage(systemName: "character.book.closed.fill",withConfiguration: imageSize), for: .normal)
        translateBtn.clipsToBounds = true
        translateBtn.invalidateIntrinsicContentSize()
        translateBtn.configuration?.contentInsets = NSDirectionalEdgeInsets(top: 25, leading: 23, bottom: 25, trailing: 23)
        translateBtn.layer.cornerRadius = 40
        
        hStack2.axis = .horizontal
        hStack2.alignment = .center
        hStack2.distribution = .equalCentering
        
        hangUpBtn.backgroundColor = .systemRed
        hangUpBtn.tintColor = .white
        hangUpBtn.configuration = .plain()
        hangUpBtn.setImage(UIImage(systemName: "phone.down.fill",withConfiguration: UIImage.SymbolConfiguration(font: .systemFont(ofSize: 30))), for: .normal)
        hangUpBtn.clipsToBounds = true
        hangUpBtn.invalidateIntrinsicContentSize()
        hangUpBtn.configuration?.contentInsets = NSDirectionalEdgeInsets(top: 25, leading: 16, bottom: 25, trailing: 16)
        hangUpBtn.layer.cornerRadius = 40
        hangUpBtn.addTarget(self, action: #selector(hangUpBtnAction), for: .touchUpInside)
    }
    
    func setConstraints() {
        collectionView.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.horizontalEdges.equalTo(view.safeAreaLayoutGuide)
            make.height.equalTo(view.safeAreaLayoutGuide).multipliedBy(0.65)
        }
        vStack.snp.makeConstraints { make in
            make.top.equalTo(collectionView.snp.bottom)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
            make.horizontalEdges.equalTo(view.safeAreaLayoutGuide)
        }
        hStack1.snp.makeConstraints { make in
            make.horizontalEdges.equalTo(vStack.layoutMarginsGuide)
        }
        
    }
    
    @objc func hangUpBtnAction() {
        CallService.shared.webRTCClient.endCall()
        dismiss(animated: true)
    }

}
//MARK: - CollectionViewDelegate
extension ConnectionViewController: UICollectionViewDelegate {
    
}
//MARK: - CollectionViewDataSource
extension ConnectionViewController: UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return randomNames.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: connectionCollectionViewCell, for: indexPath) as! ConnectionCollectionViewCell
        cell.nameLabel.text = randomNames[indexPath.row]
        
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        //6개까지 커버
        var cellWidth = 300
        switch randomNames.count {
        case 5...:
            cellWidth = Int((collectionView.frame.width - (40 * 2) - 20)/2)
            return CGSize(width: cellWidth, height: Int((collectionView.frame.height - (60 * 2))/3))
        case 3..<5:
            cellWidth = Int((collectionView.frame.width - (40 * 2) - 20)/2)
            collectionView.contentInset.top = ((collectionView.frame.height/2) - 50 - CGFloat(cellWidth))
            return CGSize(width: cellWidth, height: cellWidth)
        default:
            cellWidth = Int((collectionView.frame.width - (40 * 2)))
            if randomNames.count == 1 {
                collectionView.contentInset.top = ((collectionView.frame.height/2) - 40 - CGFloat(cellWidth/4))
            } else {
                collectionView.contentInset.top = ((collectionView.frame.height/2) - 50 - CGFloat(cellWidth/2))
            }
            return CGSize(width: cellWidth, height: cellWidth/2)
        }
    }
}

//MARK: - cell layout
extension ConnectionViewController: UICollectionViewDelegateFlowLayout {

    // 위 아래 간격
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        minimumLineSpacingForSectionAt section: Int
        ) -> CGFloat {
        return 20
    }

    // 옆 간격
    func collectionView(
      _ collectionView: UICollectionView,
      layout collectionViewLayout: UICollectionViewLayout,
      minimumInteritemSpacingForSectionAt section: Int
      ) -> CGFloat {
          return 20
      }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsets(top: 40, left: 40, bottom: 40, right: 40)
    }
}

// MARK: - canvas 이용하기
import SwiftUI
@available(iOS 13.0.0, *)
struct ConnectionViewPreview: PreviewProvider {
    static var previews: some View {
        ConnectionViewController().toPreview()
    }
}
