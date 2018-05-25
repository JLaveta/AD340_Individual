package com.example.lavet.assignment;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.lavet.assignment.dao.SettingsDao;
import com.example.lavet.assignment.entity.Settings;

@Database(entities = {Settings.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SettingsDao settingsDao();
    private static AppDatabase db;

    public static AppDatabase getDatabase(Context context) {
        if(db == null) {
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "settings")
                    .build();
        }

        return db;
    }
}
