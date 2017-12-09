package com.davidoddy.autoplay.bluetooth

import android.animation.ValueAnimator
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.util.Log
import com.davidoddy.autoplay.model.CountdownProgress
import com.davidoddy.autoplay.audio.IMediaLauncher
import com.davidoddy.autoplay.audio.IVolumeAdjuster
import com.davidoddy.autoplay.model.Settings
import org.greenrobot.eventbus.EventBus

/**
 * Created by doddy on 12/6/17.
 */
class BluetoothWatcher(val context: Context, val sharedPreferences: SharedPreferences, val mediaLauncherFactory: IMediaLauncher.Factory, val audioManager: AudioManager, val volumeAdjuster: IVolumeAdjuster) : BroadcastReceiver() {

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
                        setVolume(settings)
                    }
                }}

        animator.start()
    }


    private fun launchAudio(settings: Settings) {
        Log.v(TAG, "Launching audio: ${settings.playlist}")
        IMediaLauncher.createForSettings(this.context, this.audioManager, settings).playMusic()
    }


    private fun setVolume(settings: Settings) {
        Log.v(TAG, "Setting volume: ${settings.volume }")
        this.volumeAdjuster.setVolume(settings.volume ?: return)
    }
}