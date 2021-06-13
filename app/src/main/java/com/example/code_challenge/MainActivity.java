package com.example.code_challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout niche_tasks;
    RelativeLayout reminders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //code to reset shared preferences
        //getSharedPreferences("Code_Challenge", MODE_PRIVATE).edit().clear().commit();

        niche_tasks = findViewById(R.id.niche_tasks);
        reminders = findViewById(R.id.reminders);

        niche_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(NicheTasks.class);
            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(Reminders.class);
            }
        });
    }

    private void startIntent(Class pass){
        Intent intent = new Intent(getApplicationContext(), pass);
        startActivity(intent);
    }
}
