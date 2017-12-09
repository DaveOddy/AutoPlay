package com.davidoddy.autoplay.model

import android.content.Context
import android.content.SharedPreferences
import com.davidoddy.autoplay.R

/**
 * Created by doddy on 12/7/17.
 */
data class Settings(val deviceAddress: String, val deviceName: String, val usePlaylist: Boolean, val playlist: String, val delayInMilliseconds: Long = LAUNCH_DELAY.toLong(), val volume: Int?) {

    companion object {
        const val LAUNCH_DELAY = 5000

        fun fromSharedPreferences(context: Context, sharedPreferences: SharedPreferences?): Settings? {
            val device = sharedPreferences?.getString(context.getString(R.string.pref_device), "") ?: return null
            val usePlaylist = sharedPreferences.getBoolean(context.getString(R.string.pref_use_playlist), false)
            val playlist = sharedPreferences.getString(context.getString(R.string.pref_playlist), "")
            val delayInMilliseconds = sharedPreferences.getInt(context.getString(R.string.pref_delay), LAUNCH_DELAY)
            var volume: Int? = sharedPreferences.getInt(context.getString(R.string.pref_volume), -1)

            if (device.length == 0 || (usePlaylist && playlist.length == 0)) {
                return null
            }

            if (volume == -1) {
                volume = null
            }

            val deviceAddress = device.split("|")[0]
            val deviceName= device.split("|")[1]

            return Settings(deviceAddress, deviceName, usePlaylist, playlist, delayInMilliseconds.toLong(), volume)
        }
    }
}