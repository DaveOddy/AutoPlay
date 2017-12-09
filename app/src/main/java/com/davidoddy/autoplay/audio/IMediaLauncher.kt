package com.davidoddy.autoplay.audio

import android.content.Context
import android.media.AudioManager
import com.davidoddy.autoplay.model.Settings

/**
 * Created by doddy on 12/8/17.
 */
interface IMediaLauncher {
    companion object Factory {
        fun createForSettings(context: Context, audioManager: AudioManager, settings: Settings) : IMediaLauncher {
            if (settings.usePlaylist) {
                return PlaylistMediaLauncher(context, settings.playlist)
            }
            else {
                return PlayMediaLauncher(context, audioManager)
            }

        }
    }

    fun playMusic()
}
