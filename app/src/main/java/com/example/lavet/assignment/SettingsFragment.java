package com.example.lavet.assignment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lavet.assignment.entity.Settings;

/**
 * Provides UI for the view with List.
 */
public class SettingsFragment extends Fragment {

    private EditText reminderTime;
    private EditText maxDistance;
    private EditText userGender;
    private EditText privacySet;
    private EditText minAge;
    private EditText maxAge;
    private Button submitButton;
    Settings settings = new Settings();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View setView = inflater.inflate(R.layout.activity_profile_fragment, null);
        reminderTime = setView.findViewById(R.id.reminderTime);
        maxDistance = setView.findViewById(R.id.maxDistance);
        userGender = setView.findViewById(R.id.gender);
        privacySet = setView.findViewById(R.id.privacy);
        minAge = setView.findViewById(R.id.minAge);
        maxAge = setView.findViewById(R.id.maxAge);
        submitButton = setView.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                settings.setGender(userGender.getText().toString());
                settings.setMaxAge(Integer.parseInt(maxAge.getText().toString()));
                settings.setMinAge(Integer.parseInt(minAge.getText().toString()));
                settings.setMiles(Integer.parseInt(maxDistance.getText().toString()));
                settings.setRemTime(reminderTime.getText().toString());
                settings.setPrivacy(privacySet.getText().toString());

                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;
                String text = getString(R.string.setSubmit);

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        reminderTime.setText(settings.getRemTime());
        maxDistance.setText(settings.getMiles());
        userGender.setText(settings.getGender());
        privacySet.setText(settings.getPrivacy());
        minAge.setText(settings.getMinAge());
        maxAge.setText(settings.getMaxAge());

        return setView;
    }


}