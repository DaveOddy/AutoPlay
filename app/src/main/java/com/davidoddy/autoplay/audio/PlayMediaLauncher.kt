package com.davidoddy.autoplay.audio

import android.content.Context
import android.media.AudioManager
import android.util.Log
import android.view.KeyEvent

/**
 * Created by doddy on 12/8/17.
 */
class PlayMediaLauncher(val context: Context, val audioManager: AudioManager) : IMediaLauncher {

    companion object {
        val TAG = PlayMediaLauncher::class.java.simpleName
        const val MUSIC_INTENT = "com.android.music.musicservicecommand"
        const val COMMAND_EXTRA = "command"
        const val COMMAND_PLAY = "play"
    }


    override fun playMusic() {
        if (this.audioManager.isMusicActive) {
            Log.v(TAG, "Music already playing")
        }
        else {
            Log.v(TAG, "Playing audio")
            val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
            this.audioManager.dispatchMediaKeyEvent(keyEvent)
        }
    }
}