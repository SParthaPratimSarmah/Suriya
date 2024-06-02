package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        val MY_SORT_PREF = "SortOrder"

        val albums = ArrayList<Music>()
        val preferences = getSharedPreferences(MY_SORT_PREF, MODE_PRIVATE)
        val sortOrder = preferences.getString("sorting", "sortByName")
        val duplicate = ArrayList<String>()
        albums.clear()
        var order: String? = null
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        when (sortOrder) {
            "SortByName" -> order = MediaStore.MediaColumns.DISPLAY_NAME + " ASC"
            "SortByDate" -> order = MediaStore.MediaColumns.DATE_ADDED + " ASC"
            "SortBySize" -> order = MediaStore.MediaColumns.SIZE + " DESC"
        }
        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,  //for path
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media._ID
        )
        val cursor: Cursor? = contentResolver.query(
            uri, projection,
            null, null, order
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val album = cursor.getString(0)
                val title = cursor.getString(1)
                val duration = cursor.getLong(2)
                val path = cursor.getString(3)
                val artist = cursor.getString(4)
                val id = cursor.getString(5)
                val uri = Uri.parse("content://media/external/audio/albumart")
                val artUriC = Uri.withAppendedPath(uri, album).toString()

                val musicFiles = Music(id, title, album, artist, duration, path,artUriC)
                //take log.e for check
                Log.e("Path: $path", "Album : $album")
                if (!duplicate.contains(album)) {
                    albums.add(musicFiles)
                    duplicate.add(album)
                }
            }
        }
        cursor?.close()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        if (albums.size >= 1) {
            val albumAdapter = AlbumAdapter(this, albums)
            recyclerView.adapter = albumAdapter
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        }
    }
}