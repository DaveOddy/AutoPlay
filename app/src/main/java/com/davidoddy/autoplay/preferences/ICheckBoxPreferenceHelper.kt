package com.davidoddy.autoplay.preferences

import android.preference.CheckBoxPreference

/**
 * Created by doddy on 12/11/17.
 */
interface ICheckBoxPreferenceHelper {
    fun setCurrentPreferenceDisplay(preference: CheckBoxPreference, onStringId: Int, offStringId: Int)
}