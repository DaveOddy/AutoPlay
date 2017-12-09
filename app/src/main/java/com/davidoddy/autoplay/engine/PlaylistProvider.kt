package com.davidoddy.autoplay.engine

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

/**
 * Created by doddy on 12/6/17.
 */
class PlaylistProvider(val context: Context) : IPlaylistProvider {

    companion object {
        val TAG = PlaylistProvider::class.simpleName
        const val URI_PLAYLIST = "content://com.google.android.music.MusicContent/playlists"
        const val COL_NAME = "playlist_name"
    }


    override fun getPlaylists(): List<String> {

        val cursor = queryPlaylists()
        try {
            val list = ArrayList<String>(cursor.count)
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex(COL_NAME)))
            }
            return list
        }
        finally {
            cursor.close()
        }
    }


    private fun queryPlaylists() =
        this.context.contentResolver.query(Uri.parse(URI_PLAYLIST),
                arrayOf(COL_NAME),
                null,
                null,
                null)
}