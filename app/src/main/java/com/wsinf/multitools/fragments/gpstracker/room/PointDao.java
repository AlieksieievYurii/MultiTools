package com.wsinf.multitools.fragments.gpstracker.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PointDao
{
    @Query("SELECT * FROM t_points")
    List<Point> getAllPoints();

    @Query("SELECT * FROM t_points WHERE timeStamp BETWEEN :timeStamp_start and :timeStamp_end")
    List<Point> getAllPointsByTimeStamp(long timeStamp_start, long timeStamp_end);

    @Insert
    void insert(final Point point);

    @Delete
    void delete(final Point point);

    @Query("DELETE FROM t_points")
    void deleteAll();
}
