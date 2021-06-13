package com.example.code_challenge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NicheTasksList extends AppCompatActivity {

    ListView listView;
    static ArrayList<NicheTasksObject> nicheTasks = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    Button addMB;
    static TextView noItemText;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        final Intent getIntent = getIntent();
        title = getIntent.getStringExtra("TaskName");
        TextView heading = findViewById(R.id.heading);
        heading.setText(title);

        listView = findViewById(R.id.itemList);
        arrayAdapter = new ArrayAdapter(NicheTasksList.this, android.R.layout.simple_list_item_1, nicheTasks);
        listView.setAdapter(arrayAdapter);

        noItemText = findViewById(R.id.noItemText);
        SharedPreferences sharedPrefAppend = getSharedPreferences("Code_Challenge", MODE_APPEND);
        Set<String> decoy = new HashSet<>();
        Set<String> set = sharedPrefAppend.getStringSet("TaskList"+title, decoy);

        nicheTasks.clear();
        if(set.size()==0)
            noItemText.setText(R.string.no_item_text);
        else {
            for (String s : set)
                nicheTasks.add(NicheTasksObject.parser(s));
        }


        addMB = findViewById(R.id.addMB);
        addMB.setText("ADD TASK!");
        addMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NicheTasksAdd.class);
                intent.putExtra("TaskName", title);
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


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                 new AlertDialog.Builder(NicheTasksList.this)
                         .setTitle("Task Settings")
                         .setMessage("Delete this task or mark it as overdue/completed. Points will be deducted/rewarded accordingly.")
                         .setPositiveButton("Completed", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                                 pointsController(deleteItem(position), true, 5);

                             }
                         })
                         .setNegativeButton("Overdue", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                                 pointsController(deleteItem(position), false, 2);

                             }
                         })
                         .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 deleteItem(position);
                                 Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                             }
                         })
                         .show();
                return true; }
        });
    }

    private NicheTasksObject deleteItem(int position){
        NicheTasksObject tasksObject = nicheTasks.remove(position);
        arrayAdapter.notifyDataSetChanged();
        NicheTasksObject.save(getApplicationContext(), title);

        if(nicheTasks.size()==0)
            noItemText.setText(R.string.no_item_text);
        return tasksObject;
    }

    //flag = true for add, flag = false for subtract
    private void pointsController(NicheTasksObject tasksObject, boolean flag, int points){
        for(String s : tasksObject.assignedTo){
            String key = s+"Points";
            SharedPreferences sharedPrefAppend = getSharedPreferences("Code_Challenge", MODE_APPEND);
            int currentPoints = sharedPrefAppend.getInt(key, 0);
            if(flag)
                currentPoints += points;
            else
                currentPoints -= points;
            SharedPreferences sharedPrefEdit = getSharedPreferences("Code_Challenge", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefEdit.edit();
            editor.putInt(key, currentPoints);
            editor.commit();
        }

        String action = (flag)? "added" : "deducted";
        Snackbar.make(findViewById(R.id.list_view),
                "Points were " + action + " successfully!", Snackbar.LENGTH_LONG)
                .setAction("View Tally", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PointsTally.class);
                        startActivity(intent);
                    }
                }).show();
    }
}
