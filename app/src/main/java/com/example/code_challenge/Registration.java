package com.example.code_challenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class Registration extends AppCompatActivity {

    RelativeLayout relativeLayout; //bigger view
    LinearLayout linearLayout; //contained view
    EditText familyName;
    Spinner familyMembersSpinner;
    TextView familyNamePrompt;
    int familyMembers;

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        relativeLayout = findViewById(R.id.registrationView);
        linearLayout = findViewById(R.id.registrationViewLL);
        familyName = findViewById(R.id.registrationFamilyName);
        familyNamePrompt = findViewById(R.id.registrationFamilyNamePrompt);

        familyMembersSpinner = findViewById(R.id.registrationSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.family_members, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        familyMembersSpinner.setAdapter(arrayAdapter);

        familyMembersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String familyMembersString = (String) parent.getItemAtPosition(position);

                if(familyMembersString.equals("--")) {
                    familyNamePrompt.setVisibility(View.INVISIBLE);

                    //1 is same id, maximum ids = 1-4 (4 family members)
                    for(int i=1; i<=4; i++) {
                        EditText editText = findViewById(i);
                        if(editText!=null) linearLayout.removeView(editText);
                    }
                    familyMembers = 0;
                    onNothingSelected(parent);
                }
                else {
                    familyMembers = Integer.parseInt(familyMembersString);
                    familyNamePrompt.setVisibility(View.VISIBLE);

                    setTextView(familyMembers);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),
                        "Please select a number of family members", Toast.LENGTH_LONG).show();
            }
        });

        Button registrationSave = findViewById(R.id.registrationSave);
        registrationSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Code_Challenge", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String familyNameString = familyName.getText().toString();
                if(familyNameString.equals("") || familyNameString.length()>10)
                    Toast.makeText(getApplicationContext(), "Please enter a valid family name (no blanks, no &, 1-10)", Toast.LENGTH_LONG).show();
                else {
                    //flag to check if loop was run or not (loop won't run if selection = '--')
                    boolean flag = false;
                    for (i = 1; i <= familyMembers; i++) {
                        flag = true;
                        EditText editText = findViewById(i);
                        String s = editText.getText().toString();
                        if (s.equals("")||s.length()>10)
                            Toast.makeText(getApplicationContext(), "Please enter valid nicknames (no blanks, no &, 1-10)", Toast.LENGTH_LONG).show();
                        else {
                            editor.putInt("members", familyMembers);
                            editor.putString("FamilyName", familyNameString);
                            editor.putString("member" + i + "Name", s);
                            editor.putBoolean("isRegistered", true);
                            editor.putBoolean("profilePicture", false);
                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("firstTimeLogin", true);
                            startActivity(intent);
                        }
                    }
                    if (!flag)
                        Toast.makeText(getApplicationContext(), "Please select a number of family members", Toast.LENGTH_LONG).show();
                }


            }
        });

        Button cancel = findViewById(R.id.registrationCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "We hope to see you back soon!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void setTextView(int max){
        //clear previous views
        for(; i>=1; i--) {
            EditText editText = findViewById(i);
            if(editText!=null) linearLayout.removeView(editText);
        }

        for(i=1; i<=max; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    700, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(40, 20, 40, 20);
            EditText editText = new EditText(getApplicationContext());
            editText.setId(i);
            editText.setTextSize(20);
            editText.setTextColor(getColor(R.color.colorPrimary));
            editText.setHint("Enter Name " + i);
            editText.setLayoutParams(layoutParams);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            linearLayout.addView(editText);
        }
    }

}
