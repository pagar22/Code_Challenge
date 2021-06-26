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

public class QuickStart1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_start);


        TextView title = findViewById(R.id.quickStartTitle);
        title.setText("Niche Tasks");

        ImageView image1 = findViewById(R.id.quickStartImage1);
        image1.setImageDrawable(getDrawable(R.drawable.niche1));
        ImageView image2 = findViewById(R.id.quickStartImage2);
        image2.setImageDrawable(getDrawable(R.drawable.niche2));

        TextView description = findViewById(R.id.quickStartDescription);
        description.setText("Add and share particular household departments (like kitchen) and their corresponding day-to-day chores (like washing the dishes) within Niche Tasks."
                + '\n' + "Long click on a task to end it. A promptly completed task would fetch you +5 points while an overdue task would get you an unfortunate -2 points.");

        ImageView progress = findViewById(R.id.quickStartProgress);
        progress.setImageDrawable(getDrawable(R.drawable.progress1));


        TextView home = findViewById(R.id.quickStartHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button previous = findViewById(R.id.previous);
        previous.setVisibility(View.INVISIBLE);

        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuickStart2.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
