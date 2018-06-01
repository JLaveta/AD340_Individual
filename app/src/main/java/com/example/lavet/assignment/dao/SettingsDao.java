package com.example.lavet.assignment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.lavet.assignment.entity.SettingsEntity;

import java.util.List;

@Dao
public interface SettingsDao {

    @Query("SELECT * from SettingsEntity")
    List<SettingsEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SettingsEntity... settings);
}
