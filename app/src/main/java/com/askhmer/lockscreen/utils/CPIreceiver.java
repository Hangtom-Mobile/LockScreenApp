package com.askhmer.lockscreen.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.lockscreen.network.MySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by soklundy on 9/3/2016.
 */
public class CPIreceiver extends BroadcastReceiver {

    private String packageName, installPrice, uId;


    public CPIreceiver(String packageName, String installPrice, String uId) {
        this.packageName = packageName;
        this.installPrice = installPrice;
        this.uId = uId;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String packageName = intent.getData().getSchemeSpecificPart();
        if (packageName.equals(packageName)) {
            requestInstallPoint(context);
        }
        context.stopService(new Intent(context, CPIservice.class));
    }

    public void requestInstallPoint(final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://m.medayi.com/locknet/locknet_google_point.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("message", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferencesFile sharedPreferencesFile = new SharedPreferencesFile(context,SharedPreferencesFile.FILE_INFORMATION_TEMP);
                params.put("cash_slide_id", sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                params.put("cash_password", sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD));
                params.put("token_id", sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN));
                params.put("uid", uId);
                params.put("lock_view_price", installPrice);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
