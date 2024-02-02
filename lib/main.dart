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
      routes: {
        '/secondScreen': (context) => const SecondScreen(),
      },
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
  static const _eventNativeApp = EventChannel('event_native_service_app');

  @override
  void initState() {
    super.initState();
    getPlatformVersion();
  }

  void startCommunication() {
    _eventNativeApp.receiveBroadcastStream().listen((event) {
      event = "Hello Wold";
      debugPrint('Received from Native: $event');
    });
  }

  void startSendNotification() {
    _eventNativeApp.receiveBroadcastStream().listen((event) {
      event = "/secondScreen";
      debugPrint('Received from Native: $event');
    });
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
                onPressed: () async {
                  await _nativeServiceApp.invokeMethod(
                    'getNotification',
                  );
                },
                child: const Text("Basic Notification")),
            ElevatedButton(
                onPressed: () async {
                  await _nativeServiceApp.invokeMethod(
                    'isConnected',
                  );
                },
                child: const Text("Network_Connection")),
            ElevatedButton(
                onPressed: () {
                  startCommunication();
                },
                child: const Text("Send Message")),
                ElevatedButton(
                onPressed: () {
                  startSendNotification();
                },
                child: const Text("Send Event Notification")),
            ElevatedButton(
                onPressed: () {
                  Navigator.pushNamed(context, '/secondScreen');
                },
                child: const Text("Next Page"))
          ],
        ),
      ),
    );
  }
}

class SecondScreen extends StatefulWidget {
  const SecondScreen({super.key});

  @override
  State<SecondScreen> createState() => _SecondScreenState();
}

class _SecondScreenState extends State<SecondScreen> {
  static const _nativeServiceApp = MethodChannel('native_service_app');
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text("Second Screen"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            ElevatedButton(
                onPressed: () async {
                  await _nativeServiceApp.invokeMethod(
                      'getNotification', {'screen': '/secondScreen'});
                },
                child: const Text("Basic Notification"))
          ],
        ),
      ),
    );
  }
}
