package com.davidoddy.autoplay.model

import android.content.Context
import android.content.SharedPreferences
import com.davidoddy.autoplay.R

/**
 * Created by doddy on 12/7/17.
 */
class AppSettings(val deviceAddress: String, val deviceName: String, val usePlaylist: Boolean, val playlist: String?, val delayInMilliseconds: Long = LAUNCH_DELAY.toLong(), val volume: Int?) {

    companion object {
        const val LAUNCH_DELAY = 5000

        fun fromSharedPreferences(context: Context, sharedPreferences: SharedPreferences?): AppSettings? {
            val device = sharedPreferences?.getString(context.getString(R.string.pref_device), "") ?: return null
            val usePlaylist = sharedPreferences.getBoolean(context.getString(R.string.pref_use_playlist), false)
            var playlist = sharedPreferences.getString(context.getString(R.string.pref_playlist), "")
            val delayInMilliseconds = sharedPreferences.getInt(context.getString(R.string.pref_delay), LAUNCH_DELAY)
            var volume: Int? = sharedPreferences.getInt(context.getString(R.string.pref_volume), -1)

            if (device.isEmpty() || (usePlaylist && playlist.isEmpty())) {
                return null
            }

            if (playlist.isEmpty()) {
                playlist = null
            }

            if (volume == -1) {
                volume = null
            }

            val deviceAddress = device.split("|")[0]
            val deviceName= device.split("|")[1]

            return AppSettings(deviceAddress, deviceName, usePlaylist, playlist, delayInMilliseconds.toLong(), volume)
        }
    }
}