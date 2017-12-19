package com.davidoddy.autoplay.model

import android.content.Context
import android.content.SharedPreferences
import com.davidoddy.autoplay.R
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

/**
 * Created by doddy on 12/9/17.
 */
class AppSettingsTest {

    private val deviceAddress = "12:34:56:78:90"
    private val deviceName = "Test Device"
    private val playlist = "Test Playlist"
    private val volume = 85

    private val defaultDelay = 5000
    private val defaultVolume = -1

    @Test
    @Suppress
    fun fromSharedPreferences_Returns_Proper_Settings_For_Playlist_Configuration() {

        val sharedPreferences = Mockito.mock(SharedPreferences::class.java)

        Mockito.`when`(sharedPreferences.getString("pref_device", "")).thenReturn("$deviceAddress|$deviceName")
        Mockito.`when`(sharedPreferences.getBoolean("pref_use_playlist", false)).thenReturn(true)
        Mockito.`when`(sharedPreferences.getString("pref_playlist", "")).thenReturn(playlist)
        Mockito.`when`(sharedPreferences.getInt("pref_delay", defaultDelay)).thenReturn(defaultDelay)
        Mockito.`when`(sharedPreferences.getInt("pref_volume", defaultVolume)).thenReturn(volume)

        val settings = AppSettings.fromSharedPreferences(createMockContext(), sharedPreferences)

        Assert.assertNotNull("Returned null settings", settings)
        Assert.assertEquals("Wrong device address.", deviceAddress, settings!!.deviceAddress)
        Assert.assertEquals("Wrong device name.", deviceName, settings.deviceName)
        Assert.assertEquals("Wrong playlist option.", true, settings.usePlaylist)
        Assert.assertEquals("Wrong playlist.", playlist, settings.playlist)
        Assert.assertEquals("Wrong delay.", defaultDelay.toLong(), settings.delayInMilliseconds)
        Assert.assertEquals("Wrong volume.", volume, settings.volume)
    }


    @Test
    fun fromSharedPreferences_Returns_Proper_Settings_For_Play_Configuration() {

        val sharedPreferences = Mockito.mock(SharedPreferences::class.java)

        Mockito.`when`(sharedPreferences.getString("pref_device", "")).thenReturn("$deviceAddress|$deviceName")
        Mockito.`when`(sharedPreferences.getBoolean("pref_use_playlist", false)).thenReturn(false)
        Mockito.`when`(sharedPreferences.getString("pref_playlist", "")).thenReturn(playlist)
        Mockito.`when`(sharedPreferences.getInt("pref_delay", defaultDelay)).thenReturn(defaultDelay)
        Mockito.`when`(sharedPreferences.getInt("pref_volume", defaultVolume)).thenReturn(volume)

        val settings = AppSettings.fromSharedPreferences(createMockContext(), sharedPreferences)

        Assert.assertNotNull("Returned null settings", settings)
        Assert.assertEquals("Wrong device address.", deviceAddress, settings!!.deviceAddress)
        Assert.assertEquals("Wrong device name.", deviceName, settings.deviceName)
        Assert.assertEquals("Wrong playlist option.", false, settings.usePlaylist)
        Assert.assertEquals("Wrong playlist.", playlist, settings.playlist)
        Assert.assertEquals("Wrong delay.", defaultDelay.toLong(), settings.delayInMilliseconds)
        Assert.assertEquals("Wrong volume.", volume, settings.volume)
    }


    @Test
    fun fromSharedPreferences_Returns_Null_When_Device_Is_Empty() {

        val sharedPreferences = Mockito.mock(SharedPreferences::class.java)

        Mockito.`when`(sharedPreferences.getString("pref_device", "")).thenReturn("")
        Mockito.`when`(sharedPreferences.getBoolean("pref_use_playlist", false)).thenReturn(true)
        Mockito.`when`(sharedPreferences.getString("pref_playlist", "")).thenReturn(playlist)
        Mockito.`when`(sharedPreferences.getInt("pref_delay", defaultDelay)).thenReturn(defaultDelay)
        Mockito.`when`(sharedPreferences.getInt("pref_volume", defaultVolume)).thenReturn(volume)

        val settings = AppSettings.fromSharedPreferences(createMockContext(), sharedPreferences)

        Assert.assertNull("Returned non-null settings", settings)
    }


    @Test
    fun fromSharedPreferences_Returns_Null_When_Using_Playlist_And_Playlist_Is_Empty() {

        val sharedPreferences = Mockito.mock(SharedPreferences::class.java)

        Mockito.`when`(sharedPreferences.getString("pref_device", "")).thenReturn("$deviceAddress|$deviceName")
        Mockito.`when`(sharedPreferences.getBoolean("pref_use_playlist", false)).thenReturn(true)
        Mockito.`when`(sharedPreferences.getString("pref_playlist", "")).thenReturn("")
        Mockito.`when`(sharedPreferences.getInt("pref_delay", defaultDelay)).thenReturn(defaultDelay)
        Mockito.`when`(sharedPreferences.getInt("pref_volume", defaultVolume)).thenReturn(volume)

        val settings = AppSettings.fromSharedPreferences(createMockContext(), sharedPreferences)

        Assert.assertNull("Returned non-null settings", settings)
    }


    @Test
    fun fromSharedPreferences_Returns_Null_Playlist_When_Empty_For_Play_Configuration() {

        val sharedPreferences = Mockito.mock(SharedPreferences::class.java)

        Mockito.`when`(sharedPreferences.getString("pref_device", "")).thenReturn("$deviceAddress|$deviceName")
        Mockito.`when`(sharedPreferences.getBoolean("pref_use_playlist", false)).thenReturn(false)
        Mockito.`when`(sharedPreferences.getString("pref_playlist", "")).thenReturn("")
        Mockito.`when`(sharedPreferences.getInt("pref_delay", defaultDelay)).thenReturn(defaultDelay)
        Mockito.`when`(sharedPreferences.getInt("pref_volume", defaultVolume)).thenReturn(volume)

        val settings = AppSettings.fromSharedPreferences(createMockContext(), sharedPreferences)

        Assert.assertNotNull("Returned null settings", settings)
        Assert.assertEquals("Wrong device address.", deviceAddress, settings!!.deviceAddress)
        Assert.assertEquals("Wrong device name.", deviceName, settings.deviceName)
        Assert.assertEquals("Wrong playlist option.", false, settings.usePlaylist)
        Assert.assertEquals("Wrong playlist.", null, settings.playlist)
        Assert.assertEquals("Wrong delay.", defaultDelay.toLong(), settings.delayInMilliseconds)
        Assert.assertEquals("Wrong volume.", volume, settings.volume)
    }


    @Test
    fun fromSharedPreferences_Returns_Null_Volume_When_Set_To_Minus_One() {

        val sharedPreferences = Mockito.mock(SharedPreferences::class.java)

        Mockito.`when`(sharedPreferences.getString("pref_device", "")).thenReturn("$deviceAddress|$deviceName")
        Mockito.`when`(sharedPreferences.getBoolean("pref_use_playlist", false)).thenReturn(true)
        Mockito.`when`(sharedPreferences.getString("pref_playlist", "")).thenReturn(playlist)
        Mockito.`when`(sharedPreferences.getInt("pref_delay", defaultDelay)).thenReturn(defaultDelay)
        Mockito.`when`(sharedPreferences.getInt("pref_volume", defaultVolume)).thenReturn(-1)

        val settings = AppSettings.fromSharedPreferences(createMockContext(), sharedPreferences)

        Assert.assertNotNull("Returned null settings", settings)
        Assert.assertEquals("Wrong device address.", deviceAddress, settings!!.deviceAddress)
        Assert.assertEquals("Wrong device name.", deviceName, settings.deviceName)
        Assert.assertEquals("Wrong playlist option.", true, settings.usePlaylist)
        Assert.assertEquals("Wrong playlist.", playlist, settings.playlist)
        Assert.assertEquals("Wrong delay.", defaultDelay.toLong(), settings.delayInMilliseconds)
        Assert.assertEquals("Wrong volume.", null, settings.volume)
    }


    private fun createMockContext() : Context {
        val mock = Mockito.mock(Context::class.java)

        Mockito.`when`(mock.getString(R.string.pref_device)).thenReturn("pref_device")
        Mockito.`when`(mock.getString(R.string.pref_use_playlist)).thenReturn("pref_use_playlist")
        Mockito.`when`(mock.getString(R.string.pref_playlist)).thenReturn("pref_playlist")
        Mockito.`when`(mock.getString(R.string.pref_delay)).thenReturn("pref_delay")
        Mockito.`when`(mock.getString(R.string.pref_volume)).thenReturn("pref_volume")

        return mock
    }

}