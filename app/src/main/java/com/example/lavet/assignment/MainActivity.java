package com.example.lavet.assignment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private int year;
    private int day;
    private int month;
    private int dayOfYear;
    private int age;
    private String ageS;
    private EditText editTextName;
    private EditText editTextUser;
    private EditText editTextDesc;
    private EditText editTextOccupation;
    private TextView textViewBirth;
    private TextView textViewAge;
    private Button regButton;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUser = findViewById(R.id.username);
        textViewBirth = findViewById(R.id.dob);
        textViewAge = findViewById(R.id.age);
        regButton = findViewById(R.id.registrationButton);
        editTextDesc = findViewById(R.id.desc);
        editTextOccupation = findViewById(R.id.occupation);
        editTextName = findViewById(R.id.name);

        textViewBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                DatePickerDialog datePick = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar,
                        onDateSetListener,
                        year, month, day);
                Objects.requireNonNull(datePick.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePick.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Print out date of birth in placeholder
                String date = month + 1 + "/" + dayOfMonth + "/" + year;
                textViewBirth.setText(date);

                //Calculate user's age based on input DOB
                age = Calendar.getInstance().get(Calendar.YEAR) - year;
                if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) < dayOfYear) {
                    age--;
                }

                //Convert calculated age to a string format
                Integer ageInt = age;
                ageS = ageInt.toString();

                //Changes text and enables or disables the registration button based on age
                //Users under 18 cannot register
                String ageStr = getString(R.string.age) + ageS;
                if (ageInt >= 18) {
                    textViewAge.setText(ageStr);
                    regButton.setText(getString(R.string.register));
                    regButton.setEnabled(true);
                } else if (ageInt < 18) {
                    textViewAge.setText(ageStr);
                    regButton.setText(getString(R.string.young));
                    regButton.setEnabled(false);
                }
            }
        };
    }

    public void goToRegisterActivity(View view){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        intent.putExtra(Constants.KEY_NAME, editTextName.getText().toString());
        intent.putExtra(Constants.KEY_AGE, ageS);
        intent.putExtra(Constants.KEY_DESC, editTextDesc.getText().toString());
        intent.putExtra(Constants.KEY_OCCU, editTextOccupation.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(Constants.KEY_DOB)) {
            textViewBirth.setText((String)savedInstanceState.get(Constants.KEY_DOB));
        }

        if (savedInstanceState.containsKey(Constants.KEY_AGE)) {
            textViewAge.setText((String)savedInstanceState.get(Constants.KEY_AGE));
        }

        if (savedInstanceState.containsKey(Constants.KEY_REG)) {
            regButton.setText((String)savedInstanceState.get(Constants.KEY_REG));
        }

        if(savedInstanceState.containsKey(Constants.KEY_REG_STATE)){
            regButton.setEnabled((Boolean)savedInstanceState.get(Constants.KEY_REG_STATE));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(Constants.KEY_DOB, textViewBirth.getText().toString());
        outState.putString(Constants.KEY_AGE, textViewAge.getText().toString());
        outState.putString(Constants.KEY_REG, regButton.getText().toString());
        outState.putBoolean(Constants.KEY_REG_STATE, regButton.isEnabled());
    }
}