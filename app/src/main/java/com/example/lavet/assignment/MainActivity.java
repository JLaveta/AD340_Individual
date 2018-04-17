package com.example.lavet.assignment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
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
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
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
                    String date = month+1 + "/" + dayOfMonth + "/" + year;
                    textView.setText(date);
                }
            };
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
