package com.example.code_challenge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NicheTasks extends AppCompatActivity {

    Button one;
    Button two;
    Button three;
    Button four;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.niche_tasks);

        one = findViewById(R.id.task1);
        two = findViewById(R.id.task2);
        three = findViewById(R.id.task3);
        four = findViewById(R.id.task4);

        setButtonText(one, "one");
        setButtonText(two, "two");
        setButtonText(three, "three");
        setButtonText(four, "four");

    }

    private void setButtonText(Button button, String key){
        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
        String string= sharedPreferences.getString(key, "");
        if(string.equals(""))
            button.setText("ADD A TASK NOW!");
        else
            button.setText(string);
    }
}
