package com.example.healthx.pkgData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.healthx.pkgMisc.LocalDate;

@Entity(tableName = "ScaleMeasurement")
public class ScaleMeasurement {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "weight")
    private float weight;
    @ColumnInfo(name = "bmi")
    private float bmi;
    @ColumnInfo(name = "dateOfMeasurement")
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
