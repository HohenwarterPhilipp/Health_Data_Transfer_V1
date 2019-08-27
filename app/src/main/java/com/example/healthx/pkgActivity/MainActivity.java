package com.example.healthx.pkgActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.healthx.R;
import com.example.healthx.pkgData.ScaleMeasurement;
import com.example.healthx.pkgManager.AlertManager;

import android.content.pm.PackageManager;
import android.Manifest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SweetAlertDialog.OnSweetClickListener {
    private ImageButton btnBloodPressureMeasurement;
    private ImageButton btnScaleMeasurement;
    private Toolbar toolbar;
    private AlertManager alertManager;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllViews();
        initOtherThings();
        registerEventHandlers();
        checkPermissions();
        checkBluetooth();
        checkLocation();
    }

    //region activity configuration methods
    private void getAllViews(){
        btnBloodPressureMeasurement=findViewById(R.id.btnBloodPressureMeasurement);
        btnScaleMeasurement=findViewById(R.id.btnScaleMeasurement);
        toolbar=findViewById(R.id.toolbar);
    }

    private void registerEventHandlers(){
        btnBloodPressureMeasurement.setOnClickListener(this);
        btnScaleMeasurement.setOnClickListener(this);
        alertManager.setOnSweetClickListener(this);
    }

    private void initOtherThings(){
        setSupportActionBar(toolbar);
        alertManager=new AlertManager(this);
    }
    //endregion

    @Override
    public void onClick(View view) {
        try {
            if (view.getId() == R.id.btnBloodPressureMeasurement) {
                startBloodPressureMeasurementActivity();
            } else if(view.getId() == R.id.btnScaleMeasurement) {
                startScaleMeasurementActivity();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //region check bluetooth/location methods
    private void checkBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }

    private void checkLocation(){
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            alertManager.showAlertDialogGPS();
        }
    }
    //endregion

    //region start intent measurement methods
    private void startBloodPressureMeasurementActivity(){
        startActivity(new Intent(this, BloodPressureMeasurementActivity.class));
    }

    private void startScaleMeasurementActivity(){
        startActivity(new Intent(this, ScaleMeasurementActivity.class));
    }
    //endregion

    //region permission check methods
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<>();

        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {
            final String[] permissions = missingPermissions.toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS, grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        finish();
                        return;
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(SweetAlertDialog sweetAlertDialog) {
        sweetAlertDialog.cancel();
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
    //endregion
}
