package com.askhmer.lockscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Information extends AppCompatActivity {

    private TextView vePassId;
    private TextView veSlideId;
    private EditText editCashId;
    private EditText editPassword;
    private SharedPreferencesFile mSharedPrefrencesFile;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
        Button button = (Button)findViewById(R.id.bttn_next);
        editCashId  = (EditText)findViewById(R.id.e_cash_id);
        editPassword = (EditText)findViewById(R.id.e_password);
        vePassId = (TextView)findViewById(R.id.ve_pass_id);
        veSlideId = (TextView) findViewById(R.id.ve_slide_id);

        final EditText editTextPassword = (EditText)findViewById(R.id.password);
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String id = editTextPassword.getText().toString();
                if(editTextPassword.length() < 4) {
                    vePassId.setText(R.string.pwd_4_char);
                    vePassId.setVisibility(View.VISIBLE);
                }else {
                    vePassId.setVisibility(View.GONE);
                }
            }
        });

        /*Cash Slide id*/
        /*final EditText editTextCashSlideId = (EditText)findViewById(R.id.e_cash_id);
        editTextCashSlideId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String id = editTextCashSlideId.getText().toString();

                if (id.length() < 4 || id.contains(" ") || hasSymbol(id)) {
                    veSlideId.setVisibility(View.VISIBLE);
                } else {
                    veSlideId.setVisibility(View.GONE);
                }
            }
        });*/

         /*con password*/
        /*final EditText editTextPassword = (EditText)findViewById(R.id.e_password);
        final EditText editTextConPassword = (EditText)findViewById(R.id.e_con_password);
        editTextConPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = editTextPassword.getText().toString();
                String conPassword = editTextConPassword.getText().toString();

                if(conPassword.length() < 4) {
                    vePassId.setText(R.string.pwd_4_char);
                    vePassId.setVisibility(View.VISIBLE);
                }else {
                    vePassId.setVisibility(View.GONE);
                    if(!password.equals(conPassword)){
                        vePassId.setText(R.string.pwd_not);
                        vePassId.setVisibility(View.VISIBLE);
                    }else {
                        vePassId.setVisibility(View.GONE);
                    }
                }
            }
        });

        *//*password*//*
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = editTextPassword.getText().toString();
                String conPassword = editTextConPassword.getText().toString();

                if (password.length() < 4) {
                    vePassId.setText(R.string.pwd_4_char);
                    vePassId.setVisibility(View.VISIBLE);
                } else {
                    vePassId.setVisibility(View.GONE);
                    if (!password.equals(conPassword)) {
                        vePassId.setText(R.string.pwd_not);
                        vePassId.setVisibility(View.VISIBLE);
                    } else {
                        vePassId.setVisibility(View.GONE);
                    }
                }
            }
        });*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if ((vePassId.getVisibility() == View.GONE) && (veSlideId.getVisibility() == View.GONE)) {
                    if (editCashId.getText().toString().isEmpty()) {
                        veSlideId.setVisibility(View.VISIBLE);
                    }else if ((editPassword.getText().toString().isEmpty()) || (editTextConPassword.getText().toString().isEmpty())) {
                        vePassId.setVisibility(View.VISIBLE);
                    }else {
                        checkCashSlideId();
                    }
                }*/
                if (editTextPassword.getText().toString().isEmpty()) {
                    vePassId.setVisibility(View.VISIBLE);
                }else {
                    if (vePassId.getVisibility() == View.GONE) {
                        Intent i = new Intent(getApplicationContext(), Gender.class);
                        mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD, editTextPassword.getText().toString());
                        startActivity(i);
                        Information.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                    }
                }
            }
        });
    }

    public void checkCashSlideId(){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CHECKCASHSLIDEID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("112")) {
                            veSlideId.setText("Id already have");
                            veSlideId.setVisibility(View.VISIBLE);
                        }else {
                            Intent i = new Intent(getApplicationContext(), Name.class);
                            mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID, editCashId.getText().toString());
                            mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD, editPassword.getText().toString());
                            startActivity(i);
                            Information.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(Information.this)
                        .setTitleText("Sorry your phone no internet!")
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cash_slide_id", editCashId.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * this method if str have symbol will return true
     * @param str
     * @return
     */
    public boolean hasSymbol(String str){
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        return p.matcher(str).find();
    }
}
