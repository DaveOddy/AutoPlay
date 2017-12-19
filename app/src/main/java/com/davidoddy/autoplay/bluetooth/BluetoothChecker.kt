package com.davidoddy.autoplay.bluetooth

import android.bluetooth.BluetoothAdapter

/**
 * Created by doddy on 12/8/17.
 */
class BluetoothChecker(private val adapter: BluetoothAdapter?) : IBluetoothChecker {
    override fun isBluetoothEnabled() = this.adapter?.isEnabled ?: false
}