package com.example.health_data_transfer_v1.pkgMisc;

import java.util.Calendar;
import java.util.Date;

public class LocalDate extends Date {
    Calendar calendar;

    public LocalDate(long currentTimeMillis){
        calendar=Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        this.setTime(calendar.getTimeInMillis());
    }

    public int getDayOfLocalDate(){
        return (calendar.get(Calendar.DAY_OF_MONTH));
    }

    public int getMonthOfLocalDate(){
        return (calendar.get(Calendar.MONTH));
    }

    public int getYearOfLocalDate(){
        return (calendar.get(Calendar.YEAR));
    }

    public String getLocalDateAsString(){
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1);
    }

    public String getHoursOfCurrentDayAsString(){
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + (calendar.get(Calendar.MINUTE));
    }

    @Override
    public String toString() {
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }
}
