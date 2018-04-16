package com.example.lavet.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.headshotimg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.about_john) {
            textView.setText(R.string.about_john);
            int imgRes = getResources().getIdentifier("@drawable/hs", null, this.getPackageName());
            imageView.setImageResource(imgRes);
            return true;
        }

        if(item.getItemId() == R.id.home) {
            textView.setText(R.string.greeting);
            imageView.setImageResource(0);
            return true;
        }

        if(item.getItemId() == R.id.register) {
            goToRegistration();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToRegistration() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
