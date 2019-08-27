package com.example.healthx.pkgManager;

import android.app.Activity;

import com.ivy.ivyconnect.util.Error;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AlertManager {
    private Activity activity;
    private SweetAlertDialog alertDialogConnected;
    private SweetAlertDialog alertDialogConnectionFailed;
    private SweetAlertDialog alertDialogDisconnected;
    private SweetAlertDialog alertDialogReconnecting;
    private SweetAlertDialog alertDialogReconnectingFailed;
    private SweetAlertDialog alertDialogReconnected;
    private SweetAlertDialog alertDialogMeasurementError;
    private SweetAlertDialog alertDialogGPS;
    private SweetAlertDialog alertDialogGraphMeasurement;
    private CountdownManager countdownManagerAlertDialog;
    private SweetAlertDialog.OnSweetClickListener onSweetClickListener;

    public AlertManager(Activity activity){
        this.activity=activity;
        initOtherThings();
    }

    private void initOtherThings(){
        countdownManagerAlertDialog=new CountdownManager();
    }

    public void setOnSweetClickListener(SweetAlertDialog.OnSweetClickListener onSweetClickListener){
        this.onSweetClickListener=onSweetClickListener;
    }

    //region configure alert methods
    private void configureAlertDialogConnected(){
        alertDialogConnected=new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Connected")
                .hideConfirmButton();
    }

    private void configureAlertDialogConnectionFailed(){
        alertDialogConnectionFailed=new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Connection failed")
                .hideConfirmButton();
    }

    private void configureAlertDialogDisconnected(){
        alertDialogDisconnected=new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Disconnected")
                .hideConfirmButton();
    }

    private void configureAlertDialogReconnecting(){
        alertDialogReconnecting=new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Reconnecting")
                .hideConfirmButton();
    }

    private void configureAlertDialogReconnectingFailed(){
        alertDialogReconnectingFailed=new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Reconnecting failed")
                .hideConfirmButton();
    }

    private void configureAlertDialogReconnected(){
        alertDialogReconnected=new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Reconnected")
                .hideConfirmButton();
    }

    private void configureAlertDialogMeasurementError(){
        alertDialogMeasurementError=new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                .hideConfirmButton();
    }

    private void configureAlertDialogGPS(){
        alertDialogGPS=new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Please turn GPS on!")
                .setConfirmClickListener(onSweetClickListener);

        alertDialogGPS.setCanceledOnTouchOutside(false);
    }

    private void configureAlertDialogGraphMeasurement(){
        alertDialogGraphMeasurement=new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("There must be at least two measurement entries for showing the chart!")
                .hideConfirmButton();
    }
    //endregion

    //region show alert methods
    public void showAlertDialogConnected(){
        configureAlertDialogConnected();
        alertDialogConnected.show();
        countdownManagerAlertDialog.startCountdownAlertDialog(alertDialogConnected);
    }

    public void showAlertDialogConnectionFailed(){
        configureAlertDialogConnectionFailed();
        alertDialogConnectionFailed.show();
        countdownManagerAlertDialog.startCountdownAlertDialog(alertDialogConnectionFailed);
    }

    public void showAlertDialogDisconnected(){
        configureAlertDialogDisconnected();
        alertDialogDisconnected.show();
        countdownManagerAlertDialog.startCountdownAlertDialog(alertDialogDisconnected);
    }

    public void showAlertDialogReconnecting(){
        configureAlertDialogReconnecting();
        alertDialogReconnecting.show();
    }

    public void showAlertDialogReconnectingFailed(){
        configureAlertDialogReconnectingFailed();
        alertDialogReconnectingFailed.show();
        countdownManagerAlertDialog.startCountdownAlertDialog(alertDialogReconnectingFailed);
    }

    public void showAlertDialogReconnected(){
        configureAlertDialogReconnected();
        alertDialogReconnected.show();
        countdownManagerAlertDialog.startCountdownAlertDialog(alertDialogReconnected);
    }

    public void showAlertDialogMeasurementError(Error error){
        configureAlertDialogMeasurementError();
        alertDialogMeasurementError.setTitle("Measurement error: " + error.toString());
        alertDialogMeasurementError.show();
        countdownManagerAlertDialog.startCountdownAlertDialog(alertDialogMeasurementError);
    }

    public void showAlertDialogGPS(){
        configureAlertDialogGPS();
        alertDialogGPS.show();
    }

    public void showAlertDialogGraphMeasurement(){
        configureAlertDialogGraphMeasurement();
        alertDialogGraphMeasurement.show();
        countdownManagerAlertDialog.startCountdownAlertDialog(alertDialogGraphMeasurement);
    }
    //endregion

    //region cancel alert methods
    public void cancelAlertDialogReconnecting(){
        if(alertDialogReconnecting.isShowing()){
            alertDialogReconnecting.cancel();
        }
    }
    //endregion
}
