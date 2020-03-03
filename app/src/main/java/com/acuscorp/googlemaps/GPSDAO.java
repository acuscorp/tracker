package com.acuscorp.googlemaps;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by Noé Adrian Acuna Prado on 28/02/2020.
 * Sistema BEA
 * acuscorp@gmail.com
 */
@Dao
public interface GPSDAO {
    @Insert
    void insert(GPS gps);
    @Update
    void update(GPS gps);
    @Delete
    void delete(GPS gps);

    @Query("DELETE FROM gps_table")
    void deleteAllGPSData();

    @Query("SELECT * FROM gps_table  ORDER BY id ")
    LiveData<List<GPS>> getAllGPSData();



}
