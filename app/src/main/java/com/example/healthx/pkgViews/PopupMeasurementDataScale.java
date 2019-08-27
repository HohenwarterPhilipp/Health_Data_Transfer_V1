package com.example.healthx.pkgViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.example.healthx.R;
import com.example.healthx.pkgData.ScaleMeasurement;

public class PopupMeasurementDataScale {
    private Context context;
    private Dialog dialogPopupMeasurementDataScale;
    private TextView lblWeight;
    private TextView txtWeightValue;
    private TextView lblBMI;
    private TextView txtBMIValue;

    public PopupMeasurementDataScale(Context context) {
        this.context=context;
        initDialog();
        getAllViews();
    }

    //region dialog configuration methods
    private void initDialog(){
        dialogPopupMeasurementDataScale=new Dialog(context);
        dialogPopupMeasurementDataScale.setContentView(R.layout.popup_measurement_data_scale);
        dialogPopupMeasurementDataScale.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void getAllViews(){
        lblWeight=dialogPopupMeasurementDataScale.findViewById(R.id.lblWeight);
        txtWeightValue=dialogPopupMeasurementDataScale.findViewById(R.id.txtWeightValue);
        lblBMI=dialogPopupMeasurementDataScale.findViewById(R.id.lblBMI);
        txtBMIValue=dialogPopupMeasurementDataScale.findViewById(R.id.txtBMIValue);
    }
    //endregion

    private void setDialogFields(ScaleMeasurement scaleMeasurement){
        txtWeightValue.setText(String.valueOf(scaleMeasurement.getWeight()));
        txtBMIValue.setText(String.valueOf(scaleMeasurement.getBmi()));
    }

    public void showPopup(ScaleMeasurement scaleMeasurement) {
        setDialogFields(scaleMeasurement);
        dialogPopupMeasurementDataScale.show();
    }

    public void cancelPopup(){
        dialogPopupMeasurementDataScale.cancel();
    }
}
