package com.davidoddy.autoplay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import android.support.v4.content.ContextCompat.startForegroundService
import timber.log.Timber

/**
 * Created by doddy on 12/8/17.
 */
class StartupReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(ACTION_BOOT_COMPLETED)) {
            startService(context)
        }
    }

    private fun startService(context: Context?) {
        Timber.v("Starting service from system startup...")
        startForegroundService(context, Intent(context, WatcherService::class.java))
    }
}