package com.davidoddy.autoplay.engine

import android.animation.ValueAnimator
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.util.Log
import org.greenrobot.eventbus.EventBus

/**
 * Created by doddy on 12/6/17.
 */
class BluetoothWatcher(val context: Context, val sharedPreferences: SharedPreferences, val mediaLauncherFactory: IMediaLauncher.Factory) : BroadcastReceiver() {

    companion object {
        val TAG = BluetoothWatcher::class.simpleName
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

        if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
            Log.v(TAG, "Device On: ${device?.name}")
            onDeviceTurnedOn(device)
        }
    }


    private fun onDeviceTurnedOn(device: BluetoothDevice?) {
        val settings = Settings.fromSharedPreferences(this.context, this.sharedPreferences) ?: return
        if (settings.deviceAddress == device?.address) {
            if (settings.delayInMilliseconds > 0) {
                scheduleLaunch(settings)
            }
            else {
                launchAudio(settings)
            }
        }
    }


    private fun scheduleLaunch(settings: Settings) {
        if (settings.usePlaylist) {
            Log.v(TAG, "Scheduling playlist: ${settings.playlist}")
        }
        else {
            Log.v(TAG, "Scheduling audio")
        }

        val animator: ValueAnimator = ValueAnimator.ofInt(0, 100)

        animator.setDuration(settings.delayInMilliseconds)

                .addUpdateListener { animation: ValueAnimator? -> run {
                    val currentValue = animation?.animatedValue as Int
                    EventBus.getDefault().post(CountdownProgress(currentValue))
                    if (currentValue == 100) {
                        launchAudio(settings)
                    }
                }}

        animator.start()
    }


    private fun launchAudio(settings: Settings) {
        Log.v(TAG, "Launching audio: ${settings.playlist}")
        mediaLauncherFactory.createForSettings(this.context, this.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager, settings).playMusic()
    }
}