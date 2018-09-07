package com.davidoddy.autoplay.audio

import android.content.Context
import android.media.AudioManager
import android.view.KeyEvent
import com.davidoddy.autoplay.BuildConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by doddy on 12/9/17.
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PlayMediaLauncherTest {

    @Test
    fun playMusic_Sends_Proper_KeyEvent_When_Not_Playing() {

        val context = Mockito.mock(Context::class.java)
        val audioManager = Mockito.mock(AudioManager::class.java)
        Mockito.`when`(audioManager.isMusicActive).thenReturn(false)

        PlayMediaLauncher(context, audioManager, false).playMusic()

        val keyEventCaptor = ArgumentCaptor.forClass(KeyEvent::class.java)
        Mockito.verify(audioManager).dispatchMediaKeyEvent(keyEventCaptor.capture())
        val keyEvent = keyEventCaptor.value

        Assert.assertEquals("Wrong key event action.", KeyEvent.ACTION_DOWN, keyEvent.action)
        Assert.assertEquals("Wrong key event code.", KeyEvent.KEYCODE_MEDIA_PLAY, keyEvent.keyCode)
    }


    @Test
    fun playMusic_Sends_Proper_KeyEvents_When_Not_Playing_With_Skip() {

        val context = Mockito.mock(Context::class.java)
        val audioManager = Mockito.mock(AudioManager::class.java)
        Mockito.`when`(audioManager.isMusicActive).thenReturn(false)

        PlayMediaLauncher(context, audioManager, true).playMusic()

        val keyEventCaptor = ArgumentCaptor.forClass(KeyEvent::class.java)
        Mockito.verify(audioManager, times(2)).dispatchMediaKeyEvent(keyEventCaptor.capture())
        val expectedPlayKey = keyEventCaptor.allValues[0]
        val expectedNextKey = keyEventCaptor.allValues[1]

        Assert.assertEquals("Wrong key event action.", KeyEvent.ACTION_DOWN, expectedPlayKey.action)
        Assert.assertEquals("Wrong key event code.", KeyEvent.KEYCODE_MEDIA_PLAY, expectedPlayKey.keyCode)

        Assert.assertEquals("Wrong key event action.", KeyEvent.ACTION_DOWN, expectedNextKey.action)
        Assert.assertEquals("Wrong key event code.", KeyEvent.KEYCODE_MEDIA_NEXT, expectedNextKey.keyCode)
    }


    @Test
    fun playMusic_Does_Nothing_When_Playing() {

        val context = Mockito.mock(Context::class.java)
        val audioManager = Mockito.mock(AudioManager::class.java)
        Mockito.`when`(audioManager.isMusicActive).thenReturn(true)

        PlayMediaLauncher(context, audioManager, false).playMusic()

        Mockito.verify(audioManager, Mockito.never()).dispatchMediaKeyEvent(Mockito.any(KeyEvent::class.java))
    }
}