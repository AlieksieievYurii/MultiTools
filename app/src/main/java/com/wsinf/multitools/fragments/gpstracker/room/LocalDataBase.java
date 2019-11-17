package com.wsinf.multitools.fragments.gpstracker.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Point.class}, version = 2, exportSchema = false)
public abstract class LocalDataBase extends RoomDatabase {
    public abstract PointDao pointDao();


}
