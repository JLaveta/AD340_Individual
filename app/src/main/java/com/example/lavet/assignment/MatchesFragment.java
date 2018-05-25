package com.example.lavet.assignment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lavet.assignment.models.Matches;
import com.example.lavet.assignment.viewmodels.FirebaseViewModel;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.getColor;


/**
 * Provides UI for the view with List.
 */
public class MatchesFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.activity_profile_fragment, null);

        TextView textViewNameAge = fragView.findViewById(R.id.textViewNameAge);


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