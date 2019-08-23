package com.example.health_data_transfer_v1.pkgData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScaleMeasurementDao {
    @Query("SELECT * FROM ScaleMeasurement")
    List<ScaleMeasurement> getAll();

    @Insert
    void insertScaleMeasurements(ScaleMeasurement... scaleMeasurementEntities);

    @Delete
    void deleteScaleMeasurements(ScaleMeasurement... scaleMeasurementEntities);
    
}
