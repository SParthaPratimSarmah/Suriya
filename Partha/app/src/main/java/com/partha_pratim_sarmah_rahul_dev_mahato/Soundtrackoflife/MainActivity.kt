package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.client.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.databinding.ActivityMainBinding
import java.io.File
import java.util.*
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.collections.HashMap





private val DatabaseReference.once: Unit
    get() {}

class MainActivity : AppCompatActivity() {
    private var Ref: Firebase? = null
    private val db = FirebaseDatabase.getInstance()
    private val okay = db.reference.child("User Activity")
//unnecessarily created


    //
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapter: MusicAdapter
    //private lateinit var calendar: Calendar
    private var shuffleMode = false

    companion object{
        //


        var handler: Handler= Handler()
        lateinit var auth: FirebaseAuth
        //
        lateinit var MusicListMA : ArrayList<Music>
        lateinit var musicListSearch : ArrayList<Music>
        var search: Boolean = false
        var themeIndex: Int = 0
        val currentTheme = arrayOf(R.style.coolPink, R.style.coolBlue, R.style.coolPurple, R.style.coolGreen, R.style.coolBlack)
        val currentThemeNav = arrayOf(R.style.coolPinkNav, R.style.coolBlueNav, R.style.coolPurpleNav, R.style.coolGreenNav,
            R.style.coolBlackNav)
        val currentGradient = arrayOf(R.drawable.gradient_pink, R.drawable.gradient_blue, R.drawable.gradient_purple, R.drawable.gradient_green,
        R.drawable.gradient_black)
        var sortOrder: Int = 0

        val sortingList = arrayOf(MediaStore.Audio.Media.DATE_ADDED + " DESC", MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.SIZE + " DESC")

    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //


        //
        Firebase.setAndroidContext(this)
        Ref = Firebase("https://fir-authentication-180102007-default-rtdb.firebaseio.com")
        //
        loadLocale()
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser==null){
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
        //
        val themeEditor = getSharedPreferences("THEMES", MODE_PRIVATE)
        themeIndex = themeEditor.getInt("themeIndex", 0)
        setTheme(currentThemeNav[themeIndex])
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //




        //for nav drawer
        toggle = ActionBarDrawerToggle(this, binding.root,R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(requestRuntimePermission()){
            initializeLayout()
            //for retrieving favourites data using shared preferences
            FavouriteActivity.favouriteSongs = ArrayList()
            val editor = getSharedPreferences("FAVOURITES", MODE_PRIVATE)
            val jsonString = editor.getString("FavouriteSongs", null)
            val typeToken = object : TypeToken<ArrayList<Music>>(){}.type
            if(jsonString != null){
                val data: ArrayList<Music> = GsonBuilder().create().fromJson(jsonString, typeToken)
                FavouriteActivity.favouriteSongs.addAll(data)
            }
            PlaylistActivity.musicPlaylist = MusicPlaylist()
            val jsonStringPlaylist = editor.getString("MusicPlaylist", null)
            if(jsonStringPlaylist != null){
                val dataPlaylist: MusicPlaylist = GsonBuilder().create().fromJson(jsonStringPlaylist, MusicPlaylist::class.java)
                PlaylistActivity.musicPlaylist = dataPlaylist
            }
        }

        binding.shuffleBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
            intent.putExtra("index", 0)
            intent.putExtra("class", "MainActivity")
            startActivity(intent)
        }
        binding.changeMyLanguage.setOnClickListener {
            showChangeLanguageDialog()
        }

        binding.favouriteBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavouriteActivity::class.java))
        }
       ////binding.AlbumsBtn.setOnClickListener {
         //   startActivity(Intent(this@MainActivity, AlbumActivity::class.java))
        //}

        binding.playlistBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlaylistActivity::class.java))
        }
        binding.navView.setNavigationItemSelectedListener{
            when(it.itemId)
            {
                //R.id.navLogin ->startActivity(Intent(this@MainActivity, LoginActivity::class.java))

                R.id.navFeedback -> startActivity(Intent(this@MainActivity, Feed::class.java))
                R.id.navSettings -> startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                R.id.navAbout -> startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                R.id.navExit -> {
                    val builder = MaterialAlertDialogBuilder(this)
                    builder.setTitle("প্ৰস্থান")
                        .setMessage("আপুনি এপ্পটো বন্ধ কৰিব বিচাৰে নেকি?")
                        .setPositiveButton("হয়"){ _, _ ->
                            exitApplication()
                            finish()
                        }
                        .setNegativeButton("নহয়"){dialog, _ ->
                            dialog.dismiss()
                        }
                    val customDialog = builder.create()
                    customDialog.show()
                    customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                    customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE)
                }
                R.id.Logout ->{
                    val builder = MaterialAlertDialogBuilder(this)
                    builder.setTitle("লগআউট")
                        .setMessage("আপুনি প্ৰকৃততে লগআউট কৰিব বিচাৰে নেকি ?")
                        .setPositiveButton("হয়"){ _, _ ->
                            //FirebaseAuth.getInstance().signOut()//logout of the user
                            auth.signOut()
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            finish()
                        }
                        .setNegativeButton("নহয়"){dialog, _ ->
                            dialog.dismiss()
                        }
                    val customDialog = builder.create()
                    customDialog.show()
                    customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                    customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE)
                }

            }
            true
        }
    }



    //
    private fun showChangeLanguageDialog() {
    val listname = arrayOf("অসমীয়া","English","Français","தமிழ்","हिंदी")
    val mBuilder = AlertDialog.Builder(this@MainActivity)
    mBuilder.setTitle("Choose Language...")
    mBuilder.setSingleChoiceItems(listname, -1
    ) { dialogInterface, i ->

        if (i == 0) {
            //Assamese
            setLocale("as")
            recreate()
        } else if (i == 1) {
            //English
            setLocale("en")
            recreate()
        } else if (i == 2) {
            //French
            setLocale("fr")
            recreate()
        } else if (i == 3) {
            //Tamil
            setLocale("ta")
            recreate()
        } else if (i == 4) {
            //Hindi
            setLocale("hi")
            recreate()
        }

        //dismiss alerdialog
        dialogInterface.dismiss()
    }
        val mDialog = mBuilder.create()
    mDialog.show()

}


//

    private fun setLocale(lang: String)  {
        val locale: Locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.getResources()
            .updateConfiguration(config, baseContext.getResources().getDisplayMetrics())



//save data to shared preferences

        val editor: SharedPreferences.Editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()
    }
    //
    private fun loadLocale() {
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val language = prefs.getString("My_Lang", "")
        setLocale(language!!)
    }
   // }


    //For requesting permission
    private fun requestRuntimePermission() :Boolean{
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 13){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "অনুমতি প্ৰদান কৰা হৈছে",Toast.LENGTH_SHORT).show()
                initializeLayout()
            }
            else
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("SetTextI18n")
    private fun initializeLayout(){
        search = false
        val sortEditor = getSharedPreferences("SORTING", MODE_PRIVATE)
        sortOrder = sortEditor.getInt("sortOrder", 0)
        MusicListMA = getAllAudio()
        binding.musicRV.setHasFixedSize(true)
        binding.musicRV.setItemViewCacheSize(13)
        binding.musicRV.layoutManager = LinearLayoutManager(this@MainActivity)
        musicAdapter = MusicAdapter(this@MainActivity, MusicListMA)
        binding.musicRV.adapter = musicAdapter
        //val ent=binding.totalSongs.text

        binding.totalSongs.text  = binding.totalSongs.text.toString()+ musicAdapter.itemCount.toString()
        binding.emailthis.text = auth.currentUser?.email

        //
        val emaildata=auth.currentUser?.email.toString()
        val totalSongNumber=musicAdapter.itemCount.toString()
        val AllMUsicList= MusicListMA.toString()


        //val userMap=HashMap<String,String>()
        //userMap["Email"]=emaildata
        //userMap["Total Song of this user: "]=totalSongNumber
        //userMap["All Songs Details"]=AllMUsicList






        val calender= Calendar.getInstance()
        val currentDay: Int = calender.get(Calendar.DAY_OF_MONTH)
       //
        val day=calender.get(Calendar.DAY_OF_MONTH)
        val userMap=HashMap<String,String>()
        userMap["Email"]=emaildata
        userMap["Total Song of this user: "]=totalSongNumber
        userMap["All Songs Details"]=AllMUsicList
        userMap["Date"]=day.toString()
        //
        val settings = getSharedPreferences("PREFS", 0)
        val lastDay = settings.getInt("day", 0)
        if(lastDay !=currentDay){
            val editor = settings.edit()
            editor.putInt("day",currentDay)
            editor.commit()

            //
            okay.push().setValue(userMap).addOnCanceledListener { }
            //
        }




    }
    //

    //


    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.R)
    private fun getAllAudio(): ArrayList<Music>{
        val tempList = ArrayList<Music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC +  " != 0"
        val projection = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID)
        val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,selection,null,
        sortingList[sortOrder], null)
        if(cursor != null){
            if(cursor.moveToFirst())
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri, albumIdC).toString()
                    val music = Music(id = idC, title = titleC, album = albumC, artist = artistC, path = pathC, duration = durationC,
                    artUri = artUriC)
                    val file = File(music.path)
                    if(file.exists())
                        tempList.add(music)
                }while (cursor.moveToNext())
                cursor.close()
        }
        return tempList
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!PlayerActivity.isPlaying && PlayerActivity.musicService != null){
           exitApplication()

        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        //

        //
        //for storing favourites data using shared preferences
        val editor = getSharedPreferences("FAVOURITES", MODE_PRIVATE).edit()
        val jsonString = GsonBuilder().create().toJson(FavouriteActivity.favouriteSongs)
        editor.putString("FavouriteSongs", jsonString)
        val jsonStringPlaylist = GsonBuilder().create().toJson(PlaylistActivity.musicPlaylist)
        editor.putString("MusicPlaylist", jsonStringPlaylist)
        editor.apply()
        //for sorting
        val sortEditor = getSharedPreferences("SORTING", MODE_PRIVATE)
        val sortValue = sortEditor.getInt("sortOrder", 0)
        if(sortOrder != sortValue){
            sortOrder = sortValue
            MusicListMA = getAllAudio()
            musicAdapter.updateMusicList(MusicListMA)
        }
    }
    //method for set Text


    //



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view_menu, menu)
        //for setting gradient
        findViewById<LinearLayout>(R.id.linearLayoutNav)?.setBackgroundResource(currentGradient[themeIndex])
        val searchView = menu?.findItem(R.id.searchView)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                musicListSearch = ArrayList()
                if(newText != null){
                    val userInput = newText.lowercase()
                    for (song in MusicListMA)
                        if(song.title.lowercase().contains(userInput))
                            musicListSearch.add(song)
                    search = true
                    musicAdapter.updateMusicList(searchList = musicListSearch)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


}




