package com.example.lavet.assignment.viewmodels;

import com.example.lavet.assignment.datamodels.FirebaseModel;
import com.example.lavet.assignment.models.Matches;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FirebaseViewModel {

    private FirebaseModel matchModel;

    public FirebaseViewModel() {
        matchModel = new FirebaseModel();
    }

    /*public void addMatches(Matches match) {
        matchModel.addMatches(match);
    }*/

    public void getMatches(Consumer<ArrayList<Matches>> responseCallback) {
        matchModel.getMatches(
                (DataSnapshot dataSnapshot) -> {
                    ArrayList<Matches> matchList = new ArrayList<>();
                    for (DataSnapshot matchesSnapshot : dataSnapshot.getChildren()) {
                        Matches match = matchesSnapshot.getValue(Matches.class);
                        assert match != null;
                        match.uid = matchesSnapshot.getKey();
                        matchList.add(match);
                    }
                    responseCallback.accept(matchList);
                },
                (databaseError -> System.out.println("Error reading Todo Items: " + databaseError))
        );
    }

    public void updateMatches(Matches match) {
        matchModel.updateMatchById(match);
    }

    public void clear() {
        matchModel.clear();
    }
}