import UIKit

@objc class UIHelper: NSObject {
    @objc static func setStatusBarColor(color: UIColor) {
        if let window = UIApplication.shared.windows.first {
            let statusBarFrame = window.windowScene?.statusBarManager?.statusBarFrame ?? CGRect.zero
            let statusBarView = UIView(frame: statusBarFrame)
            statusBarView.backgroundColor = color
            window.addSubview(statusBarView)
        }
    }
}
