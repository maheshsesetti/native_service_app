package com.example.native_service_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.EventChannel


class MyStreamHandler(private val context: Context) : EventChannel.StreamHandler {

    private val handler = Handler(Looper.getMainLooper())

    private var eventSink: EventChannel.EventSink? = null

    private val CHANNEL_ID = "Event_Notification_channel"

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {

        eventSink = events
        val r: Runnable = object : Runnable{
            override fun run() {
                handler.post{
                    showNotification(events)
                    eventSink?.success(null)
                }
                handler.postDelayed(this,1000)
            }
        }
        handler.postDelayed(r,1000)
    }

    override fun onCancel(arguments: Any?) {
      eventSink = null
    }

    private fun showNotification(events: EventChannel.EventSink?) {
        createNotificationChannelMethod()
        println("events is printing $events")
//        val intent = Intent(context, MainActivity::class.java)
//        intent.action = Intent.ACTION_RUN
//        intent.putExtra("flutter_screen", events.toString())
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(context,0,
//            intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context,CHANNEL_ID).
        setSmallIcon(R.drawable.launch_background).
        setContentTitle("My Event Notification").
        setContentText("Much longer text that cannot fit one line...").
        setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line...")).
        setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        setContentIntent(pendingIntent).
//        setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(1,builder.build())

        }
    }


    private fun createNotificationChannelMethod() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val nfChannel = NotificationChannel(CHANNEL_ID,"Your Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.BLUE
                canShowBadge()
                enableLights(true)
            }
            val notificationManager: NotificationManager = context.getSystemService(FlutterActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(nfChannel)
        }
    }

}
