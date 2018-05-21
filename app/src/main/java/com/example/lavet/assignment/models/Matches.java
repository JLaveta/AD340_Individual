package com.example.lavet.assignment.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Matches implements Parcelable {

    public String imageUrl;
    public String uid;
    public String name;
    public boolean liked;

    public Matches() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Matches(String uid, boolean liked, String name) {
        this.uid = uid;
        this.liked = liked;
        this.name = name;
    }

    public Matches(Parcel in) {
        imageUrl = in.readString();
        liked = in.readByte() != 0;
        name = in.readString();
    }

    public static final Creator<Matches> CREATOR = new Creator<Matches>() {
        @Override
        public Matches createFromParcel(Parcel in) {
            return new Matches(in);
        }

        @Override
        public Matches[] newArray(int size) {
            return new Matches[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("liked", liked);
        result.put("name", name);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeByte((byte) (liked ? 1 : 0));
    }
}
