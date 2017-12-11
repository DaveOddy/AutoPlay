package com.davidoddy.autoplay.audio

import android.content.Context
import android.media.AudioManager
import android.view.KeyEvent
import timber.log.Timber

/**
 * Created by doddy on 12/8/17.
 */
class PlayMediaLauncher(val context: Context, val audioManager: AudioManager) : IMediaLauncher {

    override fun playMusic() {
        if (this.audioManager.isMusicActive) {
            Timber.v("Music already playing")
        }
        else {
            Timber.v("Playing audio")
            val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
            this.audioManager.dispatchMediaKeyEvent(keyEvent)
        }
    }
}