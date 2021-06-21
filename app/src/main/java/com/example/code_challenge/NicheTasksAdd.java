package com.example.code_challenge;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NicheTasksAdd extends AppCompatActivity {

    Button save;
    Button cancel;
    EditText description;
    List<String> assignedTo = new ArrayList<>();

    TextView datePickText;
    DatePickerDialog datePicker;
    String date = "";

    TextView timePickText;
    TimePickerDialog timePicker;
    String time = "";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.niche_tasks_add);

        final Intent getIntent = getIntent();

        //for checkboxes
        LinearLayout linearLayout = findViewById(R.id.checkboxView);
        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
        int members = sharedPreferences.getInt("members", 0);
        for(int i=1; i<=members;i++){
            final CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setId(i);
            String key = "member" + i + "Name";
            checkBox.setText(sharedPreferences.getString(key, ""));
            linearLayout.addView(checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        assignedTo.add(checkBox.getText().toString());
                    else
                        assignedTo.remove(checkBox.getText().toString());
                }
            });
        }
        final Calendar calendar = Calendar.getInstance();

        datePickText = findViewById(R.id.datePickText);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        timePickText = findViewById(R.id.timePickText);
        final int min = calendar.get(Calendar.MINUTE);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);

        timePickText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(NicheTasksAdd.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                if(hourOfDay >= 0 && hourOfDay <= 9) time = "0" + hourOfDay + " : " + minute;
                                if(minute >= 0 && minute <= 9) time = hourOfDay + " : " + "0" + minute;
                                if((hourOfDay >= 0 && hourOfDay <= 9) && (minute >= 0 && minute <= 9))
                                    time = "0" + hourOfDay + " : 0" + minute;
                                if(hourOfDay > 9 && minute > 9) time = hourOfDay + " : " + minute;

                                timePickText.setText(time);
                            }
                        }, hour, min, true);
                timePicker.show();
            }
        });

        datePicker = new DatePickerDialog(NicheTasksAdd.this);
        datePickText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePickerDialog(NicheTasksAdd.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        date = dayOfMonth + "/" + (month+1) + "/" + year;
                        datePickText.setText(date);
                    }
                }, year, month, day);

                // set min date
                datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePicker.show();
            }
        });

        description = findViewById(R.id.taskDescription);
        save = findViewById(R.id.taskSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(description.equals("") || assignedTo.isEmpty()
                        || time.equals("") || date.equals(""))
                    Toast.makeText(getApplicationContext(), "Invalid input, please fill all fields",
                            Toast.LENGTH_LONG).show();
                else {
                    String title = getIntent.getStringExtra("TaskName");
                    NicheTasksObject taskObject = new NicheTasksObject(title, description.getText().toString(),
                            assignedTo, time + " | " + date);
                    NicheTasksList.noItemText.setText("");
                    NicheTasksList.nicheTasks.add(taskObject);
                    NicheTasksList.arrayAdapter.notifyDataSetChanged();
                    NicheTasksObject.save(getApplicationContext(), title);

                    //share on WhatsApp, Email, etc.
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TITLE, "You've been assigned with a "+title+" title!");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, taskObject.toString());
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, title);
                    //shareIntent.putExtra(Intent.EXTRA_CHOOSER_TARGETS, )
                    startActivity(shareIntent);
                    finish();

                }
            }
        });

        cancel = findViewById(R.id.taskCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }

}
