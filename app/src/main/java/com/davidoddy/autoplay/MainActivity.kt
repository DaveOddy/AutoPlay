package com.davidoddy.autoplay

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.davidoddy.autoplay.bluetooth.BluetoothChecker
import com.davidoddy.autoplay.model.AppSettings
import com.davidoddy.autoplay.model.CountdownProgress
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener(::launchPreferences)

        startService()
    }


    private fun startService() {
        startForegroundService(Intent(this, WatcherService::class.java))
    }


    override public fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }


    override public fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }


    override fun onResume() {
        super.onResume()
        setupUI(PreferenceManager.getDefaultSharedPreferences(this.applicationContext))
    }


    private fun setupUI(sharedPreferences: SharedPreferences?) {
        val settings = AppSettings.fromSharedPreferences(this.applicationContext, sharedPreferences)
        val view : TextView = findViewById(R.id.specification_text)
        if (settings == null) {
            view.setText(R.string.home_page_empty)
        }
        else {
            view.text = formatConfigurationForTextView(settings)
        }
    }


    private fun formatConfigurationForTextView(settings: AppSettings): CharSequence {
        return if (settings.usePlaylist) {
            Html.fromHtml(String.format(resources.getString(R.string.home_page_pattern_playlist), settings.playlist, settings.deviceName, settings.delayInMilliseconds / 1000), Html.FROM_HTML_MODE_COMPACT)
        }
        else {
            Html.fromHtml(String.format(resources.getString(R.string.home_page_pattern_resume), settings.deviceName, settings.delayInMilliseconds / 1000), Html.FROM_HTML_MODE_COMPACT)
        }
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        setupUI(sharedPreferences)
    }


    private fun launchPreferences(view: View) {
        if (checkBluetooth()) {
            startActivity(Intent(this, AppPreferencesActivity::class.java))
        }
        else {
            showBluetoothMessage(view)
        }
    }


    private fun showBluetoothMessage(view: View) {
        Snackbar.make(view, R.string.bluetooth_snackbar_text , Snackbar.LENGTH_LONG).show()
    }


    private fun checkBluetooth() = BluetoothChecker(BluetoothAdapter.getDefaultAdapter()).isBluetoothEnabled()


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onCountdownEvent(event: CountdownProgress) {
        val progress = findViewById<ProgressBar>(R.id.countdownProgress)
        progress.progress = event.percentageComplete
        progress.visibility = shouldCountdownShow(event.percentageComplete)
    }


    private fun shouldCountdownShow(percentage: Int) =
            when (percentage) {
                in 1..99 -> View.VISIBLE
                else -> View.INVISIBLE
            }
}
