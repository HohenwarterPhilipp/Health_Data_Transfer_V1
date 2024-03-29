package com.example.healthx.pkgViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.healthx.R;
import com.example.healthx.pkgData.BloodPressureMeasurement;
import com.example.healthx.pkgMisc.LocalDate;
import com.example.healthx.pkgMisc.MeasurementValueType;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class PopupMeasurementDataBloodPressureGraph {
    private Context context;
    private Dialog dialogPopupMeasurementDataGraphBloodPressure;
    private LineChart lineChartMeasurementDataBloodPressure;
    private LineData lineData;
    private ArrayList<ILineDataSet> lineDataSets;
    private LineDataSet lineDataSetHeartRate;
    private LineDataSet lineDataSetDiastolic;
    private LineDataSet lineDataSetSystolic;
    private ArrayList<LocalDate> listLocalDatesXAxis;

    public PopupMeasurementDataBloodPressureGraph(Context context) {
        this.context=context;
        initDialog();
        getAllViews();
        initLineChartComponents();
    }

    //region dialog configuration methods
    private void getAllViews(){
        lineChartMeasurementDataBloodPressure=dialogPopupMeasurementDataGraphBloodPressure.findViewById(R.id.lineChartMeasurementDataBloodPressure);
        lineChartMeasurementDataBloodPressure.setVisibility(View.VISIBLE);
    }

    private void initDialog(){
        dialogPopupMeasurementDataGraphBloodPressure=new Dialog(context);
        dialogPopupMeasurementDataGraphBloodPressure.setContentView(R.layout.popup_measurement_data_blood_pressure_graph);
        dialogPopupMeasurementDataGraphBloodPressure.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void settingsLineDataSet(LineDataSet lineDataSet, int color){
        lineDataSet.setCircleRadius(5);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setColor(color);
        lineDataSet.setLineWidth(3);
        lineDataSet.setCircleColor(color);
        lineDataSet.setValueTextSize(10);
    }

    private void initLineChartComponents(){
        lineDataSets=new ArrayList<>();
        listLocalDatesXAxis=new ArrayList<>();
        lineDataSetHeartRate=new LineDataSet(null, "heart rate");
        lineDataSetDiastolic=new LineDataSet(null, "diastolic");
        lineDataSetSystolic=new LineDataSet(null, "systolic");

        settingsLineDataSet(lineDataSetHeartRate, Color.RED);
        settingsLineDataSet(lineDataSetDiastolic, Color.CYAN);
        settingsLineDataSet(lineDataSetSystolic, Color.MAGENTA);

        lineChartMeasurementDataBloodPressure.getDescription().setText("");
        lineChartMeasurementDataBloodPressure.setScaleEnabled(false);
        lineChartMeasurementDataBloodPressure.getAxisRight().setEnabled(false);
    }
    //endregion

    private void formatXAxis(){
        XAxis xAxis=lineChartMeasurementDataBloodPressure.getXAxis();
        xAxis.setLabelCount(4, true);
        xAxis.setValueFormatter(new ValueFormatter() {
            int idx;
            @Override
            public String getFormattedValue(float value) {
                idx=(int)value;

                if(idx<listLocalDatesXAxis.size()-0.9){
                    return new LocalDate(listLocalDatesXAxis.get((int)value).getTime()).getLocalDateAsString();
                }

                return "";
            }
        });
    }

    private ArrayList<Entry> getDataValues(ArrayList<BloodPressureMeasurement> measurements, MeasurementValueType measurementData){
        ArrayList<Entry> dataValues=new ArrayList<>();

        if(measurementData== MeasurementValueType.HEARTRATE){
            for(int idx=0; idx<measurements.size(); idx++){
                listLocalDatesXAxis.add(measurements.get(idx).getDateOfMeasurement());
                dataValues.add(new Entry(idx, measurements.get(idx).getHeartRate()));
            }
        } else if(measurementData== MeasurementValueType.DIASTOLIC){
            for(int idx=0; idx<measurements.size(); idx++){
                dataValues.add(new Entry(idx, measurements.get(idx).getDiastolic()));
            }
        } else if(measurementData== MeasurementValueType.SYSTOLIC) {
            for (int idx = 0; idx < measurements.size(); idx++) {
                dataValues.add(new Entry(idx, measurements.get(idx).getSystolic()));
            }
        }

        return dataValues;
    }

    private void fillLineDataSet(ArrayList<Entry> entriesLineDataSet, LineDataSet lineDataSet){
        for(Entry entry:entriesLineDataSet){
            lineDataSet.addEntry(entry);
        }
    }

    private void clearLineDataSets(){
        lineDataSets.clear();
        lineDataSetHeartRate.clear();
        lineDataSetDiastolic.clear();
        lineDataSetSystolic.clear();
    }

    public void showPopup(ArrayList<BloodPressureMeasurement> measurements, int idxChosenMeasurement) {
        ArrayList<Entry> entriesHeartRate=getDataValues(measurements, MeasurementValueType.HEARTRATE);
        ArrayList<Entry> entriesDiastolic=getDataValues(measurements, MeasurementValueType.DIASTOLIC);
        ArrayList<Entry> entriesSystolic=getDataValues(measurements, MeasurementValueType.SYSTOLIC);

        clearLineDataSets();
        fillLineDataSet(entriesHeartRate, lineDataSetHeartRate);
        fillLineDataSet(entriesDiastolic, lineDataSetDiastolic);
        fillLineDataSet(entriesSystolic, lineDataSetSystolic);
        lineDataSets.add(lineDataSetHeartRate);
        lineDataSets.add(lineDataSetDiastolic);
        lineDataSets.add(lineDataSetSystolic);
        lineData=new LineData(lineDataSets);
        lineChartMeasurementDataBloodPressure.setData(lineData);
        lineChartMeasurementDataBloodPressure.invalidate();
        lineChartMeasurementDataBloodPressure.setVisibleXRangeMaximum(3);
        Entry entryChosenMeasurementHeartRate=lineDataSetHeartRate.getEntryForIndex(idxChosenMeasurement);
        Entry entryChosenMeasurementDiastolic=lineDataSetDiastolic.getEntryForIndex(idxChosenMeasurement);
        Entry entryChosenMeasurementSystolic=lineDataSetSystolic.getEntryForIndex(idxChosenMeasurement);
        Drawable drawableCircleChosenMeasurement= ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle_choosen_measurement, null);
        entryChosenMeasurementHeartRate.setIcon(drawableCircleChosenMeasurement);
        entryChosenMeasurementDiastolic.setIcon(drawableCircleChosenMeasurement);
        entryChosenMeasurementSystolic.setIcon(drawableCircleChosenMeasurement);

        formatXAxis();
        dialogPopupMeasurementDataGraphBloodPressure.show();
    }

    public void cancelPopup(){
        dialogPopupMeasurementDataGraphBloodPressure.cancel();
    }
}
