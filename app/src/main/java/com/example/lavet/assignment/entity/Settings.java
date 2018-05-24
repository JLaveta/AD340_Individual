package com.example.lavet.assignment.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Settings {

    @PrimaryKey
    private int sid;

    @ColumnInfo(name="reminder_time")
    private String remTime;

    @ColumnInfo(name="search_distance")
    private int miles;

    @ColumnInfo(name="gender")
    private String gender;

    @ColumnInfo(name="privacy_set")
    private String privacy;

    @ColumnInfo(name="min_age")
    private int minAge;

    @ColumnInfo(name="max_age")
    private int maxAge;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getRemTime() {
        return remTime;
    }

    public void setRemTime(String remTime) {
        this.remTime = remTime;
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
