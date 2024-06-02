package com.partha_pratim_sarmah_rahul_dev_mahato.Soundtrackoflife;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//import com.google.firebase.ktx.Firebase;


public class Feed extends AppCompatActivity {

    private Firebase Ref;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private EditText username,email,topic,feedback;
    private DatabaseReference root = db.getReference().child("Feedback Form");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        username =(EditText) findViewById(R.id.username);
        topic=(EditText)findViewById(R.id.topic);
        email=(EditText)findViewById(R.id.emailfeed);
        feedback=(EditText) findViewById(R.id.feedback);


        com.firebase.client.Firebase.setAndroidContext(this);
        Ref= new Firebase("https://fir-authentication-180102007-default-rtdb.firebaseio.com");

    }

    public void feedbacksent(View view) {
        //getSupportActionBar().getCustomView("");
        String usernameinput =username.getText().toString();
        String useremail=email.getText().toString();
        String topicnameinput=topic.getText().toString();
        String feddbackinput=feedback.getText().toString();
       // Firebase user=Ref.child("Username","Email","TopicName","FeedBack");
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("User Name",usernameinput);
        userMap.put("Email",useremail);
        userMap.put("Topic Name",topicnameinput);
        userMap.put("Feedback",feddbackinput);

        root.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Feed.this, "Data Saved", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this, "প্ৰতিক্ৰিয়াৰ বাবে ধন্যবাদ!!", Toast.LENGTH_SHORT).show();

        finish();


    }
}