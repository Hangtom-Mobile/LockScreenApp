package com.askhmer.lockscreen.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by soklundy on 9/3/2016.
 */
public class CPIreceiver extends BroadcastReceiver {

    private String packageName;

    public CPIreceiver(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String packageName = intent.getData().getSchemeSpecificPart();
        if (packageName.equals(packageName)) {
            Log.e("show","work_broadcast");
        }
        context.stopService(new Intent(context, CPIservice.class));
    }
}
