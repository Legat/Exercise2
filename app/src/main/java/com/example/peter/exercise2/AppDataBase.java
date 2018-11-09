package com.example.peter.exercise2;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {NewsEntity.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract DAO NewsDao();
}
