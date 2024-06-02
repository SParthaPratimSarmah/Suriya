package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.client.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.databinding.ActivityRegisterBinding
import java.security.MessageDigest
import java.util.*




class RegisterActivity : AppCompatActivity() {
    private var Ref: Firebase? = null
    private val db = FirebaseDatabase.getInstance()
    private val root = db.reference.child("Registered people")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "ৰেজিষ্টাৰ"
        Firebase.setAndroidContext(this)
        Ref = Firebase("https://fir-authentication-180102007-default-rtdb.firebaseio.com")

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        //


        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.loginTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.createAccountBtn.setOnClickListener {
            val email = binding.emailRegister.text.toString().trim()//
            val password = binding.passwordRegister.text.toString().trim()//
            // Firebase user=Ref.child("Username","Email","TopicName","FeedBack");
            val pass=sha256(password).toString()
            val userMap = HashMap<String, String>()
            userMap["Email"] = email
            userMap["Password"] =pass
            root.push().setValue(userMap).addOnCompleteListener {
                Toast.makeText(
                    this,
                    "You are registered Now you can log in",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if(email.isNotEmpty() && password.isNotEmpty())
                AfterSplash.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){


                        startActivity(Intent(this, LoginActivity::class.java))

                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }

        binding.googleBtn.setOnClickListener {
            googleSignInClient.signOut()
            startActivityForResult(googleSignInClient.signInIntent, 13)
        }
    }

    private fun sha256(base: String): String? {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(base.toByteArray(charset("UTF-8")))
            val hexString = StringBuffer()
            for (i in hash.indices) {
                val hex = Integer.toHexString(0xff and hash[i].toInt())
                if (hex.length == 1) hexString.append('0')
                hexString.append(hex)
            }
            hexString.toString()
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 13 && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        AfterSplash.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

}