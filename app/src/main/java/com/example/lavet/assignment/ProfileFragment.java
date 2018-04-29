package com.example.lavet.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.fragment_profile, null);

        TextView textViewNameAge = fragView.findViewById(R.id.textViewNameAge);
        Bundle b = getArguments();

        assert b!= null;
        String name = b.getString(Constants.KEY_NAME);
        String age = b.getString(Constants.KEY_AGE);
        textViewNameAge.setText(getResources().getString(R.string.nameAge, name, age));
        setTextView(Constants.KEY_OCCU, R.id.textViewOccu, b, fragView);
        setTextView(Constants.KEY_DESC, R.id.textViewDesc, b, fragView);

        return fragView;

    }

    public void setTextView(String key, int textViewId, Bundle b, View fragView){
        if(b.containsKey(key)){
            String putText = b.getString(key);
            TextView textView = fragView.findViewById(textViewId);
            textView.setText(putText);
        }
    }
}
