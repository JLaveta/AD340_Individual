package com.example.lavet.assignment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private int year;
    private int day;
    private int month;
    private int age;
    private String ageS;
    private EditText editTextName;
    private EditText editTextUser;
    private EditText editTextDesc;
    private EditText editTextOccupation;
    private TextView textViewBirth;
    //private TextView textViewAge;
    private Button regButton;
    /*private TextView userName;
    private ImageView userPhoto;*/
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    //Firebase instance variables
    /*private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUser = findViewById(R.id.username);
        textViewBirth = findViewById(R.id.dob);
        regButton = findViewById(R.id.registrationButton);
        editTextDesc = findViewById(R.id.desc);
        editTextOccupation = findViewById(R.id.occupation);
        editTextName = findViewById(R.id.name);

        //Initializae Firebase Auth - commented out for now because testing has not been covered
        /*
        userName = findViewById(R.id.userName);
        userPhoto = findViewById(R.id.userPhoto);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null){
            //Not signed in, launch the sign in activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            userName.setText(mFirebaseUser.getDisplayName());
            if (mFirebaseUser.getPhotoUrl() != null){
                Picasso.get().load(mFirebaseUser.getPhotoUrl().toString()).into(userPhoto);
            }
        }*/

        textViewBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
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

                //Calculate user's age based on input DOB
                Calendar dob = Calendar.getInstance();
                dob.set(year, month, dayOfMonth);
                age = Calendar.getInstance().get(Calendar.YEAR) - year;
                if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }

                //Convert calculated age to a string format
                Integer ageInt = age;
                ageS = ageInt.toString();

                //Changes text and enables or disables the registration button based on age
                //Users under 18 cannot register
                String ageStr = getString(R.string.age) + ageS;
                if (ageInt >= 18) {
                    textViewBirth.setText(ageStr);
                    regButton.setText(getString(R.string.register));
                    regButton.setBackground(getDrawable(R.drawable.button_active));
                    regButton.setTypeface(null, Typeface.BOLD);
                    regButton.setEnabled(true);
                } else if (ageInt < 18) {
                    textViewBirth.setText(ageStr);
                    regButton.setText(getString(R.string.young));
                    regButton.setBackgroundColor(getColor(R.color.common_google_signin_btn_text_light_disabled));
                    regButton.setTypeface(null, Typeface.ITALIC);
                    regButton.setEnabled(false);
                }
            }
        };
    }

    public void goToRegisterActivity(View view){
        Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
        intent.putExtra(Constants.KEY_NAME, editTextName.getText().toString());
        intent.putExtra(Constants.KEY_AGE, ageS);
        intent.putExtra(Constants.KEY_DESC, editTextDesc.getText().toString());
        intent.putExtra(Constants.KEY_OCCU, editTextOccupation.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(Constants.KEY_AGE)) {
            ageS = ((String)savedInstanceState.get(Constants.KEY_AGE));
            String ageStr = getString(R.string.age) + ageS;
            textViewBirth.setText(ageStr);
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

        outState.putString(Constants.KEY_AGE, ageS);
        outState.putString(Constants.KEY_REG, regButton.getText().toString());
        outState.putBoolean(Constants.KEY_REG_STATE, regButton.isEnabled());
    }
}