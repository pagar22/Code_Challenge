package com.example.code_challenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MMCWinner extends AppCompatActivity {

    private TextView congratulationsText;
    private TextView MMCWinnerName;
    private TextView descriptionText;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmc_winner);

        congratulationsText = findViewById(R.id.congratulationsText);
        MMCWinnerName = findViewById(R.id.MMCWinnerName);
        descriptionText = findViewById(R.id.descriptionText);

    }

    public void showGif(View view) {
        Button button = findViewById(R.id.buttonWinner);
        button.setVisibility(View.INVISIBLE);
        button.setClickable(false);

        ImageView fireworks1 = findViewById(R.id.fireworks1);
        Glide.with(this).load(R.drawable.fireworks).into(fireworks1);

        ImageView fireworks2 = findViewById(R.id.fireworks2);
        Glide.with(this).load(R.drawable.fireworks).into(fireworks2);

        onSeasonEnd(getApplicationContext());

    }

    public void onSeasonEnd(Context context){

        SharedPreferences sharedPreferencesAppend = context.getSharedPreferences("Code_Challenge", MODE_APPEND);
        int familyMembers = sharedPreferencesAppend.getInt("members", 0);

        //get the winner
        String maxMember = "";
        int maxPoints = -1;
        boolean tie = false;
        for (int i = 1; i <= familyMembers; i++) {
            String memberName = sharedPreferencesAppend.getString("member" + i + "Name", "");
            if (!memberName.equals("")) {
                int memberPoints = sharedPreferencesAppend.getInt(memberName + "PointsOnSeasonEnd", -1);
                //if PointsOnSeasonEnd was never set (in Splash or PointsTally), take the value as current points instead
                if(memberPoints==-1)
                    memberPoints = sharedPreferencesAppend.getInt(memberName + "Points", 0);

                if (memberPoints > maxPoints) {
                    maxMember = memberName;
                    maxPoints = memberPoints;
                    tie = false;
                } else if (memberPoints == maxPoints) {
                    maxMember += " & " + memberName;
                    tie = true;
                }

            }
        }

        //create congratulatory strings & increment MMC wins
        SharedPreferences.Editor editor = context.getSharedPreferences("Code_Challenge", MODE_PRIVATE).edit();

        int mmcSeason = sharedPreferencesAppend.getInt("seasonMMC", 1);
        String incentiveDetailsFull = sharedPreferencesAppend.getString(mmcSeason + "Incentive", "");
        String incentiveDetails = "";
        //both are mandatory fields and will be found
        int startIndex = incentiveDetailsFull.indexOf("General Incentive");
        int endIndex = incentiveDetailsFull.lastIndexOf("Duration");
        System.out.println("Start - "+startIndex + " End - "+endIndex);
        if (startIndex != -1 && endIndex != -1)
            incentiveDetails = incentiveDetailsFull.substring(startIndex, endIndex);
        String description;
        if (tie) {
            String[] allWinners = maxMember.split(" & ");
            for (String s : allWinners) {
                int currentMMCWins = sharedPreferencesAppend.getInt(s + "MMC", 0);
                editor.putInt(s + "MMC", currentMMCWins + 1);
            }
            description = '\n' + "With " + maxPoints + " points, " + maxMember + " have won MyMommy Season # " + mmcSeason
                    + ". Winners are now eligible to claim the following rewards: " + '\n'
                    + '\n' + incentiveDetails + '\n'
                    + "Keep putting similar effort into vital housework tasks to win more MyMommy Cups."
                    + " All the best for future seasons!" + '\n'
                    + "- The MyMommy Team";
        } else {
            int currentMMCWins = sharedPreferencesAppend.getInt(maxMember + "MMC", 0);
            editor.putInt(maxMember + "MMC", currentMMCWins + 1);
            description = '\n' + "With " + maxPoints + " points, " + maxMember + " has won MyMommy Season # " + mmcSeason
                    + ". You are now eligible to claim the following rewards: " + '\n'
                    + '\n' + incentiveDetails + '\n'
                    + "You have won" + (currentMMCWins + 1) + " MyMommy Cups. Keep putting similar effort into vital housework tasks to win more."
                    + " All the best for future seasons!" + '\n'
                    + "- The MyMommy Team";
        }

        MMCWinnerName.setText(maxMember);
        descriptionText.setText(description);

        //change system status to new MyMommy
        editor.putInt("seasonMMC", mmcSeason + 1);
        editor.putBoolean("ongoingMMC", false);
        editor.putString(mmcSeason + "Incentive", "");
        editor.putLong("actionTimeMMC", Long.MAX_VALUE);

        editor.commit();

        congratulationsText.setVisibility(View.VISIBLE);
        MMCWinnerName.setVisibility(View.VISIBLE);
        descriptionText.setVisibility(View.VISIBLE);

        Button home = findViewById(R.id.homeWinner);
        home.setVisibility(View.VISIBLE);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
