package com.davidoddy.autoplay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startForegroundService
import timber.log.Timber

/**
 * Created by doddy on 12/8/17.
 */
class StartupReceiver : BroadcastReceiver() {

    companion object {
        val TAG = StartupReceiver::class.simpleName
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        startService(context)
    }

    private fun startService(context: Context?) {
        Timber.v("Starting service from system startup...")
        startForegroundService(context, Intent(context, WatcherService::class.java))
    }
}