package com.askhmer.lockscreen.network;

import android.content.Context;
import android.net.NetworkInfo;

import com.askhmer.lockscreen.utils.NetworkUtil;

/**
 * Created by soklundy on 6/24/2016.
 */
public class CheckInternet {

    public boolean isConnect(Context context) {
        int networkType =  NetworkUtil.getNetworkType(context);
        NetworkInfo.State networkState = NetworkUtil.getNetworkState(context);

        if (networkType == -1 || networkState == NetworkInfo.State.UNKNOWN) {
            /*no internet*/
            return false;
        }
        return true;
    }

}
