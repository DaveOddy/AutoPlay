package com.davidoddy.autoplay.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView

/**
 * Created by doddy on 12/8/17.
 */

open class SliderPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    companion object {
        private const val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"

        private const val RESOURCE_DIALOG_MESSAGE = "dialogMessage"
        private const val RESOURCE_TEXT = "text"
        private const val RESOURCE_DEFAULT_VALUE = "defaultValue"
        private const val RESOURCE_MAX = "max"

        private const val RESOURCE_DEFAULT_VALUE_DEFAULT = 0
        private const val RESOURCE_MAX_DEFAULT = 100

        private const val LAYOUT_PADDING = 6
        private const val SPLASH_PADDING_HORIZONTAL = 30
        private const val SPLASH_PADDING_VERTICAL = 10

        private const val VALUE_TEXT_SIZE: Float = 32f


        private fun getResourceValue(context: Context, attrs: AttributeSet, resourceName: String): String? {
            val resourceId = attrs.getAttributeResourceValue(SliderPreference.ANDROID_NAMESPACE, resourceName, 0)
            return when (resourceId) {
                0 -> attrs.getAttributeValue(SliderPreference.ANDROID_NAMESPACE, resourceName)
                else -> context.getString(resourceId)
            }
        }
    }


    private var seekBar: SeekBar? = null
    private var splashText: TextView? = null
    private var valueText: TextView? = null

    var default: Int? = null

    var max: Int? = null
        set(value) {
            field = value
            this.seekBar?.max = value ?: RESOURCE_MAX_DEFAULT
        }

    private var value: Int? = null
        set(value) {
            field = value
            this.seekBar?.progress = value ?: 0
        }

    open fun getDisplayValue() : Int? {
        return calculateForDisplay(this.value)        // TODO: Can't use property getter because can't be mocked :(
    }

    var suffix: String? = null


    var displayCalculator: CalculateDisplayValue? = null


    init {
        this.dialogMessage = getResourceValue(context, attrs, RESOURCE_DIALOG_MESSAGE)
        this.suffix = getResourceValue(context, attrs, RESOURCE_TEXT)

        // TODO: Somehow these aren't properly reading the attributes
        this.default = attrs.getAttributeIntValue(ANDROID_NAMESPACE, RESOURCE_DEFAULT_VALUE, RESOURCE_DEFAULT_VALUE_DEFAULT)
        this.max = attrs.getAttributeIntValue(ANDROID_NAMESPACE, RESOURCE_MAX, RESOURCE_MAX_DEFAULT)
    }


    override fun onCreateDialogView(): View {

        val layout = createLayout()
        addSplashText(layout)
        addValueText(layout)
        addSeekBar(layout)

        getPersistedValue()

        setSeekBarState()

        return layout
    }


    private fun createLayout() : ViewGroup {
        val layout = LinearLayout(this.context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(LAYOUT_PADDING, LAYOUT_PADDING, LAYOUT_PADDING, LAYOUT_PADDING)
        return layout
    }


    private fun addSplashText(layout: ViewGroup) {
        this.splashText = TextView(this.context)
        this.splashText?.setPadding(SPLASH_PADDING_HORIZONTAL, SPLASH_PADDING_VERTICAL, SPLASH_PADDING_HORIZONTAL, SPLASH_PADDING_VERTICAL)
        if (this.dialogMessage != null) {
            this.splashText?.text = this.dialogMessage
        }
        layout.addView(this.splashText)
    }


    private fun addValueText(layout: ViewGroup) {
        this.valueText = TextView(this.context)
        this.valueText?.gravity = Gravity.CENTER_HORIZONTAL
        this.valueText?.textSize = VALUE_TEXT_SIZE
        layout.addView(this.valueText, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
    }


    private fun addSeekBar(layout: ViewGroup) {
        this.seekBar = SeekBar(this.context)
        this.seekBar?.setOnSeekBarChangeListener(this)
        layout.addView(this.seekBar)
    }


    private fun setSeekBarState() {
        this.seekBar?.max = this.max ?: 100
        this.seekBar?.progress = this.value ?: 0
    }


    private fun getPersistedValue() {
        if (shouldPersist()) {
            this.value = getPersistedInt(this.default ?: 0)
        }
    }


    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)

        this.seekBar?.max = this.max ?: 100
        this.seekBar?.progress = this.value ?: 0
    }


    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        super.onSetInitialValue(restorePersistedValue, defaultValue)

        this.value = when {
            restorePersistedValue && shouldPersist() -> getPersistedInt(this.default ?: 0)
            restorePersistedValue && !shouldPersist() -> 0
            else -> defaultValue as Int
        }
    }


    override fun showDialog(state: Bundle?) {
        super.showDialog(state)

        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        if (shouldPersist()) {
            this.value = this.seekBar?.progress
            persistInt(this.value ?: 0)
            callChangeListener(this.value)
        }

        dialog.dismiss()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val stringValue = calculateForDisplay(progress).toString()
        this.valueText?.text = when {
            this.suffix == null -> stringValue
            else -> "$stringValue ${this.suffix}"
        }
    }

    private fun calculateForDisplay(rawValue: Int?) : Int? {
        return this.displayCalculator?.calculate(rawValue) ?: rawValue
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit


    interface CalculateDisplayValue {
        fun calculate(rawValue: Int?): Int?
    }
}