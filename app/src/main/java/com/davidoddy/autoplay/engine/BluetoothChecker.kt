package com.davidoddy.autoplay.engine

import android.bluetooth.BluetoothAdapter

/**
 * Created by doddy on 12/8/17.
 */
class BluetoothChecker(val adapter: BluetoothAdapter?) : IBluetoothChecker {
    override fun isBluetoothEnabled() = this.adapter?.isEnabled ?: false
}