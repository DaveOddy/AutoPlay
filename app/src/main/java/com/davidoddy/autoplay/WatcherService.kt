package com.davidoddy.autoplay

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.preference.PreferenceManager
import com.davidoddy.autoplay.audio.IMediaLauncher
import com.davidoddy.autoplay.audio.VolumeAdjuster
import com.davidoddy.autoplay.bluetooth.BluetoothWatcher
import timber.log.Timber

class WatcherService : Service() {

    companion object {
        const val NOTIFICATION_CHANNEL: String = "DEFAULT_CHANNEL"
        const val NOTIFICATION_ID: Int = 1
    }

    private var broadcastReceiver : BroadcastReceiver? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.v("Starting service...")

        startForeground()
        listenForBluetooth()

        return super.onStartCommand(intent, flags, startId)
    }


    private fun startForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = Notification.Builder(this, NOTIFICATION_CHANNEL)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_text))
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
                .build()
        notification.flags = Notification.FLAG_ONGOING_EVENT or Notification.FLAG_NO_CLEAR
        startForeground(NOTIFICATION_ID, notification)
    }


    override fun onDestroy() {
        super.onDestroy()

        Timber.v("Destroying service...")
        unregisterReceiver(this.broadcastReceiver)
        this.broadcastReceiver = null
    }

    override fun onBind(intent: Intent?) = null


    private fun listenForBluetooth() {
        Timber.v("Configuring watcher...")
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        this.broadcastReceiver = BluetoothWatcher(this.applicationContext
                , PreferenceManager.getDefaultSharedPreferences(this.applicationContext)
                , IMediaLauncher
                , audioManager
                , VolumeAdjuster(audioManager))

        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        registerReceiver(this.broadcastReceiver, intentFilter)
    }

}
