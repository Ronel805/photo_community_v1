package com.example.photo_community;


import androidx.room.TypeConverter;

import java.util.Date;

class Converters {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null? null : new Date(value);
    }
    @TypeConverter
    public Long dateToTimestamp(Date date){
        if(date == null)
            return null;
        else
            return date.getTime();
    }
}
