package com.example.healthx.pkgData;

import androidx.room.TypeConverter;

import com.example.healthx.pkgMisc.LocalDate;

public class Converters {
    @TypeConverter
    public static LocalDate fromTimestamp(Long value) {
        return value == null ? null : new LocalDate(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDate date) {
        return date == null ? null : date.getTime();
    }
}
