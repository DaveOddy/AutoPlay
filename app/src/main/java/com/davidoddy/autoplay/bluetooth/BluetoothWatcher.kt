package com.davidoddy.autoplay.bluetooth

import android.animation.ValueAnimator
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import com.davidoddy.autoplay.audio.IMediaLauncher
import com.davidoddy.autoplay.audio.IVolumeAdjuster
import com.davidoddy.autoplay.model.AppSettings
import com.davidoddy.autoplay.model.CountdownProgress
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

/**
 * Created by doddy on 12/6/17.
 */
class BluetoothWatcher(val context: Context
                       , private val sharedPreferences: SharedPreferences
                       , private val mediaLauncherFactory: IMediaLauncher.Factory
                       , private val audioManager: AudioManager
                       , private val volumeAdjuster: IVolumeAdjuster) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

        if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
            Timber.v("Device On: ${device?.name}")
            onDeviceTurnedOn(device)
        }
    }


    private fun onDeviceTurnedOn(device: BluetoothDevice?) {
        val settings = AppSettings.fromSharedPreferences(this.context, this.sharedPreferences)
        settings?.let {
            if (settings.deviceAddress == device?.address) {
                if (settings.delayInMilliseconds > 0) {
                    scheduleLaunch(settings)
                }
                else {
                    launchAudio(settings)
                }
            }
        }
    }


    private fun scheduleLaunch(settings: AppSettings) {
        if (settings.usePlaylist) {
            Timber.v("Scheduling playlist: ${settings.playlist}")
        }
        else {
            Timber.v("Scheduling audio")
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


    private fun launchAudio(settings: AppSettings) {
        Timber.v("Launching audio: ${settings.playlist}")
        this.mediaLauncherFactory.createForSettings(this.context, this.audioManager, settings).playMusic()
    }


    private fun setVolume(settings: AppSettings) {
        Timber.v("Setting volume: ${settings.volume }")
        this.volumeAdjuster.setVolume(settings.volume ?: return)
    }
}