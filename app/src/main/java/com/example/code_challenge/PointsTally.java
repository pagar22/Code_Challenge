package com.example.code_challenge;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PointsTally extends AppCompatActivity {

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.points_tally);

        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
        int members = sharedPreferences.getInt("members", 3);

        //linear layout for all columns
        linearLayout = findViewById(R.id.pointsTallyView);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(100,100,100,50);

        for(int i=1; i<=members; i++){
            //relative layout for each row
            RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(100,100,100,50);
            layoutParams.addRule(RelativeLayout.RIGHT_OF, i);

            //name button
            Button nameButton = new Button(getApplicationContext());
            nameButton.setId(i);
            String memberName = sharedPreferences.getString("member"+i+"Name", "Aaryan");
            setButton(nameButton, memberName);
            nameButton.setLayoutParams(buttonParams);
            relativeLayout.addView(nameButton);

            //points button
            Button pointsButton = new Button(getApplicationContext());
            int memberPoints = sharedPreferences.getInt(memberName+"Points", 0);
            setButton(pointsButton, Integer.toString(memberPoints));
            pointsButton.setLayoutParams(layoutParams);
            relativeLayout.addView(pointsButton);

            linearLayout.addView(relativeLayout);
        }

    }

    private void setButton(Button button, String name){
        button.setText(name);
        button.setTextColor(Color.WHITE);
        button.setPadding(20,20,20,20);
        button.setBackground(getDrawable(R.drawable.background_color));
        button.setClickable(false);
    }
}
