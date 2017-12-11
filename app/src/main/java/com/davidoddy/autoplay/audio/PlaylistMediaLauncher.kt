package com.davidoddy.autoplay.audio

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import timber.log.Timber

/**
 * Created by doddy on 12/8/17.
 */
class PlaylistMediaLauncher(val context: Context, val playlist: String?) : IMediaLauncher {
    override fun playMusic() {
        Timber.v("Launching intent for playlist: ${playlist}")

        val intent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)
        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Playlists.ENTRY_CONTENT_TYPE)
        intent.putExtra(MediaStore.EXTRA_MEDIA_PLAYLIST, this.playlist)
        intent.putExtra(SearchManager.QUERY, this.playlist);

        this.context.startActivity(intent);
    }
}