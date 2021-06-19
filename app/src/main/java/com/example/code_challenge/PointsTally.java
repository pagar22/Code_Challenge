package com.example.code_challenge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PointsTally extends AppCompatActivity {

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.points_tally);

        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
        final int members = sharedPreferences.getInt("members", 0);

        //linear layout for all columns
        linearLayout = findViewById(R.id.pointsTallyView);

        int counter = 5; //relativ view ID for pointsButton, since i can't exceed 4, counter = 5
        for(int i=1; i<=members; i++){
            //relative layout for each row
            RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());

            //name button
            RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            nameParams.setMargins(100,50,100,50);
            nameParams.addRule(RelativeLayout.ABOVE, R.id.pointsTallyButtonView); //does not work because of linear layout constraints
            nameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            Button nameButton = new Button(getApplicationContext());
            nameButton.setId(i);
            String memberName = sharedPreferences.getString("member"+i+"Name", "");
            setButton(nameButton, memberName);
            nameButton.setLayoutParams(nameParams);
            relativeLayout.addView(nameButton);

            //points button
            RelativeLayout.LayoutParams pointParams = new RelativeLayout.LayoutParams(
                    300, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            pointParams.setMargins(20,50,50,50);
            pointParams.addRule(RelativeLayout.RIGHT_OF, i);
            Button pointsButton = new Button(getApplicationContext());
            pointsButton.setId(counter); //increment counter value for next iteration
            int memberPoints = sharedPreferences.getInt(memberName+"Points", 0);
            setButton(pointsButton, Integer.toString(memberPoints));
            pointsButton.setLayoutParams(pointParams);
            relativeLayout.addView(pointsButton);

            //star images
            RelativeLayout.LayoutParams starParams = new RelativeLayout.LayoutParams(500, 150);
            starParams.setMargins(50,50,50,50);
            starParams.addRule(RelativeLayout.RIGHT_OF, counter);
            starParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ImageView stars = new ImageView(getApplicationContext());
            String quality = "";

            if (memberPoints < 0 || memberPoints <= 20) {
                stars.setImageResource(R.drawable.one_star);
                quality = "One Star :( You need to put more effort";
            }
            else if(memberPoints > 20 && memberPoints <= 40) {
                stars.setImageResource(R.drawable.two_star);
                quality = "Two Stars :( You're getting there";
            }
            else if(memberPoints > 40 && memberPoints <= 60) {
                stars.setImageResource(R.drawable.three_star);
                quality = "Three Stars :| Your contributions are valuable!";
            }
            else if(memberPoints > 60 && memberPoints <= 80) {
                stars.setImageResource(R.drawable.four_star);
                quality = "Four Stars :) You're a key family member!";
            }
            else if(memberPoints > 80) {
                stars.setImageResource(R.drawable.five_star);
                quality = "Five Stars :) YOU RUN THE HOUSE!";
            }

            final String finalQuality = quality;
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), finalQuality, Toast.LENGTH_LONG).show();
                }
            });
            stars.setLayoutParams(starParams);
            relativeLayout.addView(stars);

            linearLayout.addView(relativeLayout);

            //for line under every row
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 4);
            lineParams.setMargins(5,0,5,0);
            View line = new View(getApplicationContext());
            line.setBackground(getDrawable(R.color.colorPrimary));
            line.setLayoutParams(lineParams);
            linearLayout.addView(line);

            counter++;
        }

        Button reset = findViewById(R.id.pointsTallyReset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PointsTally.this)
                        .setTitle("Reset Points Tally")
                        .setMessage("Are you sure you want to reset all points to zero?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPrefers = getSharedPreferences("Code_Challenge", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPrefers.edit();
                                for(int i=1; i<=members; i++){
                                    String memberName = sharedPrefers.getString("member"+i+"Name", "");
                                    editor.putInt(memberName+"Points", 0);
                                }
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "All points were reset", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });

        Button home = findViewById(R.id.pointsTallyHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setButton(Button button, String name){
        button.setText(name);
        button.setTextColor(Color.WHITE);
        button.setPadding(20,20,20,20);
        button.setBackground(getDrawable(R.drawable.theme_button));
        button.setClickable(false);
    }
}
