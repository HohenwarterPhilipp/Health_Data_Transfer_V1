package com.example.healthx.pkgViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.healthx.R;
import com.example.healthx.pkgData.ScaleMeasurement;
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

public class PopupMeasurementDataGraphScale {
    private Context context;
    private Dialog dialogPopupMeasurementDataGraphScale;
    private LineChart lineChartMeasurementDataScale;
    private LineData lineData;
    private ArrayList<ILineDataSet> lineDataSets;
    private LineDataSet lineDataSetWeight;
    private LineDataSet lineDataSetBmi;
    private ArrayList<LocalDate> listLocalDatesXAxis;

    public PopupMeasurementDataGraphScale(Context context) {
        this.context=context;
        initDialog();
        getAllViews();
        initLineChartComponents();
    }

    //region dialog configuration methods
    private void getAllViews(){
        lineChartMeasurementDataScale=dialogPopupMeasurementDataGraphScale.findViewById(R.id.lineChartMeasurementDataBloodPressure);
        lineChartMeasurementDataScale.setVisibility(View.VISIBLE);
    }

    private void initDialog(){
        dialogPopupMeasurementDataGraphScale=new Dialog(context);
        dialogPopupMeasurementDataGraphScale.setContentView(R.layout.popup_measurement_data_graph_blood_pressure);
        dialogPopupMeasurementDataGraphScale.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        lineDataSetWeight=new LineDataSet(null, "weight");
        lineDataSetBmi=new LineDataSet(null, "bmi");

        settingsLineDataSet(lineDataSetWeight, Color.RED);
        settingsLineDataSet(lineDataSetBmi, Color.CYAN);

        lineChartMeasurementDataScale.getDescription().setText("");
        lineChartMeasurementDataScale.setScaleEnabled(false);
        lineChartMeasurementDataScale.getAxisRight().setEnabled(false);
    }
    //endregion

    private void formatXAxis(){
        XAxis xAxis=lineChartMeasurementDataScale.getXAxis();
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

    private ArrayList<Entry> getDataValues(ArrayList<ScaleMeasurement> measurements, MeasurementValueType measurementData){
        ArrayList<Entry> dataValues=new ArrayList<>();

        if(measurementData== MeasurementValueType.WEIGHT){
            for(int idx=0; idx<measurements.size(); idx++){
                listLocalDatesXAxis.add(measurements.get(idx).getDateOfMeasurement());
                dataValues.add(new Entry(idx, measurements.get(idx).getWeight()));
            }
        } else if(measurementData== MeasurementValueType.BMI){
            for(int idx=0; idx<measurements.size(); idx++){
                dataValues.add(new Entry(idx, measurements.get(idx).getBmi()));
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
        lineDataSetWeight.clear();
        lineDataSetBmi.clear();
        lineDataSets.clear();
    }

    public void showPopup(ArrayList<ScaleMeasurement> measurements, int idxChosenMeasurement) {
        ArrayList<Entry> entriesWeight=getDataValues(measurements, MeasurementValueType.WEIGHT);
        ArrayList<Entry> entriesBmi=getDataValues(measurements, MeasurementValueType.BMI);

        clearLineDataSets();
        fillLineDataSet(entriesWeight, lineDataSetWeight);
        fillLineDataSet(entriesBmi, lineDataSetBmi);
        lineDataSets.add(lineDataSetWeight);
        lineDataSets.add(lineDataSetBmi);
        lineData=new LineData(lineDataSets);
        lineChartMeasurementDataScale.setData(lineData);
        lineChartMeasurementDataScale.invalidate();
        lineChartMeasurementDataScale.setVisibleXRangeMaximum(3);
        Entry entryChosenMeasurementWeight=lineDataSetWeight.getEntryForIndex(idxChosenMeasurement);
        Entry entryChosenMeasurementBmi=lineDataSetBmi.getEntryForIndex(idxChosenMeasurement);
        Drawable drawableCircleChosenMeasurement= ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle_choosen_measurement, null);
        entryChosenMeasurementWeight.setIcon(drawableCircleChosenMeasurement);
        entryChosenMeasurementBmi.setIcon(drawableCircleChosenMeasurement);

        formatXAxis();
        dialogPopupMeasurementDataGraphScale.show();
    }

    public void cancelPopup(){
        dialogPopupMeasurementDataGraphScale.cancel();
    }
}
