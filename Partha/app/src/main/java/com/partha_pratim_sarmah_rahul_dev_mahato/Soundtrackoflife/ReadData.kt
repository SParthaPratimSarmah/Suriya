package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.MainActivity.Companion.auth
import com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife.databinding.ActivityReadDataBinding

class ReadData : AppCompatActivity() {

    private lateinit var binding : ActivityReadDataBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadDataBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    private fun readData(userName: String) {

        database = FirebaseDatabase.getInstance().getReference(auth.uid.toString())
        database.child(userName).get().addOnSuccessListener {

            if (it.exists()){

                val firstname = it.child("Email").value

                Toast.makeText(this,"Successfuly Read",Toast.LENGTH_SHORT).show()


            }else{

                Toast.makeText(this,"User Doesn't Exist",Toast.LENGTH_SHORT).show()


            }

        }.addOnFailureListener{

            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()


        }



    }
}