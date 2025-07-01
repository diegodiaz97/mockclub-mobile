import UIKit
import SwiftUI
import composeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.keyboard)
                .ignoresSafeArea(.container) // Compose has own keyboard handler
                .edgesIgnoringSafeArea(.top) // Extends color to status bar
    }
}

struct ContentVire_Previews: PreviewProvider {
    static var previews: some View{
        ContentView()
    }
}
