package com.example.native_service_app

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

 class AirplaneModeChangeReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            if(networkInfo != null && networkInfo.isConnected) {
                Toast.makeText(context, "Internet Mode Enabled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Internet Mode Disabled", Toast.LENGTH_LONG).show()
            }
        }
//        val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return
//        if (isAirplaneModeEnabled) {
//            Toast.makeText(context, "Airplane Mode Enabled", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(context, "Airplane Mode Disabled", Toast.LENGTH_LONG).show()
//        }

    }
}
