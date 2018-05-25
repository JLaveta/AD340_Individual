package com.example.lavet.assignment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Provides UI for the view with List.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    public EditText reminderTime;
    public EditText maxDistance;
    public EditText userGender;
    public EditText privacySet;
    public EditText minAge;
    public EditText maxAge;
    public Button submitButton;
    public View setView;
    Settings settings = new Settings();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setView = inflater.inflate(R.layout.activity_settings_fragment, null);
        reminderTime = setView.findViewById(R.id.reminderTime);
        maxDistance = setView.findViewById(R.id.maxDistance);
        userGender = setView.findViewById(R.id.gender);
        privacySet = setView.findViewById(R.id.privacy);
        minAge = setView.findViewById(R.id.minAge);
        maxAge = setView.findViewById(R.id.maxAge);
        submitButton = setView.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(this);

        new GetSettingsTask(this.getActivity(), settings).execute();

        return setView;
    }

    @Override
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

        new UpdateSettingsTask(this.getActivity(), settings).execute();
    }

    private class GetSettingsTask extends AsyncTask<Void, Void, Settings> {

        private WeakReference<Activity> weakActivity;
        private Settings settings;

        public GetSettingsTask(Activity activity, Settings settings) {
            weakActivity = new WeakReference<>(activity);
            this.settings = settings;
        }

        @Override
        protected Settings doInBackground(Void... voids) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return null;
            }

            AppDatabase db = AppDatabase.getDatabase(activity.getApplicationContext());

            List<Settings> settings = db.settingsDao().getAll();

            if(settings.size() <= 0 || settings.get(0) == null) {
                return null;
            }
            return settings.get(0);
        }

        @Override
        protected void onPostExecute(Settings settings) {
            Activity activity = weakActivity.get();
            if(settings == null || activity == null) {
                return;
            }

            reminderTime.setText(settings.getRemTime());
            maxDistance.setText(settings.getMiles());
            userGender.setText(settings.getGender());
            privacySet.setText(settings.getPrivacy());
            minAge.setText(settings.getMinAge());
            maxAge.setText(settings.getMaxAge());
        }
    }

    private class UpdateSettingsTask extends AsyncTask<Void, Void, Settings> {

        private WeakReference<Activity> weakActivity;
        private Settings settings;

        public UpdateSettingsTask(Activity activity, Settings settings) {
            weakActivity = new WeakReference<>(activity);
            this.settings = settings;
        }

        @Override
        protected Settings doInBackground(Void... voids) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return null;
            }

            AppDatabase db = AppDatabase.getDatabase(activity.getApplicationContext());

            db.settingsDao().insertAll(settings);
            return settings;
        }

        @Override
        protected void onPostExecute(Settings settings) {
            Activity activity = weakActivity.get();
            if(settings == null || activity == null) {
                return;
            }

            reminderTime.setText(settings.getRemTime());
            maxDistance.setText(settings.getMiles());
            userGender.setText(settings.getGender());
            privacySet.setText(settings.getPrivacy());
            minAge.setText(settings.getMinAge());
            maxAge.setText(settings.getMaxAge());
        }
    }

}