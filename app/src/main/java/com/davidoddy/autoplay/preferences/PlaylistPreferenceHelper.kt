package com.davidoddy.autoplay.preferences

import android.preference.ListPreference
import com.davidoddy.autoplay.playlist.PlaylistProvider

/**
 * Created by doddy on 12/11/17.
 */
class PlaylistPreferenceHelper(val playlistProvider: PlaylistProvider) : ListPreferenceHelper() {

    override fun loadPreferenceList(preference: ListPreference) {

        val valuesArray: Array<CharSequence> = playlistProvider.getPlaylists()
                .distinctBy { it }
                .toTypedArray()

        setArrays(preference, valuesArray, valuesArray)
    }
}