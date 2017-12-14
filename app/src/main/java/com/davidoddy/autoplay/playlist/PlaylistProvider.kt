package com.davidoddy.autoplay.playlist

import android.content.ContentResolver
import android.net.Uri

/**
 * Created by doddy on 12/6/17.
 */
open class PlaylistProvider(private val contentResolver: ContentResolver) : IPlaylistProvider {

    companion object {
        const val URI_PLAYLIST = "content://com.google.android.music.MusicContent/playlists"
        const val COL_NAME = "playlist_name"
    }


    override fun getPlaylists(): List<CharSequence> {

        val cursor = queryPlaylists()
        return cursor.use { cursor ->
            val colIndex = cursor.getColumnIndex(COL_NAME)
            generateSequence { if (cursor.moveToNext()) cursor else null }
                    .map { c -> c.getString(colIndex) }
                    .distinctBy { it }
                    .sortedBy { it }
                    .toList()
        }
    }


    private fun queryPlaylists() =
        this.contentResolver.query(Uri.parse(URI_PLAYLIST),
                arrayOf(COL_NAME),
                null,
                null,
                null)
}