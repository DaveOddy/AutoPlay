package com.davidoddy.autoplay.preferences

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.preference.ListPreference

/**
 * Created by doddy on 12/11/17.
 */
class DevicePreferenceHelper(val bluetoothAdapter: BluetoothAdapter) : ListPreferenceHelper() {

    override fun loadPreferenceList(preference: ListPreference) {

        val entries = ArrayList<CharSequence>()
        val values = ArrayList<CharSequence>()

        this.bluetoothAdapter.bondedDevices
                .filter { it.bluetoothClass.hasService(BluetoothClass.Service.AUDIO) }
                .map {
                    entries.add(it.name)
                    values.add("${it.address}|${it.name}")
                }

        setArrays(preference
                , entries.toArray(Array<CharSequence>(0, {""}))
                , values.toArray(Array<CharSequence>(0, {""})))
    }
}