package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.databinding.ActivityPlaylistBinding
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.databinding.AddPlaylistDialogBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var adapter: PlaylistViewAdapter

    companion object{
        var musicPlaylist: MusicPlaylist = MusicPlaylist()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentTheme[MainActivity.themeIndex])
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playlistRV.setHasFixedSize(true)
        binding.playlistRV.setItemViewCacheSize(13)
        binding.playlistRV.layoutManager = GridLayoutManager(this@PlaylistActivity, 2)
        adapter = PlaylistViewAdapter(this, playlistList = musicPlaylist.ref)
        binding.playlistRV.adapter = adapter
        binding.backBtnPLA.setOnClickListener { finish() }
        binding.addPlaylistBtn.setOnClickListener { customAlertDialog() }
    }
    @SuppressLint("ResourceAsColor")
    private fun customAlertDialog(){
        val customDialog = LayoutInflater.from(this@PlaylistActivity).inflate(R.layout.add_playlist_dialog, binding.root, false)
        val binder = AddPlaylistDialogBinding.bind(customDialog)
        val builder = MaterialAlertDialogBuilder(this)
        builder.setView(customDialog)
            .setTitle("প্লেলিস্টৰ বিৱৰণ")
            .setPositiveButton("যোগ কৰক"){ dialog, _ ->

                val playlistName = binder.playlistName.text
                val createdBy = binder.yourName.text

                if(playlistName!!.isEmpty() && createdBy!!.isEmpty() ){
                    val info = "আপুনি দুটা বাকচৰ এটাও পূৰণ কৰা নাই "

                    val toast = Toast.makeText(this,Html.fromHtml("<font color='#ff0000' ><b>$info</b></font>") , Toast.LENGTH_SHORT)


                    toast.show()
                    customAlertDialog()
                }
                else if(playlistName.isEmpty() && createdBy!!.isNotEmpty() ){
                    val info = "আপুনি প্লেলিষ্টৰ নাম টাইপ কৰা নাই "

                    val toast = Toast.makeText(this,Html.fromHtml("<font color='#ff0000' ><b>$info</b></font>") , Toast.LENGTH_SHORT)

                    toast.show()

                    customAlertDialog()
                }
                else if(playlistName.isNotEmpty() && createdBy!!.isEmpty() ){
                    val info = "আপুনি প্লেলিষ্টৰ সৃষ্টিকৰ্তাৰ নাম টাইপ কৰা নাই "

                    val toast = Toast.makeText(this,Html.fromHtml("<font color='#ff0000' ><b>$info</b></font>") , Toast.LENGTH_SHORT)

                    toast.show()
                    customAlertDialog()
                }
                else if(playlistName != null && createdBy != null)
                    if(playlistName.isNotEmpty() && createdBy.isNotEmpty())
                    {
                        addPlaylist(playlistName.toString(), createdBy.toString())

                    }

                dialog.dismiss()
            }.show()

    }



    private fun addPlaylist(name: String, createdBy: String){
        var playlistExists = false
        for(i in musicPlaylist.ref) {
            if (name == i.name){
                playlistExists = true
                break
            }
        }
        if(playlistExists) Toast.makeText(this, "প্লেলিষ্ট বিদ্যমান!!", Toast.LENGTH_SHORT).show()
        else {
            val tempPlaylist = Playlist()
            tempPlaylist.name = name
            tempPlaylist.playlist = ArrayList()
            tempPlaylist.createdBy = createdBy
            val calendar = Calendar.getInstance().time
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            tempPlaylist.createdOn = sdf.format(calendar)
            musicPlaylist.ref.add(tempPlaylist)
            adapter.refreshPlaylist()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}