package com.example.healthx.pkgMisc;

import java.util.Calendar;
import java.util.Date;

public class LocalDate extends Date {
    Calendar calendar;

    public LocalDate(long currentTimeMillis){
        calendar=Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        this.setTime(calendar.getTimeInMillis());
    }

    public String getLocalDateAsString(){
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1);
    }

    @Override
    public String toString() {
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }
}
