package com.davidoddy.autoplay

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.preference.PreferenceManager
import android.util.Log
import com.davidoddy.autoplay.engine.BluetoothWatcher
import com.davidoddy.autoplay.engine.IMediaLauncher
import com.davidoddy.autoplay.engine.PlayMediaLauncher
import com.davidoddy.autoplay.engine.PlaylistProvider

class WatcherService : Service() {

    companion object {
        val TAG = WatcherService::class.simpleName
        const val CHANNEL: String = "DEFAULT_CHANNEL"
        const val NOTIFICATION_ID: Int = 1
    }

    private var broadcastReceiver : BroadcastReceiver? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(TAG, "Starting service...")

        startForeground()
        listenForBluetooth()

        return super.onStartCommand(intent, flags, startId)
    }


    private fun startForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = Notification.Builder(this, CHANNEL)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_text))
                .setContentIntent(pendingIntent)
                .build()

        startForeground(NOTIFICATION_ID, notification)
    }


    override fun onDestroy() {
        super.onDestroy()

        Log.v(TAG, "Destroying service...")
        unregisterReceiver(this.broadcastReceiver)
        this.broadcastReceiver = null
    }

    override fun onBind(intent: Intent?) = null


    private fun listenForBluetooth() {
        Log.v(TAG, "Configuring watcher...")
        this.broadcastReceiver = BluetoothWatcher(this.applicationContext, PreferenceManager.getDefaultSharedPreferences(this.applicationContext), IMediaLauncher.Factory)

        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        registerReceiver(this.broadcastReceiver, intentFilter)
    }

}
