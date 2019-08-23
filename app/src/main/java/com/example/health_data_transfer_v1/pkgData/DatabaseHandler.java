package com.example.health_data_transfer_v1.pkgData;

import java.util.List;

public class DatabaseHandler {


    public static BloodPressureMeasurement addBloodPressureMeasurementToDB(final AppDatabase database, BloodPressureMeasurement bloodPressureMeasurement) {
        database.bloodPressureMeasurementDao().insertBloodPressureMeasurements(bloodPressureMeasurement);
        return bloodPressureMeasurement;
    }

    public static List<BloodPressureMeasurement> getBloodPressureMeasurementsFromDB(final AppDatabase database) {
        return database.bloodPressureMeasurementDao().getAll();
    }

    public static ScaleMeasurement addScaleMeasurementsToBD(final AppDatabase database, ScaleMeasurement scaleMeasurement) {
        database.scaleMeasurementDao().insertScaleMeasurements(scaleMeasurement);
        return scaleMeasurement;
    }

    public static List<ScaleMeasurement> getScaleMeasurementsFromDB(final AppDatabase database) {
        return database.scaleMeasurementDao().getAll();
    }
}
