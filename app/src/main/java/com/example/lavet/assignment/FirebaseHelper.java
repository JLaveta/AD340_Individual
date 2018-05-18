package com.example.lavet.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lavet.assignment.models.Matches;
import com.example.lavet.assignment.viewmodels.FirebaseViewModel;

import java.util.ArrayList;

public class FirebaseHelper extends AppCompatActivity {

    private FirebaseViewModel viewModel;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_helper);
        viewModel = new FirebaseViewModel();

        textView = findViewById(R.id.textViewFirebase);

        ArrayList<Matches> matchList = new ArrayList<>();
        ArrayList<Matches> finalMatchList = new ArrayList<>();
        viewModel.getMatches((response) -> textView.setText(response.get(1).name));


    }

    @Override
    protected void onPause() {
        viewModel.clear();
        super.onPause();
    }
}
