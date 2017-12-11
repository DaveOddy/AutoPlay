package com.davidoddy.autoplay.playlist

/**
 * Created by doddy on 12/6/17.
 */
interface IPlaylistProvider {
    fun getPlaylists(): List<CharSequence>
}
