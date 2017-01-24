package com.askhmer.lockscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePwd extends SwipeBackActivity {

    private SharedPreferencesFile mSharedPreferencesFile;
    private TextView txtPassword, txtComPassword, vTextView;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        //swipe back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        mSharedPreferencesFile = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
        btnOk = (Button) findViewById(R.id.btn_next);
        txtComPassword = (TextView) findViewById(R.id.et_confirm_password);
        txtPassword = (TextView) findViewById(R.id.et_new_password);
        vTextView = (TextView) findViewById(R.id.ve_password);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    public void changePassword() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CHANGPASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String result = jsonObj.getString("rst");
                                if (result.equals("110")) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.success_pass), Toast.LENGTH_SHORT).show();
                                    mSharedPreferencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD, txtPassword.getText().toString());
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
                Toast.makeText(getApplication(), "ForgetPwd Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Intent intent = getIntent();
                params.put("cash_slide_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                params.put("cash_password", txtPassword.getText().toString());
                params.put("cash_password_re", txtComPassword.getText().toString());
                params.put("pass_secret_key",intent.getStringExtra("password_SS"));
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
