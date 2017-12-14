package com.davidoddy.autoplay.preferences

import com.davidoddy.autoplay.ui.SliderPreference

/**
 * Created by doddy on 12/11/17.
 */
class SliderPreferenceHelper(private val displayCalculator: SliderPreference.CalculateDisplayValue? = null) : ISliderPreferenceHelper {
    override fun loadPreferenceSlider(preference: SliderPreference, max: Int, default: Int) {
        preference.displayCalculator = this.displayCalculator
        preference.max = max
        preference.default = default

        setCurrentPreferenceDisplay(preference)
    }

    override fun setCurrentPreferenceDisplay(preference: SliderPreference) {
        preference.summary = when {
            preference.suffix == null -> "${preference.getDisplayValue() ?: 0}"
            else -> "${preference.getDisplayValue() ?: 0} ${preference.suffix}"
        }
    }

}