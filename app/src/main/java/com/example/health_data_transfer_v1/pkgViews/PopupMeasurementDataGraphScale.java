package com.example.health_data_transfer_v1.pkgViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.example.health_data_transfer_v1.R;
import com.example.health_data_transfer_v1.pkgData.ScaleMeasurement;
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
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class PopupMeasurementDataGraphScale {
    private Context context;
    private Dialog dialogPopupMeasurementDataGraphScale;
    private LineChart lineChartMeasurementDataScale;
    private LineDataSet lineDataSetWeight;
    private LineDataSet lineDataSetBmi;
    private ArrayList<ILineDataSet> dataSets;
    private LineData lineData;
    private ArrayList<ScaleMeasurement> measurements;

    public PopupMeasurementDataGraphScale(Context context) {
        this.context=context;
        initDialog();
        getAllViews();
    }

    private void getAllViews(){
        lineChartMeasurementDataScale=dialogPopupMeasurementDataGraphScale.findViewById(R.id.lineChartMeasurementDataBloodPressure);
        lineChartMeasurementDataScale.setVisibility(View.VISIBLE);
    }

    private void initDialog(){
        dialogPopupMeasurementDataGraphScale=new Dialog(context);
        dialogPopupMeasurementDataGraphScale.setContentView(R.layout.popup_measurement_data_graph_blood_pressure);
        dialogPopupMeasurementDataGraphScale.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initOtherThings(){
        lineDataSetWeight=new LineDataSet(getDataValues(measurements, MeasurementData.WEIGHT), "weight");
        lineDataSetWeight.setCircleRadius(5);
        lineDataSetWeight.setDrawCircleHole(false);
        lineDataSetWeight.setColor(Color.RED);
        lineDataSetWeight.setLineWidth(3);
        lineDataSetWeight.setCircleColor(Color.RED);
        lineDataSetWeight.setValueTextSize(10);
        lineDataSetBmi=new LineDataSet(getDataValues(measurements, MeasurementData.BMI), "bmi");
        lineDataSetBmi.setCircleRadius(5);
        lineDataSetBmi.setDrawCircleHole(false);
        lineDataSetBmi.setColor(Color.CYAN);
        lineDataSetBmi.setLineWidth(3);
        lineDataSetBmi.setCircleColor(Color.CYAN);
        lineDataSetBmi.setValueTextSize(10);

        dataSets=new ArrayList<>();
        dataSets.add(lineDataSetWeight);
        dataSets.add(lineDataSetBmi);

        lineData=new LineData(dataSets);
        lineChartMeasurementDataScale.setData(lineData);
        lineChartMeasurementDataScale.getDescription().setText("");
        setVisibleRange(measurements.get(0), measurements.get(measurements.size()-1));
        lineChartMeasurementDataScale.setScaleEnabled(true);
        lineChartMeasurementDataScale.getAxisRight().setEnabled(false);
        lineChartMeasurementDataScale.invalidate();
        setXAxis();
    }

    private void setXAxis(){
        XAxis xAxis=lineChartMeasurementDataScale.getXAxis();
        xAxis.setLabelCount(3, true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                LocalDate localDate=new LocalDate((long)value);
                return localDate.getLocalDateAsString();
            }
        });
    }

    private void setVisibleRange(ScaleMeasurement smFirst, ScaleMeasurement smLast){
        long range=smLast.getDateOfMeasurement().getTime()-smFirst.getDateOfMeasurement().getTime();
        lineChartMeasurementDataScale.setVisibleXRangeMaximum((int)range);
    }

    private ArrayList<Entry> getDataValues(ArrayList<ScaleMeasurement> measurements, MeasurementData measurementData){
        ArrayList<Entry> dataValues=new ArrayList<Entry>();
        if(measurementData==MeasurementData.WEIGHT){
            for(int idx=0; idx<measurements.size(); idx++){
                dataValues.add(new Entry(measurements.get(idx).getDateOfMeasurement().getTime(), measurements.get(idx).getWeight()));
            }
        } else if(measurementData==MeasurementData.BMI){
            for(int idx=0; idx<measurements.size(); idx++){
                dataValues.add(new Entry(measurements.get(idx).getDateOfMeasurement().getTime(), measurements.get(idx).getBmi()));
            }
        }

        return dataValues;
    }

    public void showPopup(ArrayList<ScaleMeasurement> measurementsToShow) {
        measurements=measurementsToShow;
        initOtherThings();
        dialogPopupMeasurementDataGraphScale.show();
    }

    public void cancelPopup(){
        dialogPopupMeasurementDataGraphScale.cancel();
    }
}
