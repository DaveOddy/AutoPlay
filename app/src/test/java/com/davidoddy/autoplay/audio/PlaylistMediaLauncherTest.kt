package com.davidoddy.autoplay.audio

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.provider.MediaStore.EXTRA_MEDIA_FOCUS
import android.provider.MediaStore.EXTRA_MEDIA_PLAYLIST
import com.davidoddy.autoplay.BuildConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by doddy on 12/9/17.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PlaylistMediaLauncherTest {

    @Test
    fun playMusic_Creates_Proper_Intent() {

        val dummyPlaylist = "DummyPlaylist"
        val context = Mockito.mock(Context::class.java)


        val testLauncher = PlaylistMediaLauncher(context, dummyPlaylist)
        testLauncher.playMusic()

        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        Mockito.verify(context).startActivity(intentCaptor.capture())

        Assert.assertEquals("Wrong intent action.", MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH, intentCaptor.value.action)
        Assert.assertEquals("Wrong focus extra.", MediaStore.Audio.Playlists.ENTRY_CONTENT_TYPE, intentCaptor.value.getStringExtra(EXTRA_MEDIA_FOCUS))
        Assert.assertEquals("Wrong playlist extra.", dummyPlaylist, intentCaptor.value.getStringExtra(EXTRA_MEDIA_PLAYLIST))
        Assert.assertEquals("Wrong playlist search extra.", dummyPlaylist, intentCaptor.value.getStringExtra(SearchManager.QUERY))
    }
}