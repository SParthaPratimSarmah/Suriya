package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AlbumAdapter(private val mContext: Context, albumFiles: ArrayList<Music>) :
    RecyclerView.Adapter<AlbumAdapter.MyHolder>() {
    private val albumFiles: ArrayList<Music>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.album_name.setText(albumFiles[position].album)
        val image = getAlbumArt(albumFiles[position].path)
        if (image != null) {
            Glide.with(mContext).asBitmap().load(image).into(holder.album_image)
        } else {
            Glide.with(mContext).load(R.drawable.music_icon).into(holder.album_image)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, AlbumDetails::class.java)
            intent.putExtra("albumName", albumFiles[position].album)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return albumFiles.size
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var album_image: ImageView
        var album_name: TextView

        init {
            album_image = itemView.findViewById(R.id.album_image)
            album_name = itemView.findViewById(R.id.album_name)
        }
    }

    private fun getAlbumArt(uri: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art = retriever.embeddedPicture
        retriever.release()
        return art
    }

    init {
        this.albumFiles = albumFiles
    }
}