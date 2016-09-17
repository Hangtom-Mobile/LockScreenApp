package com.askhmer.lockscreen.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.GcmUtil;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;
import com.askhmer.lockscreen.utils.TokenGenerator;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RequestID extends AppCompatActivity{

    private int year, month, day;
    private Calendar calendar;
    private DatePicker myDatePicker;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_id);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        myDatePicker = (DatePicker) findViewById(R.id.my_date_picker);
        editText = (EditText) findViewById(R.id.e_name);

        myDatePicker.init(year-18,month,day,null);

        final Button button = (Button)findViewById(R.id.bttn_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMedayiID();
            }
        });
    }

    private String getDateFromDatePicker() {
        String sDOB, y, m, d;

        d = myDatePicker.getDayOfMonth() + "";
        m = myDatePicker.getMonth()+1 + "";
        y = myDatePicker.getYear() + "";

        String sMonth = m;
        String sDay = d;
        if (sMonth.length() == 1) {
            sMonth = "0" + sMonth;
        }
        if (sDay.length() == 1) {
            sDay = "0" + sDay;
        }
        sDOB = y + sMonth + sDay;
        return sDOB;
    }

    public void requestMedayiID() {
        /*loading message*/
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("login_respone", response);
                        pDialog.hide();
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (!jsonObj.getString("rst").equals("112")) {
                                    new SweetAlertDialog(RequestID.this, SweetAlertDialog.NORMAL_TYPE)
                                            .setTitleText("Welcome")
                                            .setContentText("Your Medayi ID: " + jsonObj.getString("rst"))
                                            .show();

                                }else if (jsonObj.getString("rst").equals("112")) {
                                    new SweetAlertDialog(RequestID.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Sorry...")
                                            .setContentText("You no have Medayi ID")
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(RequestID.this, "No data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*hide loading*/
                pDialog.hide();
                Toast.makeText(RequestID.this, "No Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mb_age", getDateFromDatePicker());
                params.put("mb_name", editText.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
