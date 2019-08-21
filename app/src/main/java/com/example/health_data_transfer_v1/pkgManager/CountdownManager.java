package com.example.health_data_transfer_v1.pkgManager;

import android.os.CountDownTimer;
import android.widget.ProgressBar;

import com.example.health_data_transfer_v1.pkgViews.ProgressBarAnimation;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CountdownManager {
    private static final long START_TIME_IN_MILLIS_CONNECTING=10100;
    private static final long START_TIME_IN_MILLIS_ALERT_DIALOG=3100;
    private CountDownTimer countDownTimerConnecting;
    private CountDownTimer countDownTimerAlertDialog;
    private long timeLeftInMillisConnecting=START_TIME_IN_MILLIS_CONNECTING;
    private long timeLeftInMillisAlertDialog=START_TIME_IN_MILLIS_ALERT_DIALOG;
    private ProgressBar progressBarConnecting;
    private ProgressBarAnimation progressBarAnimationConnecting;
    private SweetAlertDialog currentSweetAlertDialog;

    public CountdownManager(ProgressBar progressBarConnecting, ProgressBarAnimation progressBarAnimationConnecting){
        this.progressBarConnecting=progressBarConnecting;
        this.progressBarAnimationConnecting=progressBarAnimationConnecting;
        configureCountdownConnecting();
    }

    public CountdownManager(){
        configureCountdownAlertDialog();
    }

    //region countdown configuration methods
    private void configureCountdownConnecting(){
        countDownTimerConnecting=new CountDownTimer(timeLeftInMillisConnecting, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillisConnecting=millisUntilFinished;
            }

            @Override
            public void onFinish() {
            }
        };
    }

    private void configureCountdownAlertDialog(){
        countDownTimerAlertDialog=new CountDownTimer(timeLeftInMillisAlertDialog, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillisAlertDialog=millisUntilFinished;

                if(!currentSweetAlertDialog.isShowing()){
                    stopCountdownAlertDialog();
                }
            }

            @Override
            public void onFinish() {
                if(currentSweetAlertDialog.isShowing()){
                    currentSweetAlertDialog.dismiss();
                }
            }
        };
    }
    //endregion

    //region countdown interaction methods
    public void startCountdownConnecting(){
        resetCountdownConnecting();
        progressBarConnecting.startAnimation(progressBarAnimationConnecting);
        countDownTimerConnecting.start();
    }

    public void startCountdownAlertDialog(final SweetAlertDialog sweetAlertDialog){
        resetCountdownAlertDialog();
        currentSweetAlertDialog=sweetAlertDialog;
        countDownTimerAlertDialog.start();
    }

    public void stopCountdownConnecting(){
        countDownTimerConnecting.cancel();
    }

    public void stopCountdownAlertDialog(){
        countDownTimerAlertDialog.cancel();
    }

    private void resetCountdownConnecting(){
        timeLeftInMillisConnecting=START_TIME_IN_MILLIS_CONNECTING;
    }

    private void resetCountdownAlertDialog(){
        timeLeftInMillisAlertDialog=START_TIME_IN_MILLIS_ALERT_DIALOG;
    }
    //endregion
}
