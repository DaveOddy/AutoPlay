package com.davidoddy.autoplay.preferences

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.preference.ListPreference

/**
 * Created by doddy on 12/11/17.
 */
class DevicePreferenceHelper(val bluetoothAdapter: BluetoothAdapter) : ListPreferenceHelper() {

    override fun loadPreferenceList(preference: ListPreference) {

        val devices = this.bluetoothAdapter.bondedDevices
                .asSequence()
                .filter { it.bluetoothClass.hasService(BluetoothClass.Service.AUDIO) }
                .sortedBy { it.name.toLowerCase() }
                .toList()

        val entries: Array<CharSequence> = devices
                .map { it.name }
                .toTypedArray()

        val values: Array<CharSequence> = devices
                .map { "${it.address}|${it.name}" }
                .toTypedArray()

        setArrays(preference, entries, values)
    }
}