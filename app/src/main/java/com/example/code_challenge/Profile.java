package com.example.code_challenge;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    TextView familyName;
    ImageView editName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_APPEND);

        familyName = findViewById(R.id.profileFamilyName);
        editName = findViewById(R.id.profileFamilyNameEdit);
        final String familyNameString = sharedPreferences.getString("FamilyName", "Family");
        familyName.setText(familyNameString + "s");

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editText = new EditText(getApplicationContext());
                editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                editText.setText(familyNameString);
                new AlertDialog.Builder(Profile.this)
                        .setTitle("Edit Family Name")
                        .setView(editText)
                        .setPositiveButton("RENAME", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String input = editText.getText().toString();
                                if(input.trim().equalsIgnoreCase(familyNameString))
                                    Toast.makeText(getApplicationContext(),
                                            "No changes found!", Toast.LENGTH_SHORT).show();
                                else if (input.equals("") || input.length()>10)
                                    Toast.makeText(getApplicationContext(),
                                            "Please enter a valid new family name (no blanks, 1-10)", Toast.LENGTH_LONG).show();
                                else {
                                    String newName = editText.getText().toString();
                                    //will overwrite existing name since key is same
                                    SharedPreferences sharedPreferencesEdit =
                                            getSharedPreferences("Code_Challenge", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferencesEdit.edit();

                                    NicheTasksObject.save(getApplicationContext(), newName);

                                    editor.putString("FamilyName", editText.getText().toString());
                                    editor.commit();
                                    familyName.setText(newName + "s");
                                    Toast.makeText(getApplicationContext(), "Family Name changed to "+newName, Toast.LENGTH_LONG).show();
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
        });

        LinearLayout linearLayout = findViewById(R.id.profileMembersView);
        int familyMembers = sharedPreferences.getInt("members", 0);
        for(int i=1; i<=familyMembers; i++){
            RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());

            //members' names
            RelativeLayout.LayoutParams memberParams = new RelativeLayout.LayoutParams(
                    300, RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            memberParams.setMargins(120,50,30,50);

            String memberName = sharedPreferences.getString("member"+i+"Name", "");
            TextView member = new TextView(getApplicationContext());
            member.setId(i);
            setView(member, memberName);
            member.setLayoutParams(memberParams);
            relativeLayout.addView(member);

            //myMommy cups
            RelativeLayout.LayoutParams cupParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            cupParams.setMargins(60,50,40,50);
            cupParams.addRule(RelativeLayout.RIGHT_OF, i);

            int memberCups = sharedPreferences.getInt(memberName+"MMC", 0);
            TextView cups = new TextView(getApplicationContext());
            if(memberCups<=100) setView(cups, "MM League wins: "+memberCups);
            else setView(cups, "MM League wins: " + "100+");
            cups.setLayoutParams(cupParams);
            relativeLayout.addView(cups);

            linearLayout.addView(relativeLayout);
        }

        Button quickStartGuide = findViewById(R.id.profileQuickStart);
        quickStartGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuickStart.class);
                startActivity(intent);
            }
        });

        Button logout = findViewById(R.id.profileLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Profile.this)
                        .setTitle("Confirm Logout")
                        .setMessage("Logging out of MyMommy will permanently delete all current tasks, reminders, points and profile data. Are you sure you want to continue?")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getSharedPreferences("Code_Challenge", MODE_PRIVATE).edit().clear().commit();
                                Toast.makeText(getApplicationContext(), "We're sad to see you go :(",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Registration.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),
                                        "Cancelled", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        }).show();
            }
        });

    }

    private void setView(TextView view, String content){
        view.setText(content);
        view.setTextSize(15);
        view.setTextColor(Color.WHITE);
        view.setBackground(getDrawable(R.drawable.theme_button));
        view.setPadding(50,50,50,50);
        view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    /*was code for adding profile picture, however, new Android update does not work after API 29 (Android 11)
     Therefore has been commented out, relevant xml elements are also removed.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //if (Build.VERSION.SDK_INT >= 29) {
            System.out.println("HAS EXECUTED!");
            String[] filePath = {
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.DISPLAY_NAME,
            };
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    filePath, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + "DESC");
            // You can replace '0' by 'cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)'
            // Note that now, you read the column '_ID' and not the column 'DATA'
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getInt(0));

            // now that you have the media URI, you can decode it to a bitmap
            try (ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(imageUri, "r")) {
                if (pfd != null) {
                   profilePicture.setImageBitmap(BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor()));
                }
            } catch (IOException ex) {

            }
        //}
        /*else {
            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                Uri profilePictureURI = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(profilePictureURI, filePath,
                        null, null, null);
                cursor.moveToFirst();
                String picturePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                cursor.close();

                SharedPreferences.Editor editor = getSharedPreferences("Code_Challenge", MODE_PRIVATE).edit();
                editor.putString("profilePicturePath", picturePath);
                editor.putBoolean("profilePicture", true);
                editor.commit();
                profilePicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }

    }*/
}
