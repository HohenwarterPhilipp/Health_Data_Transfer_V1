package com.example.health_data_transfer_v1.pkgData;

import com.example.health_data_transfer_v1.pkgMisc.LocalDate;

import java.util.Date;

public class BloodPressureMeasurement {
    private int diastolic;
    private int systolic;
    private int heartRate;
    private LocalDate dateOfMeasurement;

    public BloodPressureMeasurement(int diastolic, int systolic, int heartRate, LocalDate dateOfMeasurement) {
        this.diastolic = diastolic;
        this.systolic=systolic;
        this.heartRate=heartRate;
        this.dateOfMeasurement=dateOfMeasurement;
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

    public Date getDateOfMeasurement() {
        return dateOfMeasurement;
    }

    @Override
    public String toString() {
        return dateOfMeasurement.toString();
    }
}
