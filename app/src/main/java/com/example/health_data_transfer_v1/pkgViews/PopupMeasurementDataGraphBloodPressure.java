package com.example.health_data_transfer_v1.pkgViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.example.health_data_transfer_v1.R;
import com.example.health_data_transfer_v1.pkgData.BloodPressureMeasurement;
import com.example.health_data_transfer_v1.pkgMisc.LocalDate;
import com.example.health_data_transfer_v1.pkgMisc.MeasurementData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class PopupMeasurementDataGraphBloodPressure {
    private Context context;
    private Dialog dialogPopupMeasurementDataGraphBloodPressure;
    private LineChart lineChartMeasurementDataBloodPressure;
    private LineDataSet lineDataSetHeartRate;
    private LineDataSet lineDataSetDiastolic;
    private LineDataSet lineDataSetSystolic;
    private ArrayList<ILineDataSet> dataSets;
    private LineData lineData;
    private ArrayList<BloodPressureMeasurement> measurements;

    public PopupMeasurementDataGraphBloodPressure(Context context) {
        this.context=context;
        initDialog();
        getAllViews();
    }

    private void getAllViews(){
        lineChartMeasurementDataBloodPressure=dialogPopupMeasurementDataGraphBloodPressure.findViewById(R.id.lineChartMeasurementDataBloodPressure);
        lineChartMeasurementDataBloodPressure.setVisibility(View.VISIBLE);
    }

    private void initDialog(){
        dialogPopupMeasurementDataGraphBloodPressure=new Dialog(context);
        dialogPopupMeasurementDataGraphBloodPressure.setContentView(R.layout.popup_measurement_data_graph_blood_pressure);
        dialogPopupMeasurementDataGraphBloodPressure.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initOtherThings(){
        lineDataSetHeartRate=new LineDataSet(getDataValues(measurements, MeasurementData.HEARTRATE), "heart rate");
        lineDataSetHeartRate.setCircleRadius(5);
        lineDataSetHeartRate.setDrawCircleHole(false);
        lineDataSetHeartRate.setColor(Color.RED);
        lineDataSetHeartRate.setLineWidth(3);
        lineDataSetHeartRate.setCircleColor(Color.RED);
        lineDataSetHeartRate.setValueTextSize(10);
        lineDataSetDiastolic=new LineDataSet(getDataValues(measurements, MeasurementData.DIASTOLIC), "diastolic");
        lineDataSetDiastolic.setCircleRadius(5);
        lineDataSetDiastolic.setDrawCircleHole(false);
        lineDataSetDiastolic.setColor(Color.CYAN);
        lineDataSetDiastolic.setLineWidth(3);
        lineDataSetDiastolic.setCircleColor(Color.CYAN);
        lineDataSetDiastolic.setValueTextSize(10);
        lineDataSetSystolic=new LineDataSet(getDataValues(measurements, MeasurementData.SYSTOLIC), "systolic");
        lineDataSetSystolic.setCircleRadius(5);
        lineDataSetSystolic.setDrawCircleHole(false);
        lineDataSetSystolic.setColor(Color.MAGENTA);
        lineDataSetSystolic.setLineWidth(3);
        lineDataSetSystolic.setCircleColor(Color.MAGENTA);
        lineDataSetSystolic.setValueTextSize(10);

        dataSets=new ArrayList<>();
        dataSets.add(lineDataSetHeartRate);
        dataSets.add(lineDataSetDiastolic);
        dataSets.add(lineDataSetSystolic);

        lineData=new LineData(dataSets);
        lineChartMeasurementDataBloodPressure.setData(lineData);
        lineChartMeasurementDataBloodPressure.setVisibleXRangeMaximum(500000000);
        lineChartMeasurementDataBloodPressure.getDescription().setText("");
        lineChartMeasurementDataBloodPressure.setScaleEnabled(false);
        setXAxis();
    }

    private void setXAxis(){
        XAxis xAxis=lineChartMeasurementDataBloodPressure.getXAxis();
        xAxis.setLabelCount(3, true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return super.getAxisLabel(value, axis);
            }
        });
    }

    private ArrayList<Entry> getDataValues(ArrayList<BloodPressureMeasurement> measurements, MeasurementData measurementData){
        ArrayList<Entry> dataValues=new ArrayList<Entry>();
        if(measurementData==MeasurementData.HEARTRATE){
            for(BloodPressureMeasurement bpm:measurements){
                dataValues.add(new Entry(bpm.getDateOfMeasurement().getTime(), bpm.getHeartRate()));
            }
        } else if(measurementData==MeasurementData.DIASTOLIC){
            for(BloodPressureMeasurement bpm:measurements){
                dataValues.add(new Entry(bpm.getDateOfMeasurement().getTime(), bpm.getDiastolic()));
            }
        } else if(measurementData==MeasurementData.SYSTOLIC){
            for(BloodPressureMeasurement bpm:measurements){
                dataValues.add(new Entry(bpm.getDateOfMeasurement().getTime(), bpm.getSystolic()));
            }
        }

        return dataValues;
    }

    public void showPopup(ArrayList<BloodPressureMeasurement> measurementsToShow) {
        measurements=measurementsToShow;
        initOtherThings();
        dialogPopupMeasurementDataGraphBloodPressure.show();
    }

    public void cancelPopup(){
        dialogPopupMeasurementDataGraphBloodPressure.cancel();
    }
}
