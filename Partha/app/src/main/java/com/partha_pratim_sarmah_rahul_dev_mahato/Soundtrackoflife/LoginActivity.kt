package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.databinding.ActivityLogin2Binding

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "লগইন"

        binding.registerTV.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        binding.forgetPassword.setOnClickListener {
            startActivity(Intent(this,ForgetPasswordActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailLogin.text.toString().trim()//
            val password = binding.passwordLogin.text.toString().trim()//

            if(email.isNotEmpty() && password.isNotEmpty())
                AfterSplash.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }
}