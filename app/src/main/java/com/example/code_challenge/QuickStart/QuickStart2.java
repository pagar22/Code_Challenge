package com.example.code_challenge.QuickStart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.code_challenge.MainActivity;
import com.example.code_challenge.R;

public class QuickStart2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_start);


        TextView title = findViewById(R.id.quickStartTitle);
        title.setText("Billboard");

        ImageView image1 = findViewById(R.id.quickStartImage1);
        image1.setImageDrawable(getDrawable(R.drawable.billboard1));
        ImageView image2 = findViewById(R.id.quickStartImage2);
        image2.setImageDrawable(getDrawable(R.drawable.billboard2));

        TextView description = findViewById(R.id.quickStartDescription);
        description.setText("Billboard is an overview of all the points gained by each family member for successfully completing niche tasks."
                + '\n' + "Fill out the MyMommy league form to begin a new, competitive season. The winner is the billboard topper at the end of the season duration and can claim all the stipulated incentives!");

        ImageView progress = findViewById(R.id.quickStartProgress);
        progress.setImageDrawable(getDrawable(R.drawable.progress2));


        TextView home = findViewById(R.id.quickStartHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button previous = findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), QuickStart1.class);
                startActivity(intent);
                finish();
            }
        });

        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuickStart3.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
