package com.davidoddy.autoplay.preferences

import android.preference.ListPreference

/**
 * Created by doddy on 12/11/17.
 */
open class ListPreferenceHelper : IListPreferenceHelper {

    override fun loadPreferenceList(preference: ListPreference) {}

    override fun setCurrentPreferenceDisplay(preference: ListPreference) {
        preference.summary = preference.entry
    }

    fun setArrays(preference: ListPreference, entriesArray: Array<CharSequence>, valuesArray: Array<CharSequence>) {

        preference.entries = entriesArray
        preference.entryValues = valuesArray

        if (valuesArray.isNotEmpty()) {
            preference.setDefaultValue(valuesArray[0])
        }

        setCurrentPreferenceDisplay(preference)
    }
}