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
        setContentView(R.layout.profile_frag);
        TextView textViewNameAge = findViewById(R.id.textViewNameAge);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        assert b!= null;
        String name = b.getString(Constants.KEY_NAME);
        String age = b.getString(Constants.KEY_AGE);
        textViewNameAge.setText(getResources().getString(R.string.nameAge, name, age));
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
