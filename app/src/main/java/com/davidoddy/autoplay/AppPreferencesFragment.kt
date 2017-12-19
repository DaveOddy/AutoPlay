package com.davidoddy.autoplay

import android.bluetooth.BluetoothAdapter
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.davidoddy.autoplay.playlist.PlaylistProvider
import com.davidoddy.autoplay.preferences.*
import com.davidoddy.autoplay.ui.SliderPreference


class AppPreferencesFragment : PreferenceFragment() {

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
        DevicePreferenceHelper(BluetoothAdapter.getDefaultAdapter())
                .loadPreferenceList(findPreference(getString(R.string.pref_device)) as ListPreference)

        PlaylistPreferenceHelper(PlaylistProvider(this.context.contentResolver))
                .loadPreferenceList(findPreference(getString(R.string.pref_playlist)) as ListPreference)

        SliderPreferenceHelper(createSliderDisplayCalculatorForDelay())
                .loadPreferenceSlider(
                        findPreference(getString(R.string.pref_delay)) as SliderPreference
                        , resources.getInteger(R.integer.pref_delay_max)
                        , resources.getInteger(R.integer.pref_delay_default))

        SliderPreferenceHelper()
                .loadPreferenceSlider(
                        findPreference(getString(R.string.pref_volume)) as SliderPreference
                        , resources.getInteger(R.integer.pref_volume_max)
                        , resources.getInteger(R.integer.pref_volume_default))

        setCurrentValue(getString(R.string.pref_use_playlist))
    }


    private fun createSliderDisplayCalculatorForDelay() = object : SliderPreference.CalculateDisplayValue {
        override fun calculate(rawValue: Int?): Int? {
            rawValue ?: return null
            return rawValue / 1000
        }
    }


    private fun setListener() {
        this.changePreferenceListener = SharedPreferences.OnSharedPreferenceChangeListener {_, key: String -> run {
            setCurrentValue(key)
        }}

        PreferenceManager.getDefaultSharedPreferences(this.context.applicationContext).registerOnSharedPreferenceChangeListener(this.changePreferenceListener)
    }

    private fun setCurrentValue(key: String) {
        val preference = findPreference(key)
        when (preference) {
            is ListPreference -> ListPreferenceHelper().setCurrentPreferenceDisplay(preference)
            is SliderPreference -> when (preference.key) {
                getString(R.string.pref_delay) -> SliderPreferenceHelper(createSliderDisplayCalculatorForDelay()).setCurrentPreferenceDisplay(preference)
                else -> SliderPreferenceHelper(null).setCurrentPreferenceDisplay(preference)
            }
            is CheckBoxPreference -> when (preference.key) {
                getString(R.string.pref_use_playlist) ->
                    CheckBoxPreferenceHelper().setCurrentPreferenceDisplay(
                            preference,
                            R.string.pref_use_playlist_summary_on,
                            R.string.pref_use_playlist_summary_off)
            }
        }
    }
}