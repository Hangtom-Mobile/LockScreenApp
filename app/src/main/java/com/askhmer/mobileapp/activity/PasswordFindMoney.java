package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class PasswordFindMoney extends SwipeBackActivity {

    private EditText password;
    private TextView validatePWD;
    private Button btnNext;

    private String inPutPassword;
    private Intent in;
    private SharedPreferencesFile mSharedPreferencesFile;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_find_money);
//swipe back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        mSharedPreferencesFile = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//  init
        password = (EditText) findViewById(R.id.et_password);
        validatePWD = (TextView) findViewById(R.id.ve_password);
        btnNext = (Button) findViewById(R.id.btn_next);

//  get intent extra
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                className= null;
            } else {
                className= extras.getString("class");
            }
        } else {
            className= (String) savedInstanceState.getSerializable("class");
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPasswordFromServer();
            }
        });

    }

    public void checkPasswordFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CHECKPASSWORK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("110")) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (className.equals("MyInfo")) {
                                    in = new Intent(PasswordFindMoney.this, MyInfo.class);
                                    startActivity(in);
                                }else if(className.equals("MoneyBalance")){
                                    in = new Intent(PasswordFindMoney.this, MoneyBalance.class);
                                    startActivity(in);
                                }else if(className.equals("ChangPwd")){
                                    in = new Intent(PasswordFindMoney.this, ChangePwd.class);
                                    in.putExtra("password_SS", jsonObj.getString("password_SS"));
                                    startActivity(in);
                                }
                                PasswordFindMoney.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            validatePWD.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PasswordFindMoney.this, "No internet connection!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cash_slide_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                params.put("cash_password", password.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
