package com.example.lavet.assignment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lavet.assignment.entity.SettingsEntity;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

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
    public TextView rangeText;
    public View setView;
    public ArrayAdapter distAdapter;
    public ArrayAdapter genAdapter;
    SettingsEntity settings = new SettingsEntity();
    SendData sender;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setView = inflater.inflate(R.layout.activity_settings_fragment, null);
        rangeText = setView.findViewById(R.id.rangeText);
        submitButton = setView.findViewById(R.id.submitButton);

        //Setup age range seekbar
        ageSeekBar = setView.findViewById(R.id.ageSeekBar);
        ageSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                rangeText.setText(getContext().getString(R.string.current_range, minValue, maxValue));
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
        settings.setMiles(Integer.parseInt(distanceSpinner.getSelectedItem().toString()));
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

    interface SendData {
        void sendRange(int matchRange);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try{
            sender = (SendData) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException("Error getting data, try again.");
        }
    }

    public class GetSettingsTask extends AsyncTask<Void, Void, SettingsEntity> {

        private WeakReference<Fragment> weakFragment;
        private SettingsEntity settings;

        public GetSettingsTask(Fragment fragment, SettingsEntity settings) {
            weakFragment = new WeakReference<>(fragment);
            this.settings = settings;
        }

        @Override
        protected SettingsEntity doInBackground(Void... voids) {
            Fragment fragment = weakFragment.get();
            if(fragment == null) {
                return null;
            }

            AppDatabase db = AppDatabase.getDatabase(fragment.getActivity().getApplicationContext());

            List<SettingsEntity> settings = db.settingsDao().getAll();

            if(settings.size() <= 0 || settings.get(0) == null) {
                return null;
            }
            return settings.get(0);
        }

        @Override
        protected void onPostExecute(SettingsEntity settings) {
            Fragment fragment = weakFragment.get();
            if(settings == null || fragment == null) {
                return;
            }

            reminderTime.setHour(settings.getRemTimeHour());
            reminderTime.setMinute(settings.getRemTimeMin());
            distanceSpinner.setSelection(distAdapter.getPosition(Integer.toString(settings.getMiles())));
            userGender.setSelection(genAdapter.getPosition(settings.getGender()));
            if(settings.getPrivacy().equals(getString(R.string.privateButton))){
                privacySet.check(R.id.privateButton);
            } else{
                privacySet.check(R.id.publicButton);
            }
            int minAge = settings.getMinAge();
            int maxAge = settings.getMaxAge();
            ageSeekBar.setSelectedMinValue(minAge);
            ageSeekBar.setSelectedMaxValue(maxAge);
            rangeText.setText(getContext().getString(R.string.current_range, minAge, maxAge));

            //Send to other fragment
            sender.sendRange(settings.getMiles());
        }
    }

    private class UpdateSettingsTask extends AsyncTask<Void, Void, SettingsEntity> {

        private WeakReference<Fragment> weakFragment;
        private SettingsEntity settings;

        public UpdateSettingsTask(Fragment fragment, SettingsEntity settings) {
            weakFragment = new WeakReference<>(fragment);
            this.settings = settings;
        }

        @Override
        protected SettingsEntity doInBackground(Void... voids) {
            Fragment fragment = weakFragment.get();
            if(fragment == null) {
                return null;
            }

            AppDatabase db = AppDatabase.getDatabase(fragment.getContext());

            db.settingsDao().insertAll(settings);
            return settings;
        }

        @Override
        protected void onPostExecute(SettingsEntity settings) {
            Fragment fragment = weakFragment.get();
            if(settings == null || fragment == null) {
                return;
            }

            //Todo: Set all fields based on values in database
            reminderTime.setHour(settings.getRemTimeHour());
            reminderTime.setMinute(settings.getRemTimeMin());
            distanceSpinner.setSelection(distAdapter.getPosition(Integer.toString(settings.getMiles())));
            userGender.setSelection(genAdapter.getPosition(settings.getGender()));
            if(settings.getPrivacy().equals(getString(R.string.privateButton))){
                privacySet.check(R.id.privateButton);
            } else{
                privacySet.check(R.id.publicButton);
            }
            int minAge = settings.getMinAge();
            int maxAge = settings.getMaxAge();
            ageSeekBar.setSelectedMinValue(minAge);
            ageSeekBar.setSelectedMaxValue(maxAge);
            rangeText.setText(getContext().getString(R.string.current_range, minAge, maxAge));

            //Send to other fragment
            sender.sendRange(settings.getMiles());
        }
    }

}