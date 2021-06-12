package com.example.code_challenge;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class NicheTasksList extends AppCompatActivity {

    ListView listView;
    static ArrayList<NicheTasksObject> nicheTasks = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    Button addMB;
    static TextView noItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        final Intent getIntent = getIntent();

        TextView heading = findViewById(R.id.heading);
        heading.setText(getIntent.getStringExtra("TaskName"));

        listView = findViewById(R.id.itemList);
        arrayAdapter = new ArrayAdapter(NicheTasksList.this, android.R.layout.simple_list_item_1, nicheTasks);
        listView.setAdapter(arrayAdapter);


        noItemText = findViewById(R.id.noItemText);
        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
        int size = sharedPreferences.getInt("TaskListSize", -1);
        if(size<=0)
            noItemText.setText(R.string.no_item_text);
        else{
            for(int i=0; i<=size; i++){
                String key = "Task"+i;
                String task = sharedPreferences.getString(key, "");
                NicheTasksObject taskObject = NicheTasksObject.parser(task);
                noItemText.setText("");
                nicheTasks.add(null);
                nicheTasks.set(i, taskObject);
                NicheTasksList.arrayAdapter.notifyDataSetChanged();
            }
        }


        addMB = findViewById(R.id.addMB);
        addMB.setText("ADD TASK!");
        addMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NicheTasksAdd.class);
                intent.putExtra("TaskName", getIntent.getStringExtra("TaskName"));
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NicheTasksObject nicheTask = (NicheTasksObject) listView.getItemAtPosition(position);
                new AlertDialog.Builder(NicheTasksList.this)
                        .setTitle(getIntent.getStringExtra("TaskName")+" Task")
                        .setMessage(nicheTask.toString())
                        .setNeutralButton("Done", null)
                        .show();
            }
        });
    }
}
