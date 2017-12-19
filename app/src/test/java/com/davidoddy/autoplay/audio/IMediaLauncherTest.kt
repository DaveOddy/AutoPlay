package com.davidoddy.autoplay.audio

import android.content.Context
import android.media.AudioManager
import com.davidoddy.autoplay.model.AppSettings
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

/**
 * Created by doddy on 12/9/17.
 */
class IMediaLauncherTest {

    @Test
    fun createForSettings_returns_PlaylistMediaLauncher_for_settings_using_playlist() {

        val context = Mockito.mock(Context::class.java)
        val audioManager = Mockito.mock(AudioManager::class.java)
        val dummyPlaylist = "DummyPlaylist"
        val settings = AppSettings("DummyDeviceAddress", "DummyDeviceName", true, dummyPlaylist, 0, 0)

        val testLauncher: IMediaLauncher = IMediaLauncher.createForSettings(context, audioManager, settings)

        assert(testLauncher is PlaylistMediaLauncher, {"Wrong class created."})
        Assert.assertEquals("Didn't create class with correct playlist", dummyPlaylist, (testLauncher as PlaylistMediaLauncher).playlist)
    }



    @Test
    fun createForSettings_returns_PlayMediaLauncher_for_settings_using_playlist() {

        val context = Mockito.mock(Context::class.java)
        val audioManager = Mockito.mock(AudioManager::class.java)
        val settings = AppSettings("DummyDeviceAddress", "DummyDeviceName", false, null, 0, 0)

        val testLauncher: IMediaLauncher = IMediaLauncher.createForSettings(context, audioManager, settings)

        assert(testLauncher is PlayMediaLauncher, {"Wrong class created."})
    }
}