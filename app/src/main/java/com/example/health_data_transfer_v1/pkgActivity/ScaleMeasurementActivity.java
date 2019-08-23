package com.example.health_data_transfer_v1.pkgActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.health_data_transfer_v1.R;
import com.example.health_data_transfer_v1.pkgData.AppDatabase;
import com.example.health_data_transfer_v1.pkgData.ScaleMeasurement;
import com.example.health_data_transfer_v1.pkgManager.AlertManager;
import com.example.health_data_transfer_v1.pkgMisc.LocalDate;
import com.example.health_data_transfer_v1.pkgViews.PopupConnectingCountdown;
import com.example.health_data_transfer_v1.pkgViews.PopupMeasurementDataGraphScale;
import com.example.health_data_transfer_v1.pkgViews.PopupMeasurementDataScale;
import com.ivy.ivyconnect.device.callback.DeviceConnectCallback;
import com.ivy.ivyconnect.device.callback.DeviceDisconnectCallback;
import com.ivy.ivyconnect.device.callback.DeviceMeasurementCallback;
import com.ivy.ivyconnect.device.callback.DeviceReconnectCallback;
import com.ivy.ivyconnect.device.ivy.DeviceScale;
import com.ivy.ivyconnect.model.scale.ScaleSimple;
import com.ivy.ivyconnect.model.scale.ScaleUserInfo;
import com.ivy.ivyconnect.model.scale.ScaleUserInfoWithBodyType;
import com.ivy.ivyconnect.util.Error;
import com.ivy.ivyconnect.util.MeasurementType;

import java.util.ArrayList;
import java.util.List;

public class ScaleMeasurementActivity extends AppCompatActivity implements View.OnClickListener, DeviceConnectCallback, DeviceMeasurementCallback, DeviceScale.UserInfoListener, DeviceDisconnectCallback, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, DeviceReconnectCallback {
    private Button btnReceiveScaleMeasurement;
    private ListView listScaleMeasurement;
    private ArrayAdapter<ScaleMeasurement> adapterScaleMeasurement;
    private DeviceScale deviceScale;
    private ScaleMeasurement currentScaleMeasurement;
    private PopupConnectingCountdown popupConnectingCountdown;
    private PopupMeasurementDataScale popupMeasurementData;
    private PopupMeasurementDataGraphScale popupMeasurementDataGraphScale;
    private AlertManager alertManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_measurement);
        getAllViews();
        registerEventHandlers();
        initOtherThings();
        initPopups();
        adapterScaleMeasurement.addAll(getScaleMeasurementsFromDB(AppDatabase.getAppDatabase(this)));

        adapterScaleMeasurement.add(new ScaleMeasurement(160, 100, new LocalDate(System.currentTimeMillis())));   //only for tests
        adapterScaleMeasurement.add(new ScaleMeasurement(157, 98, new LocalDate(System.currentTimeMillis() + 200000)));
        adapterScaleMeasurement.add(new ScaleMeasurement(158, 98.5f, new LocalDate(System.currentTimeMillis() + 444444)));
        adapterScaleMeasurement.add(new ScaleMeasurement(155, 96.5f, new LocalDate(System.currentTimeMillis() + 9999999)));
        adapterScaleMeasurement.add(new ScaleMeasurement(140, 90, new LocalDate(System.currentTimeMillis() + 999999999)));
    }

    //region activity configuration methods
    private void getAllViews() {
        btnReceiveScaleMeasurement = findViewById(R.id.btnReceiveScaleMeasurement);
        listScaleMeasurement = findViewById(R.id.listScaleMeasurement);
    }

    private void registerEventHandlers() {
        btnReceiveScaleMeasurement.setOnClickListener(this);
        listScaleMeasurement.setOnItemLongClickListener(this);
        listScaleMeasurement.setOnItemClickListener(this);
    }

    private void initOtherThings() {
        currentScaleMeasurement = null;
        deviceScale = new DeviceScale();
        alertManager = new AlertManager(this);
        deviceScale = new DeviceScale();
        adapterScaleMeasurement = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        listScaleMeasurement.setAdapter(adapterScaleMeasurement);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initArrayAdapter();
        initManager();
    }

    private void initArrayAdapter() {
        adapterScaleMeasurement = new ArrayAdapter<>(this, R.layout.layout_listview_center_items, R.id.txtListItem);
        listScaleMeasurement.setAdapter(adapterScaleMeasurement);
    }

    private void initManager() {
        alertManager = new AlertManager(this);
    }

    private void initPopups() {
        popupConnectingCountdown = new PopupConnectingCountdown(this);
        popupMeasurementData = new PopupMeasurementDataScale(this);
        popupMeasurementDataGraphScale = new PopupMeasurementDataGraphScale(this);
    }
    //endregion

    @Override
    public void onClick(View view) {
        try {
            if (view.getId() == R.id.btnReceiveScaleMeasurement) {
                popupConnectingCountdown.showPopup();
                currentScaleMeasurement = null;
                btnReceiveScaleMeasurement.setEnabled(false);
                deviceScale.scan(this, this);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            popupConnectingCountdown.cancelPopup();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        ArrayList<ScaleMeasurement> measurements = new ArrayList<>();

        for (int idx = 0; idx < adapterScaleMeasurement.getCount(); idx++) {
            measurements.add(adapterScaleMeasurement.getItem(idx));
        }

        popupMeasurementDataGraphScale.showPopup(measurements, position);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ScaleMeasurement scaleMeasurement = (ScaleMeasurement) adapterView.getItemAtPosition(position);
        popupMeasurementData.showPopup(scaleMeasurement);
    }

    //region device connection methods
    @Override
    public void onDeviceConnected() {
        try {
            Thread.sleep(2000);
            popupConnectingCountdown.cancelPopup();
            alertManager.showAlertDialogConnected();
            deviceScale.setDisconnectCallback(this);
            deviceScale.setReconnectCallback(this);
            deviceScale.startMeasurement(this, this);
        } catch (Exception ex) {
            Toast.makeText(this, "Exception:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDeviceConnectFailed(Error error) {
        popupConnectingCountdown.cancelPopup();
        alertManager.showAlertDialogConnectionFailed();
        deviceScale.destroy();
        btnReceiveScaleMeasurement.setEnabled(true);
    }

    @Override
    public void onDeviceDisconnected(Error error) {
        alertManager.showAlertDialogDisconnected();
        deviceScale.destroy();
        btnReceiveScaleMeasurement.setEnabled(true);
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

    }

    @Override
    public void onMeasurementProgress(final MeasurementType measurementType, final Object o) {
        if (measurementType.equals(MeasurementType.SCALE_SIMPLE)) {
            onMeasurementFinished(measurementType, o);
        }
    }

    @Override
    public void onMeasurementError(Error error) {
        if (!error.equals(Error.SCALE_RESISTANCE)) {
            alertManager.showAlertDialogMeasurementError(error);
            deviceScale.destroy();
            btnReceiveScaleMeasurement.setEnabled(true);
        }
    }

    @Override
    public void onMeasurementFinished(MeasurementType measurementType, Object o) {
        if (currentScaleMeasurement == null) {
            ScaleSimple scaleSimple = (ScaleSimple) o;
            currentScaleMeasurement = new ScaleMeasurement(scaleSimple.getWeight(), scaleSimple.getBmi(), new LocalDate(System.currentTimeMillis()));
            adapterScaleMeasurement.add(currentScaleMeasurement);
            addScaleMeasurementsToBD(AppDatabase.getAppDatabase(this), currentScaleMeasurement);
            deviceScale.destroy();
            btnReceiveScaleMeasurement.setEnabled(true);
        }
    }
    //endregion

    //region local database methods
    private static ScaleMeasurement addScaleMeasurementsToBD(final AppDatabase database, ScaleMeasurement scaleMeasurement) {
        database.scaleMeasurementDao().insertScaleMeasurements(scaleMeasurement);
        return scaleMeasurement;
    }

    private static List<ScaleMeasurement> getScaleMeasurementsFromDB(final AppDatabase database) {
        return database.scaleMeasurementDao().getAll();
    }
    //endregion

    //region scale user methods
    @Override
    public ScaleUserInfoWithBodyType getUserInfo(DeviceScale.Type type) {
        return new ScaleUserInfoWithBodyType(18, ScaleUserInfo.Gender.MAN, 178, ScaleUserInfo.BodyType.NORMAL);             //only a plain scale user for testing the measurement
    }

    @Override
    public void onResistanceUpdate(int i) {
    }
    //endregion
}
