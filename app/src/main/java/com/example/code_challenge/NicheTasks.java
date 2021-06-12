package com.example.code_challenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(one.getText().equals("ADD A NICHE NOW!"))
                    addNewTask(one, "one");
                else{
                    Intent intent = new Intent(getApplicationContext(), NicheTasksList.class);
                    intent.putExtra("TaskName", one.getText().toString());
                    startActivity(intent);
                }
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(two.getText().equals("ADD A NICHE NOW!"))
                    addNewTask(two, "two");
                else{
                    Intent intent = new Intent(getApplicationContext(), NicheTasksList.class);
                    intent.putExtra("TaskName", two.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }

    private void setButtonText(Button button, String key){
        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
        String string= sharedPreferences.getString(key, "");
        if(string.equals(""))
            button.setText("ADD A NICHE NOW!");
        else
            button.setText(string);
    }

    private void addNewTask(final Button button, final String key){
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(this)
                .setTitle("Enter Niche Name")
                .setView(editText)

                .setPositiveButton("ADD!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editText.getText().toString().equals(""))
                                    Toast.makeText(getApplicationContext(),
                                            "Please enter a valid name", Toast.LENGTH_LONG).show();
                                else {
                                    SharedPreferences sharedPreferences =
                                            getSharedPreferences("Code_Challenge", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(key, editText.getText().toString());
                                    editor.commit();
                                    button.setText(editText.getText().toString());
                                }
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),
                                        "Cancelled", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        })
                .show();
    }
}
