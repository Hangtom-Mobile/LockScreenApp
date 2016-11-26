package com.askhmer.lockscreen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Age extends AppCompatActivity{

    private SharedPreferencesFile mSharedPrefrencesFile;
    private int year, month, day;
    private Calendar calendar;
    private String sDOB, y, m, d;
    private DatePicker myDatePicker;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        context = Age.this;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        myDatePicker = (DatePicker) findViewById(R.id.my_date_picker);

        myDatePicker.init(year-18,month,day,null);

        final Button button = (Button)findViewById(R.id.bttn_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

               /* Toast.makeText(Age.this, sDOB, Toast.LENGTH_SHORT).show();*/
                /*Intent intent = new Intent(getApplicationContext(), National.class);
                mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_AGE, sDOB);
                startActivity(intent);
                Age.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);*/
                submitToServer();
            }
        });

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        if (GCMRegistrar.isRegistered(this)) {
            Log.d("GCM: ", GCMRegistrar.getRegistrationId(this));
        }

        final String regId = GCMRegistrar.getRegistrationId(this);

        if (regId.equals("")) {
            GCMRegistrar.register(this, GcmUtil.SENDER_ID);
            Log.d("GCM: ", "Registration id :  "+GCMRegistrar.getRegistrationId(this));
        }
        else {
            Log.d("info", "already registered as" + regId);
        }
    }
/*
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            MyDatePickerDialog mDatePicker = new MyDatePickerDialog(this,myDateListener,year -18, month, day);
            mDatePicker.setPermanentTitle("Your date of birth");
            mDatePicker.getDatePicker().setCalendarViewShown(false);

//            return new DatePickerDialog(this, myDateListener, year, month, day);
            return mDatePicker;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        String sMonth = month+"";
        String sDay = day+"";
        if(sMonth.length()== 1){
            sMonth = "0"+sMonth;
        }
        if(sDay.length()== 1){
            sDay = "0"+sDay;
        }
        sDOB = new StringBuilder().append(year).append(sMonth).append(sDay)+"";
        etDOB.setText("" + new StringBuilder().append(year).append("/").append(sMonth).append("/").append(sDay), TextView.BufferType.NORMAL);
    }
 */

    public void submitToServer() {
        final String phone_num = mSharedPrefrencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE);
        /*final String cash_slide_id = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID);*/
        final String cash_password = mSharedPrefrencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD);
        /*final String mb_name = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NAME);*/
        final String mb_sex = mSharedPrefrencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_GENDER);
        /*final String mb_age  = mSharedPrefrencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_AGE);*/
        /*final String mb_national  = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NATIONAL);
        final String mb_location  = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_LOCATION);*/

        mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID, phone_num);
        mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NAME, phone_num);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("110")) {
                            mSharedPrefrencesFile.putBooleanSharedPreference(SharedPreferencesFile.REGISTERNLOGIN, true);
                            Intent intent = new Intent(getApplicationContext(), MainActivityTab.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);;
                            startActivity(intent);
                            finish();
                            Age.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(Age.this)
                        .setTitleText("Sorry your phone no internet!")
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String tokenId = new TokenGenerator().resultTokenId();
                mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN,tokenId);
                params.put("phone_num", phone_num);
                params.put("cash_slide_id", phone_num);
                params.put("cash_password", cash_password);
                params.put("mb_name", phone_num);
                params.put("mb_sex", mb_sex);
                params.put("mb_age", sDOB);
                params.put("mb_national", "");
                params.put("mb_location", "");
                params.put("token_id",tokenId);

                if (GCMRegistrar.isRegistered(context)) {
                    params.put("gcm_id", GCMRegistrar.getRegistrationId(context));
                }
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
