package com.davidoddy.autoplay.audio

import android.content.Context
import android.media.AudioManager
import android.view.KeyEvent
import timber.log.Timber

/**
 * Created by doddy on 12/8/17.
 */
class PlayMediaLauncher(val context: Context, private val audioManager: AudioManager, private val skipAhead: Boolean) : IMediaLauncher {

    override fun playMusic() {
        if (this.audioManager.isMusicActive) {
            Timber.v("Music already playing")
        }
        else {
            Timber.v("Playing audio")
            sendKey(KeyEvent.KEYCODE_MEDIA_PLAY)

            if (this.skipAhead) {
                Timber.v("Playing audio")
                sendKey(KeyEvent.KEYCODE_MEDIA_NEXT)
            }
        }
    }

    private fun sendKey(key: Int) {
        this.audioManager.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, key))
    }
}