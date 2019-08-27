package com.example.healthx.pkgViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ProgressBar;

import com.example.healthx.R;
import com.example.healthx.pkgManager.CountdownManager;

public class PopupConnectingCountdown {
    private Context context;
    private Dialog dialogPopup;
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

    //region dialog configuration methods
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
    //endregion

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
