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
    }

    private void getAllViews(){
        btnReceiveScaleMeasurement=findViewById(R.id.btnReceiveScaleMeasurement);
        listScaleMeasurement=findViewById(R.id.listScaleMeasurement);
    }

    private void registerEventHandlers(){
        btnReceiveScaleMeasurement.setOnClickListener(this);
        listScaleMeasurement.setOnItemLongClickListener(this);
        listScaleMeasurement.setOnItemClickListener(this);
    }

    private void initOtherThings(){
        deviceScale=new DeviceScale();
        adapterScaleMeasurement=new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        listScaleMeasurement.setAdapter(adapterScaleMeasurement);
        currentScaleMeasurement=null;
        alertManager=new AlertManager(this);


        deviceScale=new DeviceScale();
        currentScaleMeasurement=null;
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initArrayAdapter();
        initManager();
        adapterScaleMeasurement.add(new ScaleMeasurement(160, 100, new LocalDate(System.currentTimeMillis())));   //only for tests
        adapterScaleMeasurement.add(new ScaleMeasurement(120, 140, new LocalDate(System.currentTimeMillis()+10000)));
        //adapterScaleMeasurement.add(new ScaleMeasurement(214, 12, new LocalDate(System.currentTimeMillis() + 77777)));
        //adapterScaleMeasurement.add(new ScaleMeasurement(110, 53, new LocalDate(System.currentTimeMillis() + 88888)));
        //adapterScaleMeasurement.add(new ScaleMeasurement(222, 112, new LocalDate(System.currentTimeMillis() + 999999)));
    }

    private void initArrayAdapter(){
        adapterScaleMeasurement=new ArrayAdapter<>(this, R.layout.layout_listview_center_items, R.id.txtListItem);
        listScaleMeasurement.setAdapter(adapterScaleMeasurement);
    }

    private void initManager(){
        alertManager=new AlertManager(this);
    }

    private void initPopups(){
        popupConnectingCountdown=new PopupConnectingCountdown(this);
        popupMeasurementData=new PopupMeasurementDataScale(this);
        popupMeasurementDataGraphScale=new PopupMeasurementDataGraphScale(this);
    }

    @Override
    public void onClick(View view) {
        try {
            if (view.getId() == R.id.btnReceiveScaleMeasurement) {
                popupConnectingCountdown.showPopup();
                currentScaleMeasurement=null;
                btnReceiveScaleMeasurement.setEnabled(false);
                deviceScale.scan(this, this);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            popupConnectingCountdown.cancelPopup();
        }
    }

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

    @Override
    public void onMeasurementStarted() {

    }

    @Override
    public void onMeasurementProgress(final MeasurementType measurementType, final Object o) {
        if(measurementType.equals(MeasurementType.SCALE_SIMPLE)){
            onMeasurementFinished(measurementType, o);
        }
    }

    @Override
    public void onMeasurementError(Error error) {
        if(!error.equals(Error.SCALE_RESISTANCE)){
            alertManager.showAlertDialogMeasurementError(error);
            deviceScale.destroy();
            btnReceiveScaleMeasurement.setEnabled(true);
        }
    }

    @Override
    public void onMeasurementFinished(MeasurementType measurementType, Object o) {
        if(currentScaleMeasurement==null){
            ScaleSimple scaleSimple=(ScaleSimple) o;
            currentScaleMeasurement=new ScaleMeasurement(scaleSimple.getWeight(), scaleSimple.getBmi(), new LocalDate(System.currentTimeMillis()));
            adapterScaleMeasurement.add(currentScaleMeasurement);
            deviceScale.destroy();
            btnReceiveScaleMeasurement.setEnabled(true);
        }
    }

    @Override
    public ScaleUserInfoWithBodyType getUserInfo(DeviceScale.Type type) {
        return new ScaleUserInfoWithBodyType(18, ScaleUserInfo.Gender.MAN, 178, ScaleUserInfo.BodyType.NORMAL);
    }

    @Override
    public void onResistanceUpdate(int i) {
        Toast.makeText(this, i, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        ArrayList<ScaleMeasurement> measurements=new ArrayList<>();
        for(int idx=0; idx<adapterScaleMeasurement.getCount(); idx++){
            measurements.add(adapterScaleMeasurement.getItem(idx));
        }

        popupMeasurementDataGraphScale.showPopup(measurements);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ScaleMeasurement scaleMeasurement=(ScaleMeasurement) adapterView.getItemAtPosition(position);
        popupMeasurementData.showPopup(scaleMeasurement);
    }
}
