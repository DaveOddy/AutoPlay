package com.davidoddy.autoplay.engine

import android.content.Context

/**
 * Created by doddy on 12/6/17.
 */
interface IPlaylistProvider {
    fun getPlaylists(): List<String>
}
