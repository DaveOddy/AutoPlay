package com.davidoddy.autoplay.preferences

import android.preference.ListPreference
import com.davidoddy.autoplay.playlist.PlaylistProvider
import junit.framework.Assert
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * Created by doddy on 12/12/17.
 */
class PlaylistPreferenceHelperTest {

    @Test
    fun loadPreferenceList_Loads_List_Correctly_And_Sets_Default_And_Selects_First_Item_When_List_Is_Not_Empty() {

        val playlist1 = "Playlist1"
        val playlist2 = "Playlist2"
        val playlist3 = "Playlist3"

        val preference = Mockito.mock(ListPreference::class.java)
        Mockito.`when`(preference.entry).thenReturn(playlist1)

        val playlistProvider = Mockito.mock(PlaylistProvider::class.java)

        val dummyPlaylists = listOf<String>(playlist1, playlist2, playlist3)
        Mockito.`when`(playlistProvider.getPlaylists()).thenReturn(dummyPlaylists)

        PlaylistPreferenceHelper(playlistProvider).loadPreferenceList(preference)

        val entriesCaptor = ArgumentCaptor.forClass(Array<CharSequence>::class.java)
        val entryValuesCaptor = ArgumentCaptor.forClass(Array<CharSequence>::class.java)
        val defaultValueCaptor = ArgumentCaptor.forClass(String::class.java)
        val summaryCaptor = ArgumentCaptor.forClass(String::class.java)

        Mockito.verify(preference).setEntries(entriesCaptor.capture())
        Mockito.verify(preference).setEntryValues(entryValuesCaptor.capture())
        Mockito.verify(preference).setDefaultValue(defaultValueCaptor.capture())
        Mockito.verify(preference).setSummary(summaryCaptor.capture())

        Assert.assertEquals("Wrong array length for entries.", 3, entriesCaptor.value.size)
        Assert.assertEquals("Wrong entry #1.", playlist1, entriesCaptor.value[0])
        Assert.assertEquals("Wrong entry #2.", playlist2, entriesCaptor.value[1])
        Assert.assertEquals("Wrong entry #3.", playlist3, entriesCaptor.value[2])


        Assert.assertEquals("Wrong array length for values.", 3, entryValuesCaptor.value.size)
        Assert.assertEquals("Wrong value #1.", playlist1, entryValuesCaptor.value[0])
        Assert.assertEquals("Wrong value #2.", playlist2, entryValuesCaptor.value[1])
        Assert.assertEquals("Wrong value #3.", playlist3, entryValuesCaptor.value[2])

        Assert.assertEquals("Wrong default value.", playlist1, defaultValueCaptor.value)

        Assert.assertEquals("Wrong summary.", playlist1, summaryCaptor.value)
    }


    @Test
    fun loadPreferenceList_Loads_Empty_List_And_Does_Not_Select_Item_When_List_Is_Empty() {

        val preference = Mockito.mock(ListPreference::class.java)
        Mockito.`when`(preference.entry).thenReturn("")

        val playlistProvider = Mockito.mock(PlaylistProvider::class.java)

        val dummyPlaylists = listOf<String>()
        Mockito.`when`(playlistProvider.getPlaylists()).thenReturn(dummyPlaylists)

        PlaylistPreferenceHelper(playlistProvider).loadPreferenceList(preference)

        val entriesCaptor = ArgumentCaptor.forClass(Array<CharSequence>::class.java)
        val entryValuesCaptor = ArgumentCaptor.forClass(Array<CharSequence>::class.java)
        val summaryCaptor = ArgumentCaptor.forClass(String::class.java)

        Mockito.verify(preference).setEntries(entriesCaptor.capture())
        Mockito.verify(preference).setEntryValues(entryValuesCaptor.capture())
        Mockito.verify(preference, Mockito.never()).setDefaultValue(Mockito.any())
        Mockito.verify(preference).setSummary(summaryCaptor.capture())

        Assert.assertEquals("Wrong array length for entries.", 0, entriesCaptor.value.size)
        Assert.assertEquals("Wrong array length for values.", 0, entryValuesCaptor.value.size)
        Assert.assertEquals("Wrong summary.", "", summaryCaptor.value)
    }
}