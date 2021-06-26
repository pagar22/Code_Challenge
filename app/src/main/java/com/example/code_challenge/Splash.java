package com.example.code_challenge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private int splash_time = 2000;
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
                Intent splashIntent;
                if(isRegistered)
                    splashIntent = new Intent(getApplicationContext(), MainActivity.class);
                else
                    splashIntent = new Intent(getApplicationContext(), Registration.class);



                //for MyMommy Season
                long currentTimeInMillis = System.currentTimeMillis();
                long actionTimeInFuture = sharedPreferences.getLong("actionTimeMMC", Long.MAX_VALUE);

                //if currentTime has exceeded the notificationTime, send a notification for season end
                if(currentTimeInMillis >= actionTimeInFuture){
                    Intent intent = new Intent(getApplicationContext(), NotificationBroadcastBuilder.class);
                    int mmcSeasonNumber = sharedPreferences.getInt("seasonMMC", 1);
                    intent.putExtra("season_number", mmcSeasonNumber);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    alarmManager.set(
                            AlarmManager.RTC_WAKEUP, currentTimeInMillis, pendingIntent
                    );

                    SharedPreferences.Editor editor = getSharedPreferences("Code_Challenge", MODE_PRIVATE).edit();
                    int familyMembers = sharedPreferences.getInt("members", 0);
                    int currentPoints = 0;
                    for(int i=1; i<=familyMembers; i++){
                        String memberName = sharedPreferences.getString("member"+i+"Name", "");
                        int pointsOnSeasonEnd = sharedPreferences.getInt(memberName+"PointsOnSeasonEnd", -1);

                        if(pointsOnSeasonEnd == -1) {
                            currentPoints = sharedPreferences.getInt(memberName + "Points", 0);

                            editor.putInt(memberName + "PointsOnSeasonEnd", currentPoints);
                            editor.commit();
                        }
                    }
                }

                startActivity(splashIntent);
                finish();
            }
        },splash_time);
    }


}
