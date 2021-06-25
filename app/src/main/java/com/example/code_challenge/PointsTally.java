package com.example.code_challenge;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PointsTally extends AppCompatActivity {

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.points_tally);

        final SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);
        final int members = sharedPreferences.getInt("members", 0);


        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //PointsTally item
        bottomNavigationView.setSelectedItemId(R.id.BillboardNav);

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
                        startActivity(new Intent(getApplicationContext(), NicheTasks.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.BillboardNav:
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


        //linear layout for all columns
        linearLayout = findViewById(R.id.pointsTallyView);

        int counter = 5; //relative view ID for pointsButton, since i can't exceed 4, counter = 5
        for(int i=1; i<=members; i++){
            //relative layout for each row
            RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());

            //name button
            RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            nameParams.setMargins(100,60,100,50);
            nameParams.addRule(RelativeLayout.ABOVE, R.id.pointsTallyButtonView); //does not work because of linear layout constraints
            nameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            Button nameButton = new Button(getApplicationContext());
            nameButton.setId(i);
            String memberName = sharedPreferences.getString("member"+i+"Name", "");
            setButton(nameButton, memberName);
            nameButton.setLayoutParams(nameParams);
            relativeLayout.addView(nameButton);

            //points button
            RelativeLayout.LayoutParams pointParams = new RelativeLayout.LayoutParams(
                    250, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            pointParams.setMargins(20,50,50,50);
            pointParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            Button pointsButton = new Button(getApplicationContext());
            pointsButton.setId(counter); //increment counter value for next iteration
            int memberPoints = sharedPreferences.getInt(memberName+"Points", 0);
            setButton(pointsButton, Integer.toString(memberPoints));
            pointsButton.setLayoutParams(pointParams);
            relativeLayout.addView(pointsButton);

            //star images
            RelativeLayout.LayoutParams starParams = new RelativeLayout.LayoutParams(250, 100);
            starParams.setMargins(50,65,50,50);
            starParams.addRule(RelativeLayout.RIGHT_OF, counter);
            starParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ImageView stars = new ImageView(getApplicationContext());
            String quality = "";

            if (memberPoints < 0 || memberPoints <= 20) {
                stars.setImageResource(R.drawable.one_star);
                quality = "One Star :( You need to put in more effort";
            }
            else if(memberPoints > 20 && memberPoints <= 40) {
                stars.setImageResource(R.drawable.two_star);
                quality = "Two Stars :( You're getting there";
            }
            else if(memberPoints > 40 && memberPoints <= 60) {
                stars.setImageResource(R.drawable.three_star);
                quality = "Three Stars :| Your contributions are valuable!";
            }
            else if(memberPoints > 60 && memberPoints <= 80) {
                stars.setImageResource(R.drawable.four_star);
                quality = "Four Stars :) You're a key family member!";
            }
            else if(memberPoints > 80) {
                stars.setImageResource(R.drawable.five_star);
                quality = "Five Stars :) YOU RUN THE HOUSE!";
            }

            final String finalQuality = quality;
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), finalQuality, Toast.LENGTH_LONG).show();
                }
            });
            stars.setLayoutParams(starParams);
            relativeLayout.addView(stars);

            linearLayout.addView(relativeLayout);

            //for line under every row
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 4);
            lineParams.setMargins(5,0,5,0);
            View line = new View(getApplicationContext());
            line.setBackground(getDrawable(R.color.colorPrimary));
            line.setLayoutParams(lineParams);
            linearLayout.addView(line);

            counter++;
        }

        Button reset = findViewById(R.id.pointsTallyReset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PointsTally.this)
                        .setTitle("Reset Points Tally")
                        .setMessage("All member(s)' current points will be reset to zero. Are you sure you want to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPrefers = getSharedPreferences("Code_Challenge", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPrefers.edit();
                                for(int i=1; i<=members; i++){
                                    String memberName = sharedPrefers.getString("member"+i+"Name", "");
                                    editor.putInt(memberName+"Points", 0);
                                }
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "All points were reset", Toast.LENGTH_SHORT).show();
                                finish();
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
        });

        Button insight = findViewById(R.id.pointsTallyInsight);
        insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuickStart.class);
                startActivity(intent);
            }
        });

        final Button mmc = findViewById(R.id.pointsTallyMMC);
        final boolean mmcOnGoing = sharedPreferences.getBoolean("ongoingMMC", false);
        final int mmcSeasonNumber = sharedPreferences.getInt("seasonMMC", 1);
        final String mmcSeasonString = "MyMommy Season # " + mmcSeasonNumber;
        final long actionTime = sharedPreferences.getLong("actionTimeMMC", Long.MAX_VALUE);

        //set background depending on state
        if(mmcOnGoing && System.currentTimeMillis() >= actionTime)
            mmc.setBackground(getDrawable(R.drawable.secondary_button));
        else if(mmcOnGoing)
            mmc.setBackground(getDrawable(R.drawable.theme_button));
        else
            mmc.setBackground(getDrawable(R.drawable.go_button));

        //if season has ended, stop counting points for all members
        final SharedPreferences.Editor editor = getSharedPreferences("Code_Challenge", MODE_PRIVATE).edit();
        if(System.currentTimeMillis() >= actionTime) {
            System.out.println("EXECUTED, TIMER SET OFF");
            int currentPoints = 0;
            for (int i = 1; i <= members; i++) {
                String memberName = sharedPreferences.getString("member" + i + "Name", "");
                int pointsOnSeasonEnd = sharedPreferences.getInt(memberName + "PointsOnSeasonEnd", -1);

                System.out.println(pointsOnSeasonEnd);
                //add pointsOnSeasonEnd only if not previously set and the season has ended
                if (pointsOnSeasonEnd == -1) {
                    currentPoints = sharedPreferences.getInt(memberName + "Points", 0);

                    editor.putInt(memberName + "PointsOnSeasonEnd", currentPoints);
                    System.out.println(currentPoints);
                    editor.commit();
                }
            }
        }

        createNotificationChannel();

        mmc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if season has ended but rewards are not collected
                if (mmcOnGoing && System.currentTimeMillis() >= actionTime){

                    new AlertDialog.Builder(PointsTally.this)
                            .setTitle("Claim Rewards")
                            .setMessage("The current MyMommy season has ended. Please claim its rewards before starting a new season.")
                            .setIcon(R.drawable.trophy)
                            .setPositiveButton("CLAIM REWARDS!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NotificationManager notificationManager = (NotificationManager)getSystemService(NotificationManager.class);
                                    notificationManager.cancelAll();

                                    Intent intent = new Intent(getApplicationContext(), MMCWinner.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
                //to end an ongoing season
                else if (mmcOnGoing){

                    String seasonDetailsFull = sharedPreferences.getString(mmcSeasonNumber + "Incentive", "");
                    String seasonDetails = "";
                    int startIndex = seasonDetailsFull.indexOf("General Incentive");
                    int endIndex = seasonDetailsFull.lastIndexOf("Duration");
                    if(startIndex != -1 && endIndex != -1)
                        seasonDetails = seasonDetailsFull.substring(startIndex, endIndex);

                    String duration = "Duration: ";
                    double remainingTime = actionTime - System.currentTimeMillis();
                    //if remaining time is more than one day
                    if(remainingTime >= 86400000) {
                        remainingTime = Math.ceil(remainingTime / 86400000D);
                        duration += "The season will end in " + (long)remainingTime + " days.";
                    }
                    else {
                        duration +=  " The season will end toady!";
                    }
                    final String details = seasonDetails + '\n' + duration;
                    new AlertDialog.Builder(PointsTally.this)
                            .setTitle("Ongoing Season")
                            .setMessage("A MyMommy season is currently ongoing. Note the following details:" + '\n'
                                    + '\n' + details + '\n'
                                    + '\n' + "Kindly wait until the season's end date or terminate it prematurely.")
                            .setIcon(R.drawable.warning)
                            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNeutralButton("End Prematurely", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(getApplicationContext(), "Ending Season Prematurely... Please click on the notification above to view the results", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(getApplicationContext(), NotificationBroadcastBuilder.class);
                                    intent.putExtra("season_number", mmcSeasonNumber);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                                    alarmManager.set(
                                            AlarmManager.RTC_WAKEUP,
                                            System.currentTimeMillis(),
                                            pendingIntent
                                    );

                                    //change system status
                                    //rest is handled by MMCWinner.class
                                    editor.remove("actionTimeMMC");

                                    finish();
                                }
                            })
                            .setNegativeButton("Copy details", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ClipboardManager clipboardManager =
                                            (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clipData = ClipData.newPlainText("simple text", details);
                                    assert clipboardManager != null;
                                    clipboardManager.setPrimaryClip(clipData);

                                    Toast.makeText(getApplicationContext(), "Season details were copied to your clipboard!", Toast.LENGTH_LONG).show();

                                }
                            })
                            .show();
                }
                //to start a new season
                else {
                    //root linearLayout & editText params
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

                    LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                            800, ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    editTextParams.setMargins(10,10,10,10);

                    //general incentive
                    setTextViewWithTopLine(linearLayout, "GENERAL INCENTIVE *");
                    final EditText generalIncentive = new EditText(getApplicationContext());
                    generalIncentive.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    generalIncentive.setHint("Set General Incentives...");
                    generalIncentive.setText("Winner gets a ");
                    generalIncentive.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    generalIncentive.setLayoutParams(editTextParams);
                    linearLayout.addView(generalIncentive);

                    //work incentive
                    setTextViewWithTopLine(linearLayout, "WORK INCENTIVE");
                    final CheckBox workIncentive = new CheckBox(getApplicationContext());
                    workIncentive.setText(getString(R.string.workIncentive));
                    linearLayout.addView(workIncentive);

                    //food incentive
                    setTextViewWithTopLine(linearLayout, "FOOD INCENTIVE");
                    final CheckBox foodIncentive = new CheckBox(getApplicationContext());
                    foodIncentive.setText(getString(R.string.foodIncentive)+" (please fill)");
                    final EditText foodIncentiveFood = new EditText(getApplicationContext());
                    foodIncentiveFood.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    foodIncentiveFood.setHint("Set A Food Entitlement...");
                    generalIncentive.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    foodIncentiveFood.setLayoutParams(editTextParams);
                    linearLayout.addView(foodIncentive);
                    linearLayout.addView(foodIncentiveFood);

                    //duration
                    setTextViewWithTopLine(linearLayout, "SEASON DURATION *");
                    final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                    final RadioButton duration2weeks = new RadioButton(getApplicationContext());
                    duration2weeks.setText("2 weeks");
                    radioGroup.addView(duration2weeks,0);
                    final RadioButton duration1month = new RadioButton(getApplicationContext());
                    duration1month.setText("1 month");
                    radioGroup.addView(duration1month, 1);
                    final RadioButton duration2months = new RadioButton(getApplicationContext());
                    duration2months.setText("2 months");
                    radioGroup.addView(duration2months, 2);
                    radioGroup.clearCheck();
                    linearLayout.addView(radioGroup);

                    new AlertDialog.Builder(PointsTally.this)
                            .setTitle("Start a new MyMommy Season")
                            .setMessage("Please fill out the incentive and duration details for " + mmcSeasonString)
                            .setIcon(R.drawable.trophy)
                            .setView(linearLayout)
                            .setPositiveButton("KICK OFF!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //all potential errors
                                    String generalIncentiveString = generalIncentive.getText().toString();
                                    if(generalIncentiveString.equals("")
                                            || generalIncentiveString.equalsIgnoreCase("Winner gets a "))
                                        Toast.makeText(getApplicationContext(), "General Incentive is a mandatory field", Toast.LENGTH_LONG).show();

                                    else if(foodIncentive.isChecked() && foodIncentiveFood.getText().toString().equals(""))
                                        Toast.makeText(getApplicationContext(), "Please add an appropriate food item", Toast.LENGTH_LONG).show();
                                    else if(!foodIncentive.isChecked() && !foodIncentiveFood.getText().toString().equals(""))
                                        Toast.makeText(getApplicationContext(), "Please select the food item checkbox", Toast.LENGTH_LONG).show();
                                    else if (radioGroup.getCheckedRadioButtonId()==-1)
                                        Toast.makeText(getApplicationContext(), "Duration of season is a mandatory field", Toast.LENGTH_LONG).show();

                                    else{

                                        //reset all points to zero
                                        for(int i=1; i<=members; i++){
                                            String memberName = sharedPreferences.getString("member"+i+"Name", "");
                                            editor.putInt(memberName + "Points", 0);
                                            editor.remove(memberName + "PointsOnSeasonEnd");
                                        }

                                        String myMommySeasonDetails = "Hello " + sharedPreferences.getString("FamilyName", "Family") + "s, " + '\n'
                                                + "This message is to inform you that " + mmcSeasonString +
                                                " is now underway! Please note the following incentive and duration details for this season. All the best!" + '\n' + '\n';
                                        myMommySeasonDetails += "General Incentive: " + '\n'
                                                + generalIncentiveString + '\n';

                                        if(workIncentive.isChecked())
                                            myMommySeasonDetails += "Work Incentive: " + '\n'
                                                    + getString(R.string.workIncentive) + '\n';

                                        if(foodIncentive.isChecked())
                                            myMommySeasonDetails += "Food Incentive: " + '\n'
                                                    + getString(R.string.foodIncentive) + " - "
                                                    + foodIncentiveFood.getText().toString() + '\n';

                                        RadioButton checkedButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                                        String duration = checkedButton.getText().toString();
                                        long currentTimeInMillis = System.currentTimeMillis();
                                        long actionTimeInMillis;

                                        if(duration.equals("2 weeks"))
                                            actionTimeInMillis = 1000L * 1209600L; //2 weeks in milliseconds
                                        else if (duration.equals("1 month"))
                                            actionTimeInMillis = 1000L * 2629800L; //1 month in milliseconds
                                        else actionTimeInMillis = 1000L * 5259600L; //2 months in milliseconds

                                        //for testing only
                                        //actionTimeInMillis = 1000 * 15; //season will end in 15 seconds

                                        myMommySeasonDetails += "Duration: " + duration + '\n';

                                        myMommySeasonDetails += '\n' + "LET THE GAMES BEGIN!";

                                        //change system state to season onGoing
                                        editor.putInt("seasonMMC", mmcSeasonNumber);
                                        editor.putBoolean("ongoingMMC", true);
                                        editor.putString(mmcSeasonNumber + "Incentive", myMommySeasonDetails);
                                        editor.putLong("actionTimeMMC", (currentTimeInMillis + actionTimeInMillis));
                                        editor.commit();

                                        //notification intent
                                        Intent intent = new Intent(getApplicationContext(), NotificationBroadcastBuilder.class);
                                        intent.putExtra("season_number", mmcSeasonNumber);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                                        alarmManager.set(
                                                AlarmManager.RTC_WAKEUP,
                                                currentTimeInMillis + actionTimeInMillis,
                                                pendingIntent
                                        );

                                        //share intent
                                        Intent sendIntent = new Intent();
                                        sendIntent.setAction(Intent.ACTION_SEND);
                                        sendIntent.putExtra(Intent.EXTRA_TEXT, myMommySeasonDetails);
                                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, mmcSeasonString);
                                        sendIntent.setType("text/plain");
                                        Intent shareIntent = Intent.createChooser(sendIntent, mmcSeasonString);
                                        startActivity(shareIntent);

                                        Toast.makeText(getApplicationContext(),
                                                mmcSeasonString + " is now underway! Please share the generated message with all family members.",
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }

            }
        });


    }

    private void setButton(Button button, String name){
        button.setText(name);
        button.setTextColor(Color.WHITE);
        button.setPadding(20,20,20,20);
        button.setBackground(getDrawable(R.drawable.theme_button));
        button.setClickable(false);
    }

    private void setTextViewWithTopLine(LinearLayout linearLayout, String content){
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                10);
        lineParams.setMargins(50,50,50,10);
        View line = new View(getApplicationContext());
        line.setBackground(getDrawable(R.color.colorPrimary));
        line.setLayoutParams(lineParams);

        TextView textView = new TextView(getApplicationContext());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        textView.setTextColor(getColor(R.color.colorPrimary));
        textView.setText(content);
        textView.setPadding(10,10,10,10);

        linearLayout.addView(line);
        linearLayout.addView(textView);
    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence charSequence = "NotificationChannel";
            String description = "Notification Channel for MyMommy";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel("code_challenge", charSequence, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}

