package com.example.lavet.assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        assert b!= null;
        if(b.containsKey(Constants.KEY_USER)){
            String username = b.getString(Constants.KEY_USER);
            String confirmMsg = getString(R.string.confirm, username);
            textView.setText(confirmMsg);
        }
    }

    public void goToFormActivity(View view){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
