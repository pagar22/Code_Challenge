package com.example.code_challenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private int splash_time = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh);
        //Splash Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
                boolean isRegistered = sharedPreferences.getBoolean("isRegistered", false);
                //if registered, goto Main else goto registration page
                Intent splashIntent = (isRegistered)? new Intent(getApplicationContext(), MainActivity.class)
                        : new Intent(getApplicationContext(), Registration.class);
                startActivity(splashIntent);
                finish();
            }
        },splash_time);
    }


}
