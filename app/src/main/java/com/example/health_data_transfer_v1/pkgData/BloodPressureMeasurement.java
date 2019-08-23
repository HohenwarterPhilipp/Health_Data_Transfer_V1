package com.example.health_data_transfer_v1.pkgData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.health_data_transfer_v1.pkgMisc.LocalDate;

@Entity(tableName = "BloodPressureMeasurement")
public class BloodPressureMeasurement {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "diastolic")
    private int diastolic;

    @ColumnInfo(name = "systolic")
    private int systolic;

    @ColumnInfo(name = "heartRate")
    private int heartRate;

    @ColumnInfo(name = "dateOfMeasurement")
    private LocalDate dateOfMeasurement;


    public BloodPressureMeasurement(int diastolic, int systolic, int heartRate, LocalDate dateOfMeasurement) {
        this.diastolic = diastolic;
        this.systolic = systolic;
        this.heartRate = heartRate;
        this.dateOfMeasurement = dateOfMeasurement;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public int getSystolic() {
        return systolic;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public LocalDate getDateOfMeasurement() {
        return dateOfMeasurement;
    }

    @Override
    public String toString() {
        return dateOfMeasurement.toString();
    }

}
