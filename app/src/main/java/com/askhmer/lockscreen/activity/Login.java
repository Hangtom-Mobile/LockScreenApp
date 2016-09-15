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
import com.askhmer.lockscreen.utils.SharedPreferencesFile;
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

        /*next listener*/
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPW();
            }
        });
    }

    private void loginPW() {
        /*loading message*/
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.LOGINPW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*hide loading*/
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
                        /*hide loading*/
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
    }
}
