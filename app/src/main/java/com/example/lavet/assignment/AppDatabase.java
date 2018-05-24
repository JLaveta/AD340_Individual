package com.example.lavet.assignment;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.lavet.assignment.dao.SettingsDao;
import com.example.lavet.assignment.entity.Settings;

@Database(entities = {Settings.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SettingsDao settingsDao();
}
