package com.davidoddy.autoplay.audio

import android.content.Context
import android.media.AudioManager
import com.davidoddy.autoplay.model.AppSettings

/**
 * Created by doddy on 12/8/17.
 */
interface IMediaLauncher {
    companion object Factory {
        fun createForSettings(context: Context, audioManager: AudioManager, settings: AppSettings) : IMediaLauncher {
            return if (settings.usePlaylist) {
                PlaylistMediaLauncher(context, settings.playlist)
            } else {
                PlayMediaLauncher(context, audioManager)
            }

        }
    }

    fun playMusic()
}
