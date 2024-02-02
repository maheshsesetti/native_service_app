package com.example.native_service_app


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast


import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler


class MainActivity: FlutterActivity(), MethodCallHandler {

    private lateinit  var methodChannel:MethodChannel

    private lateinit  var eventChannel:EventChannel

    private var channel = "native_service_app"

    private var event_channel = "event_native_service_app"

    private val CHANNEL_ID = "Notification_channel"

    private val action = "snooze"

    private lateinit var receiver: AirplaneModeChangeReceiver




    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannelMethod()
        receiver = AirplaneModeChangeReceiver()

        val intentFilter: IntentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)

        registerReceiver(receiver,intentFilter)
    }


    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }


    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger,channel)
        eventChannel = EventChannel(flutterEngine.dartExecutor.binaryMessenger,event_channel)
        eventChannel.setStreamHandler(
            MyStreamHandler(context)
        )
        methodChannel.setMethodCallHandler(this)
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when(call.method)  {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            } "getNotification" -> {
                val screen = call.argument<String>("screen")
                showNotification(screen)
                result.success(null)
            } "isConnected" ->{
               if(isConnected()) {
                   Toast.makeText(context, "Internet Mode Enabled", Toast.LENGTH_LONG).show()
               } else {
                   Toast.makeText(context, "Internet Mode Disabled", Toast.LENGTH_LONG).show()
               }
               result.success(null)
            } "send_message" -> {

                result.success(null)
            }else -> {
                    result.notImplemented()
            }
        }
    }




    private fun isConnected() : Boolean {
           val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
           val networkInfo = connectivityManager.activeNetworkInfo
           return  networkInfo != null && networkInfo.isConnectedOrConnecting
       }


    private fun showNotification(screen:String?) {
        createNotificationChannelMethod()
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Intent.ACTION_RUN
        intent.putExtra("flutter_screen", screen)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context,0,
            intent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this,CHANNEL_ID).
            setSmallIcon(R.drawable.launch_background).
            setContentTitle("My Notification").
            setContentText("Much longer text that cannot fit one line...").
            setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line...")).
            setPriority(NotificationCompat.PRIORITY_DEFAULT).
            setContentIntent(pendingIntent).
            setAutoCancel(true)
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





