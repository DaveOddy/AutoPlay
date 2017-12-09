package com.davidoddy.autoplay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startForegroundService

/**
 * Created by doddy on 12/8/17.
 */
class StartupReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        startService(context)
    }

    private fun startService(context: Context?) {
        startForegroundService(context, Intent(context, WatcherService::class.java))
    }
}