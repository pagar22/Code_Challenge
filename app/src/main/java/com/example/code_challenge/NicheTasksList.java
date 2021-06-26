package com.example.code_challenge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class NicheTasksList extends AppCompatActivity {

    ListView listView;
    static ArrayList<NicheTasksObject> nicheTasks = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    Button addMB;
    static TextView noItemText;
    static ImageView noItemImage;
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
        arrayAdapter = new ArrayAdapter<NicheTasksObject>
                (NicheTasksList.this, R.layout.custom_card_view, nicheTasks){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.custom_card_view, null, true);
                NicheTasksObject tasksObject = nicheTasks.get(position);

                //set image
                ImageView imageView = view.findViewById(R.id.cardViewImage);
                Random random = new Random();
                int choice = random.nextInt(4);
                switch (choice){
                    case 0: imageView.setImageDrawable(getDrawable(R.drawable.broom));
                    break;
                    case 1: imageView.setImageDrawable(getDrawable(R.drawable.cooking));
                        break;
                    case 2: imageView.setImageDrawable(getDrawable(R.drawable.laundry));
                        break;
                    case 3: imageView.setImageDrawable(getDrawable(R.drawable.dishes));
                        break;
                }

                //set description
                TextView description = view.findViewById(R.id.cardViewDescription);
                description.setText(tasksObject.description);

                //set assignedTo
                TextView assignedTo = view.findViewById(R.id.cardViewAssign);
                String assignedTOString = "Assigned to: " + tasksObject.assignedTo.toString()
                        .replace("[", "").replace("]","");
                assignedTo.setText(assignedTOString);

                //set date
                TextView date = view.findViewById(R.id.cardViewDate);
                date.setText(tasksObject.date);

                CardView cardView = view.findViewById(R.id.customCardView);
                cardView.setBackgroundColor(Color.WHITE);

                return view;
            }
        };
        listView.setAdapter(arrayAdapter);

        noItemText = findViewById(R.id.noItemText);
        noItemImage = findViewById(R.id.noTasksImage);
        SharedPreferences sharedPrefAppend = getSharedPreferences("Code_Challenge", MODE_APPEND);
        Set<String> decoy = new HashSet<>();
        Set<String> set = sharedPrefAppend.getStringSet("TaskList"+title, decoy);

        nicheTasks.clear();
        if(set.size()==0) {
            noItemText.setText(R.string.no_item_text);
            noItemImage.setVisibility(View.VISIBLE);
        }
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

                final NicheTasksObject nicheTask = (NicheTasksObject) listView.getItemAtPosition(position);
                new AlertDialog.Builder(NicheTasksList.this)
                        .setTitle(getIntent.getStringExtra("TaskName")+" Task")
                        .setMessage(nicheTask.toString())
                        .setIcon(R.drawable.create)
                        .setPositiveButton("Done", null)
                        .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TITLE, "You've been assigned with a "+title+" title!");
                                sendIntent.putExtra(Intent.EXTRA_TEXT, nicheTask.toString());
                                sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                                sendIntent.setType("text/plain");
                                Intent shareIntent = Intent.createChooser(sendIntent, title);
                                startActivity(shareIntent);
                            }
                        })
                        .show();

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                 new AlertDialog.Builder(NicheTasksList.this)
                         .setTitle("Task Settings")
                         .setMessage("Delete this task or mark it as overdue/completed. Points will be deducted/rewarded accordingly.")
                         .setIcon(R.drawable.settings)
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
        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //to decrement no. of active niche tasks
        int currentTasks = sharedPreferences.getInt("activeNicheTasks", 0);
        if(currentTasks != 0)
            editor.putInt("activeNicheTasks", currentTasks - 1);

        editor.commit();
        NicheTasksObject.save(getApplicationContext(), title);

        if(nicheTasks.size()==0) {
            noItemText.setText(R.string.no_item_text);
            noItemImage.setVisibility(View.VISIBLE);
        }
        return tasksObject;
    }

    //flag = true for add, flag = false for subtract
    private void pointsController(NicheTasksObject tasksObject, boolean flag, int points){
        for(String s : tasksObject.assignedTo){
            //update points for each member in assignedTo list
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
