package com.example.health_data_transfer_v1.pkgViews;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.example.health_data_transfer_v1.R;

public class PopupMeasurementDataBloodPressure{
    private Context context;
    private Dialog dialogPopupMeasurementDataBloodPressure;
    private TextView lblDiastolic;
    private TextView txtDiastolicValue;
    private TextView lblSystolic;
    private TextView txtSystolicValue;
    private TextView lblHeartRate;
    private TextView txtHeartRateValue;

    public PopupMeasurementDataBloodPressure(Context context) {
        this.context=context;
        initDialog();
        getAllViews();
    }

    private void initDialog(){
        dialogPopupMeasurementDataBloodPressure=new Dialog(context);
        dialogPopupMeasurementDataBloodPressure.setContentView(R.layout.popup_measurement_data_blood_pressure);
        dialogPopupMeasurementDataBloodPressure.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void getAllViews(){
        lblDiastolic=dialogPopupMeasurementDataBloodPressure.findViewById(R.id.lblDiastolic);
        txtDiastolicValue=dialogPopupMeasurementDataBloodPressure.findViewById(R.id.txtDiastolicValue);
        lblSystolic=dialogPopupMeasurementDataBloodPressure.findViewById(R.id.lblSystolic);
        txtSystolicValue=dialogPopupMeasurementDataBloodPressure.findViewById(R.id.txtSystolicValue);
        lblHeartRate=dialogPopupMeasurementDataBloodPressure.findViewById(R.id.lblHeartRate);
        txtHeartRateValue=dialogPopupMeasurementDataBloodPressure.findViewById(R.id.txtHeartRateValue);
    }

    private void setDialogFields(BloodPressureMeasurement bloodPressureMeasurement){
        txtDiastolicValue.setText(String.valueOf(bloodPressureMeasurement.getDiastolic()));
        txtSystolicValue.setText(String.valueOf(bloodPressureMeasurement.getDiastolic()));
        txtHeartRateValue.setText(String.valueOf(bloodPressureMeasurement.getHeartRate()));
    }

    public void showPopup(BloodPressureMeasurement bloodPressureMeasurement) {
        setDialogFields(bloodPressureMeasurement);
        dialogPopupMeasurementDataBloodPressure.show();
    }

    public void cancelPopup(){
        dialogPopupMeasurementDataBloodPressure.cancel();
    }
}
