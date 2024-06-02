package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AlbumDetails : AppCompatActivity() {
    private var musicFiles = ArrayList<Music>()

    var recyclerView: RecyclerView? = null
    var albumPhoto: ImageView? = null
    var albumName: String? = null
    var albumSongs: ArrayList<Music> = ArrayList()
    var albumDetailsAdapter: AlbumDetailsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_details)
        recyclerView = findViewById(R.id.recyclerView)
        albumPhoto = findViewById(R.id.albumPhoto)
        albumName = intent.getStringExtra("albumName")
        var j = 0
        println(musicFiles)
        for (i in 0 until musicFiles.size) {
            if (albumName == musicFiles.get(i).album) {
                albumSongs.add(j, musicFiles.get(i))
                println(musicFiles)
                j++
            }
        }
//        val image = getAlbumArt(albumSongs[0].path)
//        val bitmap = BitmapFactory.decodeByteArray(image, 0, image!!.size)
//        if (image != null) {
//            Glide.with(this)
//                .load(bitmap)
//                .into(albumPhoto)
//        } else {
//            Glide.with(this)
//                .load(R.drawable.music_icon)
//                .into(albumPhoto)
//        }
    }

    override fun onResume() {
        super.onResume()
            albumDetailsAdapter = AlbumDetailsAdapter(this, albumSongs)
            recyclerView!!.adapter = albumDetailsAdapter
            recyclerView!!.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.VERTICAL, false
            )

    }

    private fun getAlbumArt(uri: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art = retriever.embeddedPicture
        retriever.release()
        return art
    }
}