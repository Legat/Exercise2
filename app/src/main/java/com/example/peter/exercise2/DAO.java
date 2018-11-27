package com.example.peter.exercise2;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface DAO {

    @Query("SELECT * FROM news")
    List<NewsEntity> getAll();

    @Query("SELECT * FROM news WHERE category =:category")
    List<NewsEntity> getByFilter(String category);

    @Query("SELECT * FROM news WHERE id = :id")
    NewsEntity getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<NewsEntity> news);

    @Update
    int update(NewsEntity newsEntity);

    @Delete
    void delete(List<NewsEntity> news);

    @Delete
    void deleteItem(NewsEntity news);

    @Query("DELETE FROM news")
    void deleteAll();
}
