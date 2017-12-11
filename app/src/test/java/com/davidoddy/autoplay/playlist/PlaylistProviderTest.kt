package com.davidoddy.autoplay.playlist

import android.content.ContentResolver
import android.database.CharArrayBuffer
import android.database.ContentObserver
import android.database.Cursor
import android.database.DataSetObserver
import android.net.Uri
import android.os.Bundle
import com.davidoddy.autoplay.BuildConfig
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by doddy on 12/11/17.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PlaylistProviderTest {

    val dummyPlaylists = arrayOf("Playlist3", "Playlist2", "Playlist1").toList()

    @Test
    fun getPlaylists_Returns_List_Of_Strings_Sorted_Properly_From_Cursor() {

        val cursor: Cursor = MockPlaylistCursor(this.dummyPlaylists)
        val contentResolver: ContentResolver = createMockContentResolver(cursor)

        val playlists = PlaylistProvider(contentResolver).getPlaylists()

        val uriCaptor: ArgumentCaptor<Uri> = ArgumentCaptor.forClass(Uri::class.java)
        val columnArrayCaptor: ArgumentCaptor<Array<String>> = ArgumentCaptor.forClass(Array<String>::class.java)
        val selectionCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        val selectionArgsArrayCaptor: ArgumentCaptor<Array<String>> = ArgumentCaptor.forClass(Array<String>::class.java)
        val orderByCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)

        Mockito.verify(contentResolver).query(uriCaptor.capture()
                , columnArrayCaptor.capture()
                , selectionCaptor.capture()
                , selectionArgsArrayCaptor.capture()
                , orderByCaptor.capture())

        Assert.assertEquals("Wrong intent", PlaylistProvider.URI_PLAYLIST, uriCaptor.value.toString())
        Assert.assertEquals("Wrong column count", 1, columnArrayCaptor.value.size)
        Assert.assertEquals("Wrong column", PlaylistProvider.COL_NAME, columnArrayCaptor.value[0])
        Assert.assertNull("Non-null Selection", selectionCaptor.value)
        Assert.assertNull("Non-null Selection Args", selectionArgsArrayCaptor.value)
        Assert.assertNull("Non-null sort order", orderByCaptor.value)

        for (i in 0..dummyPlaylists.size - 1) {
            Assert.assertEquals("Playlists not properly returned: ${playlists[i]}, ${dummyPlaylists[2 - i]}", dummyPlaylists[i], playlists[2 - i])
        }
    }


    private fun createMockContentResolver(cursor: Cursor) : ContentResolver {

        val contentResolver = Mockito.mock(ContentResolver::class.java)

        Mockito.`when`(contentResolver.query(
                Mockito.any(Uri::class.java),
                Mockito.any(Array<String>::class.java),
                Mockito.anyString(),
                Mockito.any(Array<String>::class.java),
                Mockito.anyString()
        )).thenReturn(cursor)

        return contentResolver
    }


    private class MockPlaylistCursor(val playlists: List<String>) : Cursor {

        private var itemIndex = -1

        override fun setNotificationUri(cr: ContentResolver?, uri: Uri?) {}

        override fun copyStringToBuffer(columnIndex: Int, buffer: CharArrayBuffer?) {}

        override fun getExtras(): Bundle = Bundle.EMPTY

        override fun setExtras(extras: Bundle?) {}

        override fun moveToPosition(position: Int): Boolean {
            if ((0..playlists.size - 1).contains( position)) {
                this.itemIndex = position
                return true
            }
            else {
                return false
            }
        }

        override fun getLong(columnIndex: Int): Long = 0

        override fun moveToFirst(): Boolean {
            if (this.playlists.size > 0) {
                this.itemIndex = 0
                return true
            } else {
                return false
            }
        }

        override fun getFloat(columnIndex: Int): Float = 0f

        override fun moveToPrevious(): Boolean {
            if (this.itemIndex > 0) {
                this.itemIndex--
                return true
            }
            else {
                return false
            }
        }

        override fun getDouble(columnIndex: Int): Double = 0.0

        override fun close() {}

        override fun isClosed(): Boolean = false

        override fun getCount(): Int = this.playlists.size

        override fun isFirst(): Boolean = this.itemIndex == 0

        override fun isNull(columnIndex: Int): Boolean = this.playlists[this.itemIndex] == null

        override fun unregisterContentObserver(observer: ContentObserver?) {}

        override fun getColumnIndexOrThrow(columnName: String?): Int =
            when (columnName) {
                PlaylistProvider.COL_NAME -> 0
                else -> throw IllegalArgumentException("Unknown column name: ${columnName}")
            }

        override fun requery(): Boolean {
            this.itemIndex = 0
            return true
        }

        override fun getWantsAllOnMoveCalls(): Boolean = false

        override fun getColumnNames(): Array<String> {
            return arrayOf(PlaylistProvider.COL_NAME)
        }

        override fun getInt(columnIndex: Int): Int = 0

        override fun isLast(): Boolean = this.itemIndex == (this.playlists.size - 1)

        override fun getType(columnIndex: Int): Int =
                when (columnIndex) {
                    0 -> Cursor.FIELD_TYPE_STRING
                    else -> Cursor.FIELD_TYPE_NULL
                }

        override fun registerDataSetObserver(observer: DataSetObserver?) {}

        override fun moveToNext(): Boolean {
            if (this.itemIndex < (this.playlists.size - 1)) {
                this.itemIndex++
                return true
            }
            else {
                return false
            }
        }

        override fun getPosition(): Int = this.itemIndex

        override fun isBeforeFirst(): Boolean = this.itemIndex < 0

        override fun registerContentObserver(observer: ContentObserver?) {}

        override fun moveToLast(): Boolean {
            if (this.playlists.size > 0) {
                this.itemIndex = this.playlists.size - 1
                return true
            }
            else {
                return false
            }
        }

        override fun deactivate() {}

        override fun getNotificationUri(): Uri = Uri.EMPTY

        override fun getColumnName(columnIndex: Int): String =
                when (columnIndex) {
                    0 -> PlaylistProvider.COL_NAME
                    else -> throw IllegalArgumentException("Unknown column index: ${columnIndex}")
                }


        override fun getColumnIndex(columnName: String?) =
                when (columnName) {
                    PlaylistProvider.COL_NAME -> 0
                    else -> -1
                }

        override fun getBlob(columnIndex: Int): ByteArray = ByteArray(0)

        override fun getShort(columnIndex: Int): Short = 0

        override fun getString(columnIndex: Int): String =
                when (columnIndex) {
                    0 -> this.playlists[this.itemIndex]
                    else -> throw IllegalArgumentException("Unknown column index: ${columnIndex}")
                }

        override fun move(offset: Int): Boolean {
            val targetPosition = this.itemIndex + offset
            if ((0..this.playlists.size - 1).contains(targetPosition)) {
                this.itemIndex = targetPosition
                return true
            }
            else {
                return false
            }
        }

        override fun getColumnCount(): Int = 1

        override fun respond(extras: Bundle?): Bundle = Bundle.EMPTY

        override fun unregisterDataSetObserver(observer: DataSetObserver?) {}

        override fun isAfterLast(): Boolean = this.itemIndex >= this.playlists.size
    }
}