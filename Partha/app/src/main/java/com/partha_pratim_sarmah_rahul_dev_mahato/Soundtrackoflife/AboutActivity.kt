package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentThemeNav[MainActivity.themeIndex])
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "এপটোৰ বিষয়ে"
        binding.aboutText.text = aboutText()
    }
    private fun aboutText(): String{
        return "This app is made by Partha Pratim Sarmah and Rahul Dev Mahato as a final year project under Gauhati University,IT Department\n\n\n" +
                "Please share your experience in feedback ." +
                "\n\n\nএই এপটো পাৰ্থ প্ৰতিম শৰ্মা & ৰাহুল দেৱ মাহাটোৱে গুৱাহাটী বিশ্ববিদ্যালয়,তথ্য প্ৰযুক্তি বিভাগৰ অধীনত অন্তিম বৰ্ষৰ প্ৰকল্প হিচাপে নিৰ্মাণ কৰিছে\n" +
                "\nঅনুগ্ৰহ কৰি প্ৰতিক্ৰিয়াত আপোনাৰ অভিজ্ঞতা প্ৰকাশ কৰক।\n" +
                "\n \n" +
                "\n\n\n\n"
    }
}