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

public class QuickStart3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_start);


        TextView title = findViewById(R.id.quickStartTitle);
        title.setText("MyMommy");

        ImageView image1 = findViewById(R.id.quickStartImage1);
        image1.setImageDrawable(getDrawable(R.drawable.general1));
        ImageView image2 = findViewById(R.id.quickStartImage2);
        image2.setImageDrawable(getDrawable(R.drawable.general2));

        TextView description = findViewById(R.id.quickStartDescription);
        description.setText("The home page gives you a brief overview of your active Niche Tasks and MyMommy season. You can edit your family name or view members' league wins in the profile section (which is also where I'll be in case you need help again!). And that's largely everything, enjoy your MyMommy experience!");

        ImageView progress = findViewById(R.id.quickStartProgress);
        progress.setImageDrawable(getDrawable(R.drawable.progress3));


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
                Intent intent = new Intent(getApplicationContext(), QuickStart2.class);
                startActivity(intent);
                finish();
            }
        });

        Button next = findViewById(R.id.next);
        next.setText("FINISH");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
