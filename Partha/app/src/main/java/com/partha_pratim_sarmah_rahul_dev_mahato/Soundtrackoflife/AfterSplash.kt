package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.databinding.ActivityAfterSplashBinding

class AfterSplash : AppCompatActivity() {

    private lateinit var binding: ActivityAfterSplashBinding

    companion object{
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_splash)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser==null){
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        binding = ActivityAfterSplashBinding.inflate(layoutInflater)
        startActivity(Intent(this, MainActivity::class.java))
        setContentView(binding.root)
        finish()

        binding.signIn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

      binding.signOut.setOnClickListener {
            auth.signOut()
            binding.userDetails.text=updateData()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.userDetails.text=updateData()
    }

    private fun updateData(): String{
        return "ইমেইল : ${auth.currentUser?.email}"
    }

}