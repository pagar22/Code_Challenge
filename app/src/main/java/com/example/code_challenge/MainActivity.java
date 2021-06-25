package com.example.code_challenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView welcomeText;
    TextView welcomeTasksText;
    TextView welcomeSeasonText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

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

        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);

        welcomeText = findViewById(R.id.welcomeText);
        String familyName = sharedPreferences.getString("FamilyName", "Family");
        String welcomeTime = "Good Morning, ";
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if(currentHour>=6 && currentHour<12) welcomeTime = "Good Morning, ";
        else if(currentHour>=12 && currentHour<17) welcomeTime = "Good Afternoon, ";
        else if(currentHour>=17 && currentHour<24) welcomeTime = "Good Evening, ";

        String welcomeTextString = welcomeTime + familyName + "s. "
                + '\n' + "What do you want to do today?";

        welcomeText.setText(welcomeTextString);

        welcomeTasksText = findViewById(R.id.welcomeTasksText);
        int tasks = sharedPreferences.getInt("activeNicheTasks", 0);
        if(tasks == 0)
            welcomeTasksText.setText("You currently have no tasks assigned :(" + '\n' + "Visit niche tasks to assign a new one now!");
        else if(tasks == 1)
            welcomeTasksText.setText("You currently have " + tasks + " task assigned." + '\n' + "Visit niche tasks to assign more!");
        else
            welcomeTasksText.setText("You currently have " + tasks + " tasks assigned." + '\n' + "Visit niche tasks to assign more!");


        welcomeSeasonText = findViewById(R.id.welcomeSeasonText);
        boolean season = sharedPreferences.getBoolean("ongoingMMC", false);
        if(season){
            int seasonNumber = sharedPreferences.getInt("seasonMMC", 1);
            welcomeSeasonText.setText("MyMommy League Season #" + seasonNumber + " is ongoing. Visit billboard to see the table toppers!");
        }
        else
            welcomeSeasonText.setText("A MyMommy season isn't active :( Visit billboard now to start a new one!");


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //no item selected on welcome page
        bottomNavigationView.setSelectedItemId(R.id.HomeNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.HomeNav:
                        return true;
                    case R.id.NicheTasksNav:
                        startActivity(new Intent(getApplicationContext(), NicheTasks.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.BillboardNav:
                        startActivity(new Intent(getApplicationContext(), PointsTally.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ProfileNav:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;

                }

                return false;
            }
        });

    }

}
