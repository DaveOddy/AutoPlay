package com.davidoddy.autoplay

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass.Service.AUDIO
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.davidoddy.autoplay.playlist.PlaylistProvider
import com.davidoddy.autoplay.ui.SliderPreference


class SettingsFragment : PreferenceFragment() {

    private var changePreferenceListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.preferences)
        loadPreferences()
        setListener()
    }


    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this.context.applicationContext).unregisterOnSharedPreferenceChangeListener(this.changePreferenceListener)

    }


    private fun loadPreferences() {
        loadDevices()
        loadPlaylists()
        loadDelay()
        loadVolume()
    }


    private fun loadDevices() {

        val entries = ArrayList<CharSequence>()
        val values = ArrayList<CharSequence>()

        val bondedDevices = BluetoothAdapter.getDefaultAdapter().bondedDevices
        for (device in bondedDevices) {
            if (device.bluetoothClass.hasService(AUDIO)) {
                entries.add(device.name)
                values.add(device.address + "|" + device.name)
            }
        }

        val key = resources.getString(R.string.pref_device)
        val preference = findPreference(key) as ListPreference
        preference.entries = entries.toArray(Array<CharSequence>(0, {""}))
        preference.entryValues = values.toArray(Array<CharSequence>(0, {""}))
        preference.setDefaultValue(values[0])

        setCurrentValue(key)
    }

    private fun loadPlaylists() {

        val values = ArrayList<CharSequence>()

        val playlists = PlaylistProvider(context.applicationContext).getPlaylists()
        for (playlist in playlists) {
            values.add(playlist)
        }

        val key = resources.getString(R.string.pref_playlist)
        val preference = findPreference(key) as ListPreference
        preference.entries = values.toArray(Array<CharSequence>(0, {""}))
        preference.entryValues = preference.entries
        preference.setDefaultValue(values[0])

        setCurrentValue(key)
        setCurrentValue(getString(R.string.pref_use_playlist))
    }

    private fun loadDelay() {
        val key = resources.getString(R.string.pref_delay)
        val preference = (findPreference(key) as SliderPreference)
        preference.displayCalculator = object : SliderPreference.CalculateDisplayValue {
            override fun calculate(rawValue: Int?) : Int? {
                rawValue ?: return null
                return rawValue / 1000
            }
        }
        preference.max = resources.getInteger(R.integer.pref_delay_max)
        preference.default = resources.getInteger(R.integer.pref_delay_default)

        setCurrentValue(key)
    }

    private fun loadVolume() {
        val key = resources.getString(R.string.pref_volume)
        val preference = (findPreference(key) as SliderPreference)
        preference.max = resources.getInteger(R.integer.pref_volume_max)
        preference.default = resources.getInteger(R.integer.pref_volume_default)
        setCurrentValue(key)
    }

    private fun setListener() {

        this.changePreferenceListener = SharedPreferences.OnSharedPreferenceChangeListener({_, key: String -> run {
            setCurrentValue(key)
        }})

        PreferenceManager.getDefaultSharedPreferences(this.context.applicationContext).registerOnSharedPreferenceChangeListener(this.changePreferenceListener)
    }

    private fun setCurrentValue(key: String) {
        val preference = findPreference(key)
        when (preference) {
            is ListPreference -> preference.setSummary(preference.entry)
            is SliderPreference -> {
                preference.setSummary(when {
                    preference.suffix == null -> "${preference.displayValue ?: 0}"
                    else -> "${preference.displayValue ?: 0} ${preference.suffix}"
                })
            }
            is CheckBoxPreference -> {
                if (preference.key.equals(getString(R.string.pref_use_playlist))) {
                    if (preference.isChecked) {
                        preference.setSummary(R.string.pref_use_playlist_summary_on)
                    }
                    else {
                        preference.setSummary(R.string.pref_use_playlist_summary_off)
                    }
                }
            }
        }
    }
}