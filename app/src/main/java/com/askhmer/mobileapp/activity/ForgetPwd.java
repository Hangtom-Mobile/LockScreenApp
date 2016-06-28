package com.askhmer.mobileapp.activity;

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
import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.network.API;
import com.askhmer.mobileapp.network.MySingleton;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPwd extends SwipeBackActivity {

    private Button ok;
    private Intent in;
    private SharedPreferencesFile mSharedPreferencesFile;
    private TextView txtCashSlideId, txtPhoneNum, txtVori;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
//swipe back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        mSharedPreferencesFile = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
        txtCashSlideId = (TextView) findViewById(R.id.et_user_id);
        txtPhoneNum = (TextView) findViewById(R.id.et_phone_number);
        txtVori = (TextView) findViewById(R.id.txt_veri_incor);

        ok = (Button) findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPasswordFromServer();
            }
        });
    }

    public void forgetPasswordFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.LOSEPASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String result = jsonObj.getString("rst");
                                if (result.equals("111")) {
                                    txtVori.setVisibility(View.VISIBLE);
                                }else {
                                    in = new Intent(ForgetPwd.this, NewPassword.class);
                                    in.putExtra("secret_key",result);
                                    startActivity(in);
                                    ForgetPwd.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
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
                params.put("cash_slide_id", txtCashSlideId.getText().toString());
                params.put("phone_num", txtPhoneNum.getText().toString());
                params.put("token_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN));
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
