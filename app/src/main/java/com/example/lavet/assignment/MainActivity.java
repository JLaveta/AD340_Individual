package com.example.lavet.assignment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private int year;
    private int day;
    private int month;
    private int dayOfYear;
    private ImageView imageView;
    private EditText editText;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.about_john) {
            setContentView(R.layout.about);
            imageView = findViewById(R.id.headshotimg);
            int imgRes = getResources().getIdentifier("@drawable/hs", null, this.getPackageName());
            imageView.setImageResource(imgRes);
            return true;
        }

        if(item.getItemId() == R.id.home) {
            setContentView(R.layout.activity_main);
            return true;
        }

        if(item.getItemId() == R.id.register) {
            setContentView(R.layout.form);
            textView = findViewById(R.id.dob);
            editText = findViewById(R.id.username);

            textView.setOnClickListener(new View.OnClickListener() {
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
                    datePick.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePick.show();

                }
            });

            onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //Print out date of birth in placeholder
                    String date = month+1 + "/" + dayOfMonth + "/" + year;
                    textView.setText(date);

                    //Calculate user's age based on input DOB
                    int age = Calendar.getInstance().get(Calendar.YEAR) - year;
                    if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) < dayOfYear){
                        age--;
                    }

                    //Convert calculated age to a string format
                    Integer ageInt = new Integer(age);
                    String ageS = ageInt.toString();

                    //Changes text and enables or disables the registration button based on age
                    //Users under 18 cannot register
                    TextView ageText = findViewById(R.id.age);
                    Button regButton = findViewById(R.id.registrationButton);

                    if(ageInt >= 18){
                        ageText.setText(getString(R.string.age) + ageS);
                        findViewById(R.id.registrationButton).setEnabled(true);
                    }

                    else if(ageInt < 18){
                        ageText.setText(getString(R.string.age) + ageS);
                        regButton.setText(getString(R.string.young));
                        regButton.setEnabled(false);
                    }
                }
            };
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToRegisterActivity(View view){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        intent.putExtra(Constants.KEY_USER, editText.getText().toString());
        startActivity(intent);
    }
}