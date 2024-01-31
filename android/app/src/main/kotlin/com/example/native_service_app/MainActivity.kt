package com.example.native_service_app


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle


import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler


class MainActivity: FlutterActivity(), MethodCallHandler {

    private lateinit  var methodChannel:MethodChannel


    private var channel = "native_service_app"

    private val CHANNEL_ID = "your_channel_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannelMethod()
    }


    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger,channel)
        methodChannel.setMethodCallHandler(this)
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when(call.method)  {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            } "getNotification" -> {
            showNotification()
            result.success(null)
            } else -> {
                result.notImplemented()
        }
        }
    }

    private fun showNotification() {
        createNotificationChannelMethod()
       val builder = NotificationCompat.Builder(this,CHANNEL_ID).
       setSmallIcon(R.drawable.launch_background).
       setContentTitle("My Notification").
       setContentText("Much longer text that cannot fit one line...").
       setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line...")).
       setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(0,builder.build())
        }
    }

    private fun createNotificationChannelMethod() {

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val nfChannel = NotificationChannel(CHANNEL_ID,"Your Channel Name",NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.BLUE
                canShowBadge()
                enableLights(true)
            }
            val notificationManager:NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(nfChannel)

        }
    }


}
