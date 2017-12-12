package com.davidoddy.autoplay.playlist

import android.content.ContentResolver
import android.net.Uri

/**
 * Created by doddy on 12/6/17.
 */
open class PlaylistProvider(val contentResolver: ContentResolver) : IPlaylistProvider {

    companion object {
        const val URI_PLAYLIST = "content://com.google.android.music.MusicContent/playlists"
        const val COL_NAME = "playlist_name"
    }


    override fun getPlaylists(): List<CharSequence> {

        val cursor = queryPlaylists()
        try {
            val list = ArrayList<String>(cursor.count)
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex(COL_NAME)))
            }
            return list.sortedBy { it }
        }
        finally {
            cursor.close()
        }
    }


    private fun queryPlaylists() =
        this.contentResolver.query(Uri.parse(URI_PLAYLIST),
                arrayOf(COL_NAME),
                null,
                null,
                null)
}