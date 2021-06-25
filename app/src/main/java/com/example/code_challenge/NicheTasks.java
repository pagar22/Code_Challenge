package com.example.code_challenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NicheTasks extends AppCompatActivity {

    Button one;
    Button two;
    Button three;
    Button four;

    TextView noNicheText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.niche_tasks);

        one = findViewById(R.id.task1);
        two = findViewById(R.id.task2);
        three = findViewById(R.id.task3);
        four = findViewById(R.id.task4);
        noNicheText = findViewById(R.id.noNicheText);

        setButton(one, "one");
        setButton(two, "two");
        setButton(three, "three");
        setButton(four, "four");


        runButton(one);
        runButton(two);
        runButton(three);
        runButton(four);

        runButtonLongClick(one, "one");
        runButtonLongClick(two, "two");
        runButtonLongClick(three, "three");
        runButtonLongClick(four, "four");

        setNoNicheText();

        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //NicheTasks item
        bottomNavigationView.setSelectedItemId(R.id.NicheTasksNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.HomeNav:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.NicheTasksNav:
                        return true;
                    case R.id.BillboardNav:
                        startActivity(new Intent(getApplicationContext(), PointsTally.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.ProfileNav:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                }

                return false;
            }
        });

        //add button
        Button add = findViewById(R.id.nicheTasksAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(one.getVisibility() == View.INVISIBLE)
                    addButton(one, "one");
                else if(two.getVisibility() == View.INVISIBLE)
                    addButton(two, "two");
                else if(three.getVisibility() == View.INVISIBLE)
                    addButton(three, "three");
                else
                    addButton(four, "four");

            }
        });

        //reset button
        Button resetAll = findViewById(R.id.nicheTasksReset);
        resetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!noNicheText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please add a niche first!", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(NicheTasks.this)
                            .setTitle("Reset Niche Tasks")
                            .setMessage("All current niche tasks and their task lists will be permanently reset. Are you sure you want to continue?")
                            .setIcon(R.drawable.warning)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteButton(one, "one");
                                    deleteButton(two, "two");
                                    deleteButton(three, "three");
                                    deleteButton(four, "four");

                                    Toast.makeText(getApplicationContext(), "All niche tasks were reset", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            }).show();
                }
            }
        });
    }

    private void setButton(Button button, String key){
        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
        String string= sharedPreferences.getString(key, "");
        if(string.equals(""))
            button.setVisibility(View.INVISIBLE);
        else {
            button.setVisibility(View.VISIBLE);
            button.setText(string);
        }
    }

    private void addButton(final Button button, final String key){
        final EditText editText = new EditText(getApplicationContext());
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setHint("(eg. Kitchen)");
        new AlertDialog.Builder(NicheTasks.this)
                .setTitle("Enter A Niche Title")
                .setView(editText)
                .setIcon(R.drawable.create)
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
                                || input.trim().equalsIgnoreCase(buttonFour)
                                || input.equals("ADD A NICHE NOW!"))
                            Toast.makeText(getApplicationContext(),
                                    "Please enter a valid name (no blanks, no repeats)", Toast.LENGTH_LONG).show();
                        else {
                            SharedPreferences sharedPreferences =
                                    getSharedPreferences("Code_Challenge", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(key, editText.getText().toString());
                            editor.commit();
                            button.setVisibility(View.VISIBLE);
                            button.setText(editText.getText().toString());
                            setNoNicheText();
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

    private void runButton(final Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NicheTasksList.class);
                intent.putExtra("TaskName", button.getText().toString());
                startActivity(intent);

            }
        });
    }

    private void runButtonLongClick(final Button button, final String key) {
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String existingName = button.getText().toString();
                final EditText editText = new EditText(getApplicationContext());
                editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                editText.setText(existingName);
                new AlertDialog.Builder(NicheTasks.this)
                        .setTitle("Niche Settings")
                        .setView(editText)
                        .setIcon(R.drawable.settings)
                        .setPositiveButton("RENAME", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String buttonOne = one.getText().toString();
                                String buttonTwo = two.getText().toString();
                                String buttonThree = three.getText().toString();
                                String buttonFour = four.getText().toString();

                                String input = editText.getText().toString();
                                if (input.trim().equalsIgnoreCase(existingName))
                                    Toast.makeText(getApplicationContext(),
                                            "No changes found!", Toast.LENGTH_SHORT).show();
                                else if (input.equals("")
                                        || input.trim().equalsIgnoreCase(buttonOne)
                                        || input.trim().equalsIgnoreCase(buttonTwo)
                                        || input.trim().equalsIgnoreCase(buttonThree)
                                        || input.trim().equalsIgnoreCase(buttonFour))
                                    Toast.makeText(getApplicationContext(),
                                            "Please enter a valid new name (no blanks, no repeats)", Toast.LENGTH_LONG).show();
                                else {
                                    String newName = editText.getText().toString();
                                    //will overwrite existing name since key is same
                                    SharedPreferences sharedPreferencesEdit =
                                            getSharedPreferences("Code_Challenge", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferencesEdit.edit();
                                    editor.putString(key, editText.getText().toString());
                                    editor.commit();

                                    NicheTasksObject.save(getApplicationContext(), newName);
                                    button.setText(newName);
                                }
                            }
                        })
                        .setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteButton(button, key);
                                Toast.makeText(getApplicationContext(),
                                        "Deleted " + button.getText().toString() + " Niche", Toast.LENGTH_SHORT).show();
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
                return true;
            }
        });

    }

    private void deleteButton(Button button, String key){
        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.remove("TaskList"+button.getText().toString());
        editor.commit();
        button.setVisibility(View.INVISIBLE);

        //refresh the activity after deleting
        Intent intent = new Intent(getApplicationContext(), NicheTasks.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);

        setNoNicheText();
    }

    private void setNoNicheText(){
        if((one.getVisibility() == View.INVISIBLE)
                && (two.getVisibility() == View.INVISIBLE)
                && (three.getVisibility() == View.INVISIBLE)
                && (four.getVisibility() == View.INVISIBLE))
            noNicheText.setText("Get started by adding a new niche!");
        else
            noNicheText.setText("");
    }


}
