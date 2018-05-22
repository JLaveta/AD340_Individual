package com.example.lavet.assignment.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

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
}
