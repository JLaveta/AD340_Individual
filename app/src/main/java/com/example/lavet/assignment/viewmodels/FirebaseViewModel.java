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

    public void addMatches(Matches item) {
        matchModel.addMatches(item);
    }

    public void getMatches(Consumer<ArrayList<Matches>> responseCallback) {
        matchModel.getMatches(
                (DataSnapshot dataSnapshot) -> {
                    ArrayList<Matches> matchList = new ArrayList<>();
                    for (DataSnapshot matchesSnapshot : dataSnapshot.getChildren()) {
                        Matches item = matchesSnapshot.getValue(Matches.class);
                        assert item != null;
                        item.uid = matchesSnapshot.getKey();
                        matchList.add(item);
                    }
                    responseCallback.accept(matchList);
                },
                (databaseError -> System.out.println("Error reading Todo Items: " + databaseError))
        );
    }

    public void updateMatches(Matches item) {
        matchModel.updateMatchById(item);
    }

    public void clear() {
        matchModel.clear();
    }
}