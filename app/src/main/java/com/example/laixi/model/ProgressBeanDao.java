package com.example.laixi.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProgressBeanDao {
    @Query("SELECT * FROM progressbean")
    List<ProgressBean> getAll();

    @Query("SELECT * FROM progressbean WHERE seqNum IN (:seqNums)")
    List<ProgressBean> loadAllByIds(int[] seqNums);

    @Insert
    void insertAll(ProgressBean... progressBeans);

    @Delete
    void delete(ProgressBean progressBean);

    @Update
    void update(ProgressBean progressBean);
}
    