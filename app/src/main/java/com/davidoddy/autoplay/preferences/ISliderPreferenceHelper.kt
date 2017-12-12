package com.davidoddy.autoplay.preferences

import com.davidoddy.autoplay.ui.SliderPreference

/**
 * Created by doddy on 12/11/17.
 */
interface ISliderPreferenceHelper {
    fun loadPreferenceSlider(preference: SliderPreference, max: Int, default: Int)
    fun setCurrentPreferenceDisplay(preference: SliderPreference)
}