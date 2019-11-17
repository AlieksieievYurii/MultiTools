package com.wsinf.multitools.fragments.gpstracker.room;

import androidx.room.TypeConverter;

import java.util.Date;
class Converters {

    private Converters() {}

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
