package com.example.lavet.assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        assert b!= null;
        setTextView(Constants.KEY_NAME, R.id.textViewName, b);
        setTextView(Constants.KEY_AGE, R.id.textViewAge, b);
        setTextView(Constants.KEY_OCCU, R.id.textViewOccu, b);
        setTextView(Constants.KEY_DESC, R.id.textViewDesc, b);

    }

    public void setTextView(String key, int textViewId, Bundle b){
        if(b.containsKey(key)){
            String putText = b.getString(key);
            TextView textView = findViewById(textViewId);
            textView.setText(putText);
        }
    }

    public void goToFormActivity(View view){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
