package com.example.lavet.assignment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.autofill.CharSequenceTransformation;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lavet.assignment.entity.Settings;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Provides UI for the view with List.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    public TimePicker reminderTime;
    public Spinner distanceSpinner;
    public Spinner userGender;
    public RadioGroup privacySet;
    public RangeSeekBar ageSeekBar;
    public Button submitButton;
    public View setView;
    public ArrayAdapter distAdapter;
    public ArrayAdapter genAdapter;
    Settings settings = new Settings();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setView = inflater.inflate(R.layout.activity_settings_fragment, null);
        submitButton = setView.findViewById(R.id.submitButton);

        //Setup age range seekbar
        ageSeekBar = setView.findViewById(R.id.ageSeekBar);
        ageSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Toast.makeText(getContext(), minValue + "-" + maxValue, Toast.LENGTH_LONG).show();
            }

        });
        ageSeekBar.setNotifyWhileDragging(true);

        //Setup reminder time picker
        reminderTime = setView.findViewById(R.id.reminderTime);
        reminderTime.setIs24HourView(false);

        //Setup distance spinner
        distanceSpinner = setView.findViewById(R.id.maxDistance);
        distAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.search_ranges, android.R.layout.simple_spinner_item);
        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(distAdapter);

        //Setup gender spinner
        userGender = setView.findViewById(R.id.gender);
        genAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userGender.setAdapter(genAdapter);

        //Setup Privacy Radio buttons
        privacySet = setView.findViewById(R.id.privacyGroup);
        privacySet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.privateButton:
                        //Stuff
                        break;
                    case R.id.publicButton:
                        //Other stuff
                        break;
                }
            }
        });

        submitButton.setOnClickListener(this);

        new GetSettingsTask(this, settings).execute();

        return setView;
    }

    @Override
    public void onClick(View v){

        settings.setRemTimeHour(reminderTime.getHour());
        settings.setRemTimeMin(reminderTime.getMinute());
        settings.setMaxAge((Integer) ageSeekBar.getSelectedMaxValue());
        settings.setMinAge((Integer) ageSeekBar.getSelectedMinValue());
        settings.setMiles(distanceSpinner.getSelectedItem().toString());
        settings.setPrivacy(setView.findViewById(privacySet.getCheckedRadioButtonId()).toString());
        settings.setGender(userGender.getSelectedItem().toString());

        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;
        String text = getString(R.string.setSubmit);

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        new UpdateSettingsTask(this, settings).execute();
    }

    private class GetSettingsTask extends AsyncTask<Void, Void, Settings> {

        private WeakReference<Fragment> weakFragment;
        private Settings settings;

        public GetSettingsTask(Fragment fragment, Settings settings) {
            weakFragment = new WeakReference<>(fragment);
            this.settings = settings;
        }

        @Override
        protected Settings doInBackground(Void... voids) {
            Fragment fragment = weakFragment.get();
            if(fragment == null) {
                return null;
            }

            AppDatabase db = AppDatabase.getDatabase(fragment.getActivity().getApplicationContext());

            List<Settings> settings = db.settingsDao().getAll();

            if(settings.size() <= 0 || settings.get(0) == null) {
                return null;
            }
            return settings.get(0);
        }

        @Override
        protected void onPostExecute(Settings settings) {
            Fragment fragment = weakFragment.get();
            if(settings == null || fragment == null) {
                return;
            }

            //Todo: Set all settings fields based on database
            reminderTime.setHour(settings.getRemTimeHour());
            reminderTime.setMinute(settings.getRemTimeMin());
            distanceSpinner.setSelection(distAdapter.getPosition(settings.getMiles()));
            userGender.setSelection(genAdapter.getPosition(settings.getGender()));
            if(settings.getPrivacy().equals(getString(R.string.privateButton))){
                privacySet.check(R.id.privateButton);
            } else{
                privacySet.check(R.id.publicButton);
            }
            ageSeekBar.setSelectedMinValue(settings.getMinAge());
            ageSeekBar.setSelectedMaxValue(settings.getMaxAge());
            //reminderTime.setText(settings.getRemTime());
            //userGender.setText(settings.getGender());
            //privacySet.setText(settings.getPrivacy());
            //minAge.setText(String.valueOf(settings.getMinAge()));
            //maxAge.setText(String.valueOf(settings.getMaxAge()));
        }
    }

    private class UpdateSettingsTask extends AsyncTask<Void, Void, Settings> {

        private WeakReference<Fragment> weakFragment;
        private Settings settings;

        public UpdateSettingsTask(Fragment fragment, Settings settings) {
            weakFragment = new WeakReference<>(fragment);
            this.settings = settings;
        }

        @Override
        protected Settings doInBackground(Void... voids) {
            Fragment fragment = weakFragment.get();
            if(fragment == null) {
                return null;
            }

            AppDatabase db = AppDatabase.getDatabase(fragment.getContext());

            db.settingsDao().insertAll(settings);
            return settings;
        }

        @Override
        protected void onPostExecute(Settings settings) {
            Fragment fragment = weakFragment.get();
            if(settings == null || fragment == null) {
                return;
            }

            //Todo: Set all fields based on values in database
            reminderTime.setHour(settings.getRemTimeHour());
            reminderTime.setMinute(settings.getRemTimeMin());
            distanceSpinner.setSelection(distAdapter.getPosition(settings.getMiles()));
            userGender.setSelection(genAdapter.getPosition(settings.getGender()));
            if(settings.getPrivacy().equals(getString(R.string.privateButton))){
                privacySet.check(R.id.privateButton);
            } else{
                privacySet.check(R.id.publicButton);
            }
            ageSeekBar.setSelectedMinValue(settings.getMinAge());
            ageSeekBar.setSelectedMaxValue(settings.getMaxAge());
            //reminderTime.setText(settings.getRemTime());
            //maxDistance.setText(String.valueOf(settings.getMiles()));
            //userGender.setText(settings.getGender());
            //privacySet.setText(settings.getPrivacy());
            //minAge.setText(String.valueOf(settings.getMinAge()));
            //maxAge.setText(String.valueOf(settings.getMaxAge()));
        }
    }

}