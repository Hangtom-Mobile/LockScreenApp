package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.network.API;
import com.askhmer.mobileapp.network.MySingleton;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;
import com.askhmer.mobileapp.utils.TokenGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginDob extends AppCompatActivity{

    private SharedPreferencesFile mSharedPrefrencesFile;
    private int year, month, day;
    private Calendar calendar;
    private String sDOB, y, m, d;
    private DatePicker myDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dob_login);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.FILE_INFORMATION_TEMP);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        myDatePicker = (DatePicker) findViewById(R.id.my_date_picker);
//        EditText textD = (EditText) myDatePicker.getChildAt(1);
//        textD.setTextSize(50);

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
                mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_AGE, sDOB);

                String tokenId = new TokenGenerator().resultTokenId();
                mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN, tokenId);

                requestLogin();
            }
        });
    }

    public void requestLogin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("login_respone", response);
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj.getString("rst").equals("110")) {
                                    mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NAME, jsonObj.getString("mb_name"));
                                    mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE, jsonObj.getString("mb_hp"));
                                    mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_AGE, jsonObj.getString("mb_birth"));
                                    mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_LOCATION, jsonObj.getString("mb_location"));
                                    mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_GENDER, jsonObj.getString("mb_sex"));
                                    mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID, jsonObj.getString("mb_id"));
                                    mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NATIONAL, jsonObj.getString("mb_national"));
                                    mSharedPrefrencesFile.putBooleanSharedPreference(SharedPreferencesFile.REGISTERNLOGIN, true);

                                    Intent intent = new Intent(getApplicationContext(), MainActivityTab.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    LoginDob.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                                }else {
                                    new SweetAlertDialog(LoginDob.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Sorry...")
                                            .setContentText("Information are incorrect!")
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(LoginDob.this, "No data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginDob.this, "No Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String cashId = mSharedPrefrencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID);
                String password = mSharedPrefrencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD);
                String dob = mSharedPrefrencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_AGE);
                String tokenId = mSharedPrefrencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN);

                params.put("cash_slide_id", cashId);
                params.put("cash_password", password);
                params.put("token_id", tokenId);
                params.put("mb_age", dob);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
