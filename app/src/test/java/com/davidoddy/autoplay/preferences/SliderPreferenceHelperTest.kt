package com.davidoddy.autoplay.preferences

import com.davidoddy.autoplay.ui.SliderPreference
import junit.framework.Assert
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * Created by doddy on 12/12/17.
 */
class SliderPreferenceHelperTest {

    val MAX: Int = 100
    val DEFAULT: Int = 50

    @Test
    fun loadPreferenceSlider_Sets_Correct_Display_Calculator() {

        val displayCalculator = object : SliderPreference.CalculateDisplayValue {
            override fun calculate(rawValue: Int?): Int? {
                return rawValue;
            }
        }

        val preference = Mockito.mock(SliderPreference::class.java)

        SliderPreferenceHelper(displayCalculator).loadPreferenceSlider(preference, MAX, DEFAULT)

        Assert.assertEquals("Wrong calculator set.", displayCalculator, preference.displayCalculator)
    }

    @Test
    fun loadPreferenceSlider_Sets_Correct_Max_And_Default() {

        val preference = Mockito.mock(SliderPreference::class.java)

        SliderPreferenceHelper().loadPreferenceSlider(preference, MAX, DEFAULT)

        Assert.assertEquals("Wrong max set.", MAX, preference.max)
        Assert.assertEquals("Wrong default set.", DEFAULT, preference.default)
    }

    @Test
    fun loadPreferenceSlider_Sets_Correct_Display_With_Suffix_And_NonNull_Display_Value() {

        val SUFFIX = "TEST"
        val DISPLAY_VALUE = 100

        val preference = Mockito.mock(SliderPreference::class.java)
        preference.suffix = SUFFIX
        Mockito.`when`(preference.getDisplayValue()).thenReturn(DISPLAY_VALUE)

        SliderPreferenceHelper().setCurrentPreferenceDisplay(preference)

        val summaryCaptor = ArgumentCaptor.forClass(CharSequence::class.java)
        Mockito.verify(preference).setSummary(summaryCaptor.capture())

        Assert.assertEquals("Wrong summary with suffix.", "$DISPLAY_VALUE $SUFFIX", summaryCaptor.value)
    }

    @Test
    fun loadPreferenceSlider_Sets_Correct_Display_Without_Suffix_And_NonNull_Display_Value() {

        val SUFFIX = null
        val DISPLAY_VALUE = 100

        val preference = Mockito.mock(SliderPreference::class.java)
        preference.suffix = SUFFIX
        preference.displayCalculator
        Mockito.`when`(preference.getDisplayValue()).thenReturn(DISPLAY_VALUE)

        SliderPreferenceHelper().setCurrentPreferenceDisplay(preference)

        val summaryCaptor = ArgumentCaptor.forClass(CharSequence::class.java)
        Mockito.verify(preference).setSummary(summaryCaptor.capture())

        Assert.assertEquals("Wrong summary without suffix.", DISPLAY_VALUE.toString(), summaryCaptor.value)
    }

    @Test
    fun loadPreferenceSlider_Sets_Correct_Display_With_Suffix_And_Null_Display_Value() {

        val SUFFIX = "TEST"
        val DISPLAY_VALUE = null

        val preference = Mockito.mock(SliderPreference::class.java)
        preference.suffix = SUFFIX
        Mockito.`when`(preference.getDisplayValue()).thenReturn(DISPLAY_VALUE)

        SliderPreferenceHelper().setCurrentPreferenceDisplay(preference)

        val summaryCaptor = ArgumentCaptor.forClass(CharSequence::class.java)
        Mockito.verify(preference).setSummary(summaryCaptor.capture())

        Assert.assertEquals("Wrong summary with suffix.", "0 $SUFFIX", summaryCaptor.value)
    }

    @Test
    fun loadPreferenceSlider_Sets_Correct_Display_Without_Suffix_And_Null_Display_Value() {

        val SUFFIX = null
        val DISPLAY_VALUE = null

        val preference = Mockito.mock(SliderPreference::class.java)
        preference.suffix = SUFFIX
        Mockito.`when`(preference.getDisplayValue()).thenReturn(DISPLAY_VALUE)

        SliderPreferenceHelper().setCurrentPreferenceDisplay(preference)

        val summaryCaptor = ArgumentCaptor.forClass(CharSequence::class.java)
        Mockito.verify(preference).setSummary(summaryCaptor.capture())

        Assert.assertEquals("Wrong summary without suffix.", "0", summaryCaptor.value)
    }
}