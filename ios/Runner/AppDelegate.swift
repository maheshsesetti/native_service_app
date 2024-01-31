import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate{
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
      GeneratedPluginRegistrant.register(with: self)
      MyMethodChannelHandler.register(with :self.registrar(forPlugin: "MyMethodChannelHandler")!)
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}

public class MyMethodChannelHandler: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let methodChannel = FlutterMethodChannel(name: "native_service_app", binaryMessenger: registrar.messenger())
        let instance = MyMethodChannelHandler()
        registrar.addMethodCallDelegate(instance, channel: methodChannel)
    }
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "getPlatformVersion":
            let version = String(UIDevice.current.systemVersion)
            result(version)
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
