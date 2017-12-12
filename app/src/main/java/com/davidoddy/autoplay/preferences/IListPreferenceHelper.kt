package com.davidoddy.autoplay.preferences

import android.preference.ListPreference

/**
 * Created by doddy on 12/11/17.
 */
interface IListPreferenceHelper {
    fun loadPreferenceList(preference: ListPreference)
    fun setCurrentPreferenceDisplay(preference: ListPreference)
}