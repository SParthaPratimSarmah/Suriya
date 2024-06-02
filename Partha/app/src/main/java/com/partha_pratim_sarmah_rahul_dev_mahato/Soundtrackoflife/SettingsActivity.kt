package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentThemeNav[MainActivity.themeIndex])
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "নিৰ্ধাৰিত"
        when(MainActivity.themeIndex){
            0 -> binding.coolPinkTheme.setBackgroundColor(Color.YELLOW)
            1 -> binding.coolBlueTheme.setBackgroundColor(Color.YELLOW)
            2 -> binding.coolPurpleTheme.setBackgroundColor(Color.YELLOW)
            3 -> binding.coolGreenTheme.setBackgroundColor(Color.YELLOW)
            4 -> binding.coolBlackTheme.setBackgroundColor(Color.YELLOW)
        }
        binding.coolPinkTheme.setOnClickListener { saveTheme(0) }
        binding.coolBlueTheme.setOnClickListener { saveTheme(1) }
        binding.coolPurpleTheme.setOnClickListener { saveTheme(2) }
        binding.coolGreenTheme.setOnClickListener { saveTheme(3) }
        binding.coolBlackTheme.setOnClickListener { saveTheme(4) }
        binding.versionName.text = setVersionDetails()
        binding.sortBtn.setOnClickListener {
            val menuList = arrayOf("শেহতীয়াকৈ যোগ কৰা হৈছে", " গানৰ শিৰোনামা অনুসৰি", "ফাইলৰ আকাৰ অনুসৰি")
            var currentSort = MainActivity.sortOrder
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("চৰ্টিং")
                .setPositiveButton("হ'ব"){ _, _ ->
                    val editor = getSharedPreferences("SORTING", MODE_PRIVATE).edit()
                    editor.putInt("sortOrder", currentSort)
                    editor.apply()
                }
                .setSingleChoiceItems(menuList, currentSort){ _,which->
                    currentSort = which
                }
            val customDialog = builder.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }

    }
//lets create separate string xml for each language


    private fun saveTheme(index: Int){
        if(MainActivity.themeIndex != index){
            val editor = getSharedPreferences("THEMES", MODE_PRIVATE).edit()
            editor.putInt("themeIndex", index)
            editor.apply()
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("থীম প্ৰয়োগ কৰক")
                .setMessage("আপুনি থীম প্ৰয়োগ কৰিব বিচাৰে নেকি?")
                .setPositiveButton("হয়"){ _, _ ->
                    exitApplication()
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
    private fun setVersionDetails():String{
        return "Version Name: ${BuildConfig.VERSION_NAME}"
    }
}