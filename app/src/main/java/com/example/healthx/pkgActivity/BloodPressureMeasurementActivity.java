package com.example.healthx.pkgActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.healthx.R;
import com.example.healthx.pkgData.AppDatabase;
import com.example.healthx.pkgData.BloodPressureMeasurement;
import com.example.healthx.pkgData.DatabaseHandler;
import com.example.healthx.pkgManager.AlertManager;
import com.example.healthx.pkgMisc.LocalDate;
import com.example.healthx.pkgViews.PopupConnectingCountdown;
import com.example.healthx.pkgViews.PopupMeasurementDataBloodPressure;
import com.example.healthx.pkgViews.PopupMeasurementDataBloodPressureGraph;
import com.ivy.ivyconnect.device.callback.DeviceConnectCallback;
import com.ivy.ivyconnect.device.callback.DeviceDisconnectCallback;
import com.ivy.ivyconnect.device.callback.DeviceMeasurementCallback;
import com.ivy.ivyconnect.device.callback.DeviceReconnectCallback;
import com.ivy.ivyconnect.device.ivy.DeviceArm;
import com.ivy.ivyconnect.model.pressure.PressureComplete;
import com.ivy.ivyconnect.util.Error;
import com.ivy.ivyconnect.util.MeasurementType;

import java.util.ArrayList;
import java.util.List;

public class BloodPressureMeasurementActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, DeviceConnectCallback, DeviceMeasurementCallback, DeviceDisconnectCallback, DeviceReconnectCallback {
    private Button btnReceiveBloodPressureMeasurement;
    private ListView listBloodPressureMeasurement;
    private ArrayAdapter<BloodPressureMeasurement> adapterBloodPressureMeasurement;
    private DeviceArm deviceArm;
    private BloodPressureMeasurement currentBloodPressureMeasurement;
    private PopupConnectingCountdown popupConnectingCountdown;
    private PopupMeasurementDataBloodPressure popupMeasurementData;
    private PopupMeasurementDataBloodPressureGraph popupMeasurementDataGraphBloodPressure;
    private AlertManager alertManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_measurement);
        getAllViews();
        registerEventHandlers();
        initOtherThings();
        initPopups();
        new AllMeasurementsLoader().execute(this);
    }

    //region activity configuration methods
    private void getAllViews() {
        btnReceiveBloodPressureMeasurement = findViewById(R.id.btnReceiveBloodPressureMeasurement);
        listBloodPressureMeasurement = findViewById(R.id.listBloodPressureMeasurement);
        toolbar = findViewById(R.id.toolbar);
    }

    private void registerEventHandlers() {
        btnReceiveBloodPressureMeasurement.setOnClickListener(this);
        listBloodPressureMeasurement.setOnItemLongClickListener(this);
        listBloodPressureMeasurement.setOnItemClickListener(this);
    }

    private void initOtherThings() {
        deviceArm = new DeviceArm();
        currentBloodPressureMeasurement = null;
        setSupportActionBar(toolbar);
        initArrayAdapter();
        initManager();
    }

    private void initArrayAdapter() {
        adapterBloodPressureMeasurement = new ArrayAdapter<>(this, R.layout.layout_listview_center_items, R.id.txtListItem);
        listBloodPressureMeasurement.setAdapter(adapterBloodPressureMeasurement);
    }

    private void initManager() {
        alertManager = new AlertManager(this);
    }

    private void initPopups() {
        popupConnectingCountdown = new PopupConnectingCountdown(this);
        popupMeasurementData = new PopupMeasurementDataBloodPressure(this);
        popupMeasurementDataGraphBloodPressure = new PopupMeasurementDataBloodPressureGraph(this);
    }
    //endregion

    @Override
    public void onClick(View view) {
        try {
            if (view.getId() == R.id.btnReceiveBloodPressureMeasurement) {
                popupConnectingCountdown.showPopup();
                currentBloodPressureMeasurement = null;
                btnReceiveBloodPressureMeasurement.setEnabled(false);
                deviceArm.scan(this, this);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            popupConnectingCountdown.cancelPopup();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(adapterView.getCount()>1){
            ArrayList<BloodPressureMeasurement> measurements = new ArrayList<>();

            for (int idx = 0; idx < adapterBloodPressureMeasurement.getCount(); idx++) {
                measurements.add(adapterBloodPressureMeasurement.getItem(idx));
            }

            popupMeasurementDataGraphBloodPressure.showPopup(measurements, position);
        } else {
            alertManager.showAlertDialogGraphMeasurement();
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        BloodPressureMeasurement bloodPressureMeasurement = (BloodPressureMeasurement) adapterView.getItemAtPosition(position);
        popupMeasurementData.showPopup(bloodPressureMeasurement);
    }

    //region device connection methods
    private void registerDeviceListener() {
        deviceArm.setDisconnectCallback(this);
        deviceArm.setReconnectCallback(this);
    }

    @Override
    public void onDeviceConnected() {
        try {
            Thread.sleep(2000);                 //device needs time for configuration
            popupConnectingCountdown.cancelPopup();
            alertManager.showAlertDialogConnected();
            registerDeviceListener();
            deviceArm.startMeasurement(this);
        } catch (InterruptedException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDeviceConnectFailed(Error error) {
        popupConnectingCountdown.cancelPopup();
        alertManager.showAlertDialogConnectionFailed();
        deviceArm.destroy();
        btnReceiveBloodPressureMeasurement.setEnabled(true);
    }

    @Override
    public void onDeviceDisconnected(Error error) {
        alertManager.showAlertDialogDisconnected();
        deviceArm.destroy();
        btnReceiveBloodPressureMeasurement.setEnabled(true);
    }

    @Override
    public void onDeviceReconnecting() {
        alertManager.showAlertDialogReconnecting();
    }

    @Override
    public void onDeviceReconnectingFailed(Error error) {
        alertManager.cancelAlertDialogReconnecting();
        alertManager.showAlertDialogReconnectingFailed();
    }

    @Override
    public void onDeviceReconnectingSuccess() {
        alertManager.cancelAlertDialogReconnecting();
        alertManager.showAlertDialogReconnected();
    }
    //endregion

    //region device measurement methods
    @Override
    public void onMeasurementStarted() {
        btnReceiveBloodPressureMeasurement.setEnabled(false);
    }

    @Override
    public void onMeasurementProgress(MeasurementType measurementType, Object o) {
    }

    @Override
    public void onMeasurementFinished(MeasurementType measurementType, Object o) {
        if (currentBloodPressureMeasurement == null) {
            PressureComplete pressureComplete = (PressureComplete) o;
            currentBloodPressureMeasurement = new BloodPressureMeasurement(pressureComplete.getDiastolic(), pressureComplete.getSystolic(), pressureComplete.getHeartRate(), new LocalDate(System.currentTimeMillis()));
            new MeasurementSaver(currentBloodPressureMeasurement).execute(this);
            deviceArm.destroy();
            btnReceiveBloodPressureMeasurement.setEnabled(true);
        }
    }

    @Override
    public void onMeasurementError(Error error) {
        alertManager.showAlertDialogMeasurementError(error);
        deviceArm.destroy();
        btnReceiveBloodPressureMeasurement.setEnabled(true);
    }
    //endregion

    private class AllMeasurementsLoader extends AsyncTask<Context, Void, List<BloodPressureMeasurement>> {

        @Override
        protected List<BloodPressureMeasurement> doInBackground(Context... contexts) {
            return DatabaseHandler.getBloodPressureMeasurementsFromDB(AppDatabase.getAppDatabase(getApplicationContext()));
        }

        @Override
        protected void onPostExecute(List<BloodPressureMeasurement> measurements) {
            super.onPostExecute(measurements);
            adapterBloodPressureMeasurement.addAll(measurements);
        }
    }

    private class MeasurementSaver extends AsyncTask<Context, Void,BloodPressureMeasurement> {
        BloodPressureMeasurement cur;

        public MeasurementSaver(BloodPressureMeasurement currentBloodPressureMeasurement) {
        cur =currentBloodPressureMeasurement;
        }

        @Override
        protected BloodPressureMeasurement doInBackground(Context... contexts) {
            return DatabaseHandler.addBloodPressureMeasurementToDB(AppDatabase.getAppDatabase(getApplicationContext()),cur);
        }

        @Override
        protected void onPostExecute(BloodPressureMeasurement measurement) {
            super.onPostExecute(measurement);
            adapterBloodPressureMeasurement.add(measurement);
        }
    }
}
