package com.wsinf.multitools.fragments.gpstracker.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@TypeConverters({Converters.class})
@Entity(tableName = "t_points")
public class Point {
    @PrimaryKey(autoGenerate = true)
    private int pid;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    private Date timeStamp;

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeStamp = new Date();
    }


    public double getLatitude() {
        return latitude;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }
}
