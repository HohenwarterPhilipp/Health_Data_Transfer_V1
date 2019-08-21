package com.example.health_data_transfer_v1.pkgData;

import com.example.health_data_transfer_v1.pkgMisc.LocalDate;

public class ScaleMeasurement {
    private float weight;
    private float bmi;
    private LocalDate dateOfMeasurement;

    public ScaleMeasurement(float weight, float bmi, LocalDate dateOfMeasurement) {
        this.weight=weight;
        this.bmi=bmi;
        this.dateOfMeasurement=dateOfMeasurement;
    }

    public float getWeight() {
        return weight;
    }

    public float getBmi() {
        return bmi;
    }

    public LocalDate getDateOfMeasurement() {
        return dateOfMeasurement;
    }

    @Override
    public String toString() {
        return dateOfMeasurement.toString();
    }
}
