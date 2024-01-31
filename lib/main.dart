import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _platformVersion = "unKnown";
  static const _nativeServiceApp = MethodChannel('native_service_app');

  @override
  void initState() {
    super.initState();
    getPlatformVersion();
  }

  Future<void> getPlatformVersion() async {
    String platformVersion;
    try {
      platformVersion =
          await _nativeServiceApp.invokeMethod('getPlatformVersion') ??
              "Unknown platform version";
    } on PlatformException {
      platformVersion = "Failed to get platform version.";
    }
    if (!mounted) return;
    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> showNotification() async {
    try {
     await _nativeServiceApp.invokeMethod('getNotification');
    } catch (e) {
      debugPrint(e.toString());
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              "Platform Version : $_platformVersion",
              style: Theme.of(context).textTheme.headlineMedium,
            ),
            ElevatedButton(
                onPressed: () {
                  showNotification();
                },
                child: const Text("Show Notification"))
          ],
        ),
      ),
    );
  }
}
