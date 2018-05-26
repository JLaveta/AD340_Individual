package com.example.lavet.assignment;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.lavet.assignment.models.Matches;


import java.util.List;

public class MatchesDiffCallback extends DiffUtil.Callback {

    private final List<Matches> mOldMatchList;
    private final List<Matches> mNewMatchList;

    public MatchesDiffCallback(List<Matches> oldMatchList, List<Matches> newMatchList) {
        this.mOldMatchList = oldMatchList;
        this.mNewMatchList = newMatchList;
    }

    @Override
    public int getOldListSize() {
        return mOldMatchList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewMatchList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldMatchList.get(oldItemPosition).uid == mNewMatchList.get(
                newItemPosition).uid;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Matches oldMatch = mOldMatchList.get(oldItemPosition);
        final Matches newMatch = mNewMatchList.get(newItemPosition);

        return oldMatch.name.equals(newMatch.name);
    }

    /*@Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }*/
}
