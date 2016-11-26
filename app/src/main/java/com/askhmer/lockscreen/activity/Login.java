package com.askhmer.lockscreen.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    private EditText editTextId, editTextPassword;
    private Button btnNext;
    private SharedPreferencesFile mSharedPrefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPrefer = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        editTextId = (EditText) findViewById(R.id.e_cash_id);
        editTextPassword = (EditText) findViewById(R.id.e_password);

        btnNext = (Button) findViewById(R.id.bttn_next);

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        final String regId = GCMRegistrar.getRegistrationId(this);

        if (regId.equals("")) {
            GCMRegistrar.register(this, GcmUtil.SENDER_ID);
        }

        /*next listener*/
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*loginPW();*/
                requestLogin();
            }
        });
    }

    public void requestLogin() {
        /*loading message*/
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("login_respone", response);
                        pDialog.hide();
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj.getString("rst").equals("110")) {
                                    mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NAME, jsonObj.getString("mb_name"));
                                    mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE, jsonObj.getString("mb_hp"));
                                    mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_AGE, jsonObj.getString("mb_birth"));
                                    mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_LOCATION, jsonObj.getString("mb_location"));
                                    mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_GENDER, jsonObj.getString("mb_sex"));
                                    mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID, jsonObj.getString("mb_id"));
                                    mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NATIONAL, jsonObj.getString("mb_national"));
                                    mSharedPrefer.putBooleanSharedPreference(SharedPreferencesFile.REGISTERNLOGIN, true);

                                    Intent intent = new Intent(getApplicationContext(), MainActivityTab.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    Login.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                                }else if (jsonObj.getString("rst").equals("112")) {
                                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Sorry...")
                                            .setContentText("Information incorrect!")
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(Login.this, "No data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*hide loading*/
                pDialog.hide();
                Toast.makeText(Login.this, "No Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String tokenId = new TokenGenerator().resultTokenId();
                mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN, tokenId);

                /*String cashId = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID);
                String password = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD);*/
                /*String dob = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_AGE);*/

                String cashId = editTextId.getText().toString();
                String password = editTextPassword.getText().toString();

                params.put("cash_slide_id", cashId);
                params.put("cash_password", password);
                params.put("token_id", tokenId);
                /*params.put("mb_age", dob);*/

                if (GCMRegistrar.isRegistered(Login.this)) {
                    params.put("gcm_id", GCMRegistrar.getRegistrationId(Login.this));
                }
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /*private void loginPW() {
        *//*loading message*//*
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.LOGINPW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        *//*hide loading*//*
                        pDialog.hide();
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj.getString("rst").equals("110")) {
                                    mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID, editTextId.getText().toString());
                                    mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD, editTextPassword.getText().toString());
                                    startActivity(new Intent(getApplicationContext(), LoginDob.class));
                                }else if (jsonObj.getString("rst").equals("112")) {
                                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Sorry...")
                                            .setContentText("ID is incorrect!")
                                            .show();
                                }else {
                                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Sorry...")
                                            .setContentText("Password is incorrect!")
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(Login.this, "No data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        *//*hide loading*//*
                        pDialog.hide();

                        Toast.makeText(Login.this, "No Connection", Toast.LENGTH_SHORT).show();
                    }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String cashId = editTextId.getText().toString();
                    String password = editTextPassword.getText().toString();

                    params.put("cash_slide_id", cashId);
                    params.put("cash_password", password);

                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }*/
}
