package com.example.native_service_app

import android.content.Context
import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.EventChannel
import java.util.Objects

class MyStreamHandler(context: Context) : EventChannel.StreamHandler {

    private val handler = Handler(Looper.getMainLooper())

    private var eventSink: EventChannel.EventSink? = null

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
        val r: Runnable = object : Runnable{
            override fun run() {
                handler.post{
                    eventSink?.success("Hello from Flutter!")
                }
                handler.postDelayed(this,1000)
            }
        }
        handler.postDelayed(r,1000)
    }

    override fun onCancel(arguments: Any?) {
      eventSink = null
    }

}
