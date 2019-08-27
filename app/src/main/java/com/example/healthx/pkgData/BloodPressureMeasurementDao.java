package com.example.healthx.pkgData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface  BloodPressureMeasurementDao {
    @Query("SELECT * FROM BloodPressureMeasurement")
    List<BloodPressureMeasurement> getAll();

    @Insert
    void insertBloodPressureMeasurements(BloodPressureMeasurement... bloodPressureMeasurements);

    @Delete
    void deleteBloodPressureMeasurements(BloodPressureMeasurement... bloodPressureMeasurements);

}
