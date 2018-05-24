package com.example.lavet.assignment;

import android.arch.persistence.room.Room;
import android.content.Context;

public class Database {

    private static AppDatabase db;

    public static AppDatabase getDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "appy-database").build();
        }

        return db;
    }
}
