package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Location extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner location;
    private SharedPreferencesFile mSharedPreferencesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPreferencesFile = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.FILE_INFORMATION_TEMP);
        Button button =  (Button)findViewById(R.id.bttn_next);
        location = (Spinner) findViewById(R.id.sp_location);
        location.setPrompt("Select year location!!!");

        // Creating adapter for spinner
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.list_province, android.R.layout.simple_spinner_item);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        location.setAdapter(dataAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_LOCATION, String.valueOf(location.getSelectedItemId() + 1));
                submitToServer();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Showing selected spinner item
        Toast.makeText(view.getContext(), "Selected: " +id, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void submitToServer() {
        final String phone_num = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE);
        final String cash_slide_id = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID);
        final String cash_password = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD);
        final String mb_name = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NAME);
        final String mb_sex = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_GENDER);
        final String mb_age  = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_AGE);
        final String mb_national  = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NATIONAL);
        final String mb_location  = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_LOCATION);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("110")) {
                            mSharedPreferencesFile.putBooleanSharedPreference(SharedPreferencesFile.IS_OPEN_INFORMATION_SCREEN_KEY, true);
                            Intent intent = new Intent(getApplicationContext(), MainActivityTab.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);;
                            startActivity(intent);
                            finish();
                            Location.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(Location.this)
                        .setTitleText("Sorry your phone no internet!")
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String tokenId = new TokenGenerator().resultTokenId();
                mSharedPreferencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN,tokenId);
                params.put("phone_num", phone_num);
                params.put("cash_slide_id", cash_slide_id);
                params.put("cash_password", cash_password);
                params.put("mb_name", mb_name);
                params.put("mb_sex", mb_sex);
                params.put("mb_age", mb_age);
                params.put("mb_national", mb_national);
                params.put("mb_location", mb_location);
                params.put("token_id",tokenId);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
