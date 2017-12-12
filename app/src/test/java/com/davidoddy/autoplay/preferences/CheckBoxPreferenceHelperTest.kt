package com.davidoddy.autoplay.preferences

import android.preference.CheckBoxPreference
import org.junit.Test
import org.mockito.Mockito

/**
 * Created by doddy on 12/11/17.
 */
class CheckBoxPreferenceHelperTest {

    @Test
    fun setCurrentPreferenceDisplay_Sets_Correct_Id_When_On() {

        val onId = 1
        val offId = 2

        val preference = Mockito.mock(CheckBoxPreference::class.java)
        Mockito.`when`(preference.isChecked).thenReturn(true)

        CheckBoxPreferenceHelper().setCurrentPreferenceDisplay(preference, onId, offId)

        Mockito.verify(preference, Mockito.times(1)).setSummary(onId)
        Mockito.verify(preference, Mockito.times(0)).setSummary(offId)
    }

    @Test
    fun setCurrentPreferenceDisplay_Sets_Correct_Id_When_Off() {

        val onId = 1
        val offId = 2

        val preference = Mockito.mock(CheckBoxPreference::class.java)
        Mockito.`when`(preference.isChecked).thenReturn(false)

        CheckBoxPreferenceHelper().setCurrentPreferenceDisplay(preference, onId, offId)

        Mockito.verify(preference, Mockito.times(0)).setSummary(onId)
        Mockito.verify(preference, Mockito.times(1)).setSummary(offId)
    }
}