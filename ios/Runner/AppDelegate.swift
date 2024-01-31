import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate{
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
      
     
      let flutterViewController: FlutterViewController = window?.rootViewController as! FlutterViewController
      
      let notificationChannel = FlutterMethodChannel(name: "native_service_app", binaryMessenger: flutterViewController.binaryMessenger)
      
      notificationChannel.setMethodCallHandler{
          [weak self] (call: FlutterMethodCall, result: @escaping FlutterResult) in
          self?.handleNotificationMethodCall(call, result)
      }
      
    
      GeneratedPluginRegistrant.register(with: self)
    
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
    
    private func handleNotificationMethodCall(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        switch call.method {
        case "getPlatformVersion":
            let version = String(UIDevice.current.systemVersion)
            result(version)
        case "getNotification":
            showNotification()
            result(nil)
        default:
            result(FlutterMethodNotImplemented)
        }
    }
    
    public func showNotification() {
        let center = UNUserNotificationCenter.current()
        let content = UNMutableNotificationContent()
        content.title = "My notification"
        content.body = "Much longer text that cannot fit one line..."
        content.sound = UNNotificationSound.default
        
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 1, repeats: false)
        
        let request = UNNotificationRequest(identifier: "flutterNotification", content: content, trigger: trigger)
        
        center.add(request) {
            error in
            if let error = error {
                print("Error adde in Notifcation request: \(error.localizedDescription)")
            } else {
                print("Notification works successfully")
            }
        }
        
    }
}


