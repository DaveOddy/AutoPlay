package com.davidoddy.autoplay.preferences

import android.preference.CheckBoxPreference

/**
 * Created by doddy on 12/11/17.
 */
class CheckBoxPreferenceHelper : ICheckBoxPreferenceHelper {
    override fun setCurrentPreferenceDisplay(preference: CheckBoxPreference, onStringId: Int, offStringId: Int) {
        if (preference.isChecked) {
            preference.setSummary(onStringId)
        }
        else {
            preference.setSummary(offStringId)
        }
    }
}