package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPassword extends AppCompatActivity {

    private Button btnNext;
    private TextView txtPassword, txtComPassword, vTextView;
    private SharedPreferencesFile mSharedPreferencesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        txtComPassword = (TextView) findViewById(R.id.et_con_password);
        txtPassword = (TextView) findViewById(R.id.et_password);
        vTextView = (TextView) findViewById(R.id.ve_password);
        btnNext = (Button) findViewById(R.id.btn_next);
        mSharedPreferencesFile = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordForget();
            }
        });

    }

    public void changePasswordForget() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CHANGPASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String result = jsonObj.getString("rst");
                                if (result.equals("110")) {
                                    Toast.makeText(NewPassword.this, "Reset password Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), AccountManage.class));
                                    finish();
                                }else {
                                    vTextView.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Intent intent = getIntent();
                params.put("cash_slide_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                params.put("cash_password", txtPassword.getText().toString());
                params.put("cash_password_re", txtComPassword.getText().toString());
                params.put("pass_secret_key",intent.getStringExtra("secret_key"));
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
