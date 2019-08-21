package com.example.health_data_transfer_v1.pkgViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.health_data_transfer_v1.R;
import com.example.health_data_transfer_v1.pkgManager.CountdownManager;

public class PopupConnectingCountdown extends AppCompatActivity {
    private Context context;
    private Dialog dialogPopup;
    private TextView txtConnectingCountdown;
    private ProgressBar progressBarConnecting;
    private ProgressBarAnimation progressBarAnimationConnecting;
    private CountdownManager countdownManagerConnecting;

    public PopupConnectingCountdown(Context context) {
        this.context=context;
        initDialog();
        getAllViews();
        initAnimation();
        initManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getAllViews(){
        progressBarConnecting=dialogPopup.findViewById(R.id.progressbarConnectingCountdown);
    }

    private void initDialog(){
        dialogPopup=new Dialog(context);
        dialogPopup.setContentView(R.layout.popup_connecting_countdown);
        dialogPopup.setCancelable(false);
        dialogPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initAnimation(){
        progressBarAnimationConnecting=new ProgressBarAnimation(progressBarConnecting, 100, 0);
        progressBarAnimationConnecting.setDuration(10100);
    }

    private void initManager(){
        countdownManagerConnecting=new CountdownManager(progressBarConnecting, progressBarAnimationConnecting);
    }

    public void showPopup() {
        dialogPopup.show();
        countdownManagerConnecting.startCountdownConnecting();
        progressBarConnecting.startAnimation(progressBarAnimationConnecting);
    }

    public void cancelPopup(){
        dialogPopup.cancel();
        countdownManagerConnecting.stopCountdownConnecting();
        progressBarConnecting.clearAnimation();
    }
}
