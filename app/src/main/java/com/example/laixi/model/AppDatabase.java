package com.example.laixi.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ProgressBean.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProgressBeanDao progressBeanDao();
}
    