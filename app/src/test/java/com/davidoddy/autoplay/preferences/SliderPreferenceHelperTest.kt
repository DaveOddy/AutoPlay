package com.davidoddy.autoplay.preferences

import com.davidoddy.autoplay.ui.SliderPreference
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * Created by doddy on 12/12/17.
 */
class SliderPreferenceHelperTest {

    companion object {
        const private val MAX: Int = 100
        const private val DEFAULT: Int = 50
    }

    @Test
    fun loadPreferenceSlider_Sets_Correct_Display_Calculator() {

        val displayCalculator = object : SliderPreference.CalculateDisplayValue {
            override fun calculate(rawValue: Int?): Int? {
                return rawValue
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

        val suffix = "TEST"
        val displayValue = 100

        val preference = Mockito.mock(SliderPreference::class.java)
        preference.suffix = suffix
        Mockito.`when`(preference.getDisplayValue()).thenReturn(displayValue)

        SliderPreferenceHelper().setCurrentPreferenceDisplay(preference)

        val summaryCaptor = ArgumentCaptor.forClass(CharSequence::class.java)
        Mockito.verify(preference).summary = summaryCaptor.capture()

        Assert.assertEquals("Wrong summary with suffix.", "$displayValue $suffix", summaryCaptor.value)
    }

    @Test
    fun loadPreferenceSlider_Sets_Correct_Display_Without_Suffix_And_NonNull_Display_Value() {

        val suffix = null
        val displayValue = 100

        val preference = Mockito.mock(SliderPreference::class.java)
        preference.suffix = suffix
        preference.displayCalculator
        Mockito.`when`(preference.getDisplayValue()).thenReturn(displayValue)

        SliderPreferenceHelper().setCurrentPreferenceDisplay(preference)

        val summaryCaptor = ArgumentCaptor.forClass(CharSequence::class.java)
        Mockito.verify(preference).summary = summaryCaptor.capture()

        Assert.assertEquals("Wrong summary without suffix.", displayValue.toString(), summaryCaptor.value)
    }

    @Test
    fun loadPreferenceSlider_Sets_Correct_Display_With_Suffix_And_Null_Display_Value() {

        val suffix = "TEST"
        val displayValue = null

        val preference = Mockito.mock(SliderPreference::class.java)
        preference.suffix = suffix
        Mockito.`when`(preference.getDisplayValue()).thenReturn(displayValue)

        SliderPreferenceHelper().setCurrentPreferenceDisplay(preference)

        val summaryCaptor = ArgumentCaptor.forClass(CharSequence::class.java)
        Mockito.verify(preference).summary = summaryCaptor.capture()

        Assert.assertEquals("Wrong summary with suffix.", "0 $suffix", summaryCaptor.value)
    }

    @Test
    fun loadPreferenceSlider_Sets_Correct_Display_Without_Suffix_And_Null_Display_Value() {

        val suffix = null
        val displayValue = null

        val preference = Mockito.mock(SliderPreference::class.java)
        preference.suffix = suffix
        Mockito.`when`(preference.getDisplayValue()).thenReturn(displayValue)

        SliderPreferenceHelper().setCurrentPreferenceDisplay(preference)

        val summaryCaptor = ArgumentCaptor.forClass(CharSequence::class.java)
        Mockito.verify(preference).summary = summaryCaptor.capture()

        Assert.assertEquals("Wrong summary without suffix.", "0", summaryCaptor.value)
    }
}