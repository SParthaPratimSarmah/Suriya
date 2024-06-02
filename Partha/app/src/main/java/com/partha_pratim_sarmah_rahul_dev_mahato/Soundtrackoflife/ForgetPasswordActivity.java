package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private Button forgetBtn;
    private EditText txtEmail;
    private String email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        auth=FirebaseAuth.getInstance();
        txtEmail=findViewById(R.id.forgetEmail);
        forgetBtn = findViewById(R.id.ForgetPasswordBtn);

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

    }

    private void validateData() {
        email = txtEmail.getText().toString().trim();
        if(email.isEmpty()){
            txtEmail.setError("Email Required");
        }else{
            forgetPass();
        }
    }

    private void forgetPass() {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ForgetPasswordActivity.this,"Check your email",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                finish();
            }
            else{
                Toast.makeText(ForgetPasswordActivity.this,"Error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}