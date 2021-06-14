package com.example.code_challenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


        runButton(one, "one");
        runButton(two, "two");
        runButton(three, "three");
        runButton(four, "four");

        runButtonLongClick(one, "one");
        runButtonLongClick(two, "two");
        runButtonLongClick(three, "three");
        runButtonLongClick(four, "four");

        Button pointsTally = findViewById(R.id.pointsTally);
        pointsTally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PointsTally.class);
                startActivity(intent);
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

    private void runButton(final Button button, final String key){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open dialog to set new niche task
                if(button.getText().equals("ADD A NICHE NOW!")){
                    final EditText editText = new EditText(getApplicationContext());
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    new AlertDialog.Builder(getApplicationContext())
                            .setTitle("Enter Niche Name")
                            .setView(editText)

                            .setPositiveButton("ADD!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String buttonOne = one.getText().toString();
                                    String buttonTwo = two.getText().toString();
                                    String buttonThree = three.getText().toString();
                                    String buttonFour = four.getText().toString();

                                    String input = editText.getText().toString();
                                    if (input.equals("")
                                            || input.trim().equalsIgnoreCase(buttonOne)
                                            || input.trim().equalsIgnoreCase(buttonTwo)
                                            || input.trim().equalsIgnoreCase(buttonThree)
                                            || input.trim().equalsIgnoreCase(buttonFour))
                                        Toast.makeText(getApplicationContext(),
                                                "Please enter a valid name (no blanks, no repeats)", Toast.LENGTH_LONG).show();
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
                //else open existing task activity
                else{
                    Intent intent = new Intent(getApplicationContext(), NicheTasksList.class);
                    intent.putExtra("TaskName", button.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void runButtonLongClick(final Button button, final String key){
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(button.getText().equals("ADD A NICHE NOW!"))
                    Toast.makeText(getApplicationContext(), "Please set the niche first", Toast.LENGTH_LONG).show();
                //open dialog to delete/edit existing niche
                else{
                    final String existingName = button.getText().toString();
                    final EditText editText = new EditText(getApplicationContext());
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    editText.setText(existingName);
                    new AlertDialog.Builder(getApplicationContext())
                            .setTitle("Niche Settings")
                            .setView(editText)

                            .setPositiveButton("RENAME", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String buttonOne = one.getText().toString();
                                    String buttonTwo = two.getText().toString();
                                    String buttonThree = three.getText().toString();
                                    String buttonFour = four.getText().toString();

                                    String input = editText.getText().toString();
                                    if (input.equals("")
                                            || input.trim().equalsIgnoreCase(buttonOne)
                                            || input.trim().equalsIgnoreCase(buttonTwo)
                                            || input.trim().equalsIgnoreCase(buttonThree)
                                            || input.trim().equalsIgnoreCase(buttonFour))
                                        Toast.makeText(getApplicationContext(),
                                                "Please enter a valid new name (no blanks, no repeats)", Toast.LENGTH_LONG).show();
                                    else if(input.trim().equalsIgnoreCase(existingName))
                                        Toast.makeText(getApplicationContext(),
                                                "No changes found!", Toast.LENGTH_SHORT).show();
                                    else {
                                        //will overwrite existing name since key is same
                                        SharedPreferences sharedPreferences =
                                                getSharedPreferences("Code_Challenge", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(key, editText.getText().toString());
                                        editor.commit();
                                        button.setText(editText.getText().toString());
                                    }
                                }
                            })
                            .setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove("TaskList"+button.getText().toString());
                                    editor.commit();

                                    Toast.makeText(getApplicationContext(),
                                            "Deleted "+button.getText().toString()+" Niche", Toast.LENGTH_SHORT).show();
                                    button.setText("ADD A NICHE NOW!");
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
                return true;
            }
        });
    }
}
