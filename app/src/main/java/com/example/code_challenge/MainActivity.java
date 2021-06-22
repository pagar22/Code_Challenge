package com.example.code_challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    RelativeLayout niche_tasks;
    RelativeLayout reminders;
    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //code to reset shared preferences
        //getSharedPreferences("Code_Challenge", MODE_PRIVATE).edit().clear().commit();

        //simple if statement to display snackbar if user has just registered
        if(getIntent().getBooleanExtra("firstTimeLogin", false)){
            Snackbar.make(findViewById(R.id.mainActivityView),
                    "Welcome to MyMommy! Begin your journey by visiting the quickstart guide", Snackbar.LENGTH_LONG)
                    .setDuration(8000)
                    .setAction("GUIDE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), QuickStart.class);
                            startActivity(intent);
                        }
                    }).show();
        }

        niche_tasks = findViewById(R.id.niche_tasks);
        reminders = findViewById(R.id.reminders);
        welcomeText = findViewById(R.id.welcomeText);

        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
        String familyName = sharedPreferences.getString("FamilyName", "Family");
        String welcomeTime = "Good Morning, ";
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if(currentHour>=6 && currentHour<12) welcomeTime = "Good Morning, ";
        else if(currentHour>=12 && currentHour<17) welcomeTime = "Good Afternoon, ";
        else if(currentHour>=17 && currentHour<24) welcomeTime = "Good Evening, ";

        welcomeText.setText(welcomeTime+familyName+"s.");
        niche_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(NicheTasks.class);
            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(PointsTally.class);
            }
        });

        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });
    }

    private void startIntent(Class pass){
        Intent intent = new Intent(getApplicationContext(), pass);
        startActivity(intent);
    }
}
