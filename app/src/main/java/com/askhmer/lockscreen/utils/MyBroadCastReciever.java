package com.askhmer.lockscreen.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.askhmer.lockscreen.model.ScreenListener;

/**
 * Created by soklundy on 6/30/2016.
 */
public class MyBroadCastReciever extends BroadcastReceiver {

    private ScreenListener screenListener;

    public  MyBroadCastReciever(Context context){
        this.screenListener= (ScreenListener) context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenListener.displayMessage();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

        }
    }
}