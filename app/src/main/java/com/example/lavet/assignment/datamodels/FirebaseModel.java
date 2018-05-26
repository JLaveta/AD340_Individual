package com.example.lavet.assignment.datamodels;

import com.example.lavet.assignment.models.Matches;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.function.Consumer;

public class FirebaseModel {

    private DatabaseReference mDatabase;
    private HashMap<DatabaseReference, ValueEventListener> listeners;

    public FirebaseModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listeners = new HashMap<>();
    }

    /*public void addMatches(Matches match) {
        DatabaseReference matchesRef = mDatabase.child("matches");
        matchesRef.push().setValue(match);
    }*/

    public void getMatches(Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {
        DatabaseReference matchesRef = mDatabase.child("matches");
        ValueEventListener matchesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataChangedCallback.accept(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dataErrorCallback.accept(databaseError);
            }
        };
        matchesRef.addValueEventListener(matchesListener);
        listeners.put(matchesRef, matchesListener);
    }

    public void updateMatchById(Matches match) {
        DatabaseReference matchesRef = mDatabase.child("matches");
        matchesRef.child(match.uid).setValue(match);
    }

    public void clear() {
        // Clear all the listeners onPause
        listeners.forEach(Query::removeEventListener);
    }
}
