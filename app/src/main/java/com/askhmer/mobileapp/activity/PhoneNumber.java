package com.askhmer.mobileapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.model.Reciver;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;
import com.askhmer.mobileapp.utils.SmsBroadcastReceiver;

import java.util.regex.Pattern;

public class PhoneNumber extends AppCompatActivity implements Reciver {

    private SharedPreferencesFile mSharedPrefrencesFile;
    private EditText etPhoneNum;
    private EditText verifyNumber;
    private TextView tvMsg;
    private Button btnApply;
    private Button btnConfirm;
    private String randomNumber;
    private BroadcastReceiver mybroadcast;
    private TextView waitMsg;
    private String formatedPhNumber;
    private RelativeLayout layoutConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        init();

        etPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isValidMobile(etPhoneNum.getText().toString())){
                    tvMsg.setVisibility(View.GONE);
                }
            }
        });

        
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidMobile(etPhoneNum.getText().toString())) {
                    int randomPIN = (int) (Math.random() * 9000) + 1000;
                    randomNumber = randomPIN+"";
                    String unformatePhNumber = etPhoneNum.getText().toString();
                    formatedPhNumber = unformatePhNumber.replaceAll("[^\\.0123456789]", "");

                    String ind = String.valueOf(formatedPhNumber.charAt(0));

                    waitMsg.setVisibility(View.VISIBLE);
                    layoutConfirm.setVisibility(View.VISIBLE);

                    if (ind.equals("0")) {
                        String fulPhoneNum = "855" + formatedPhNumber.substring(1);
                        sendSMS(fulPhoneNum, randomNumber);
                    } else {
                        String fulPhoneNum = "855" + formatedPhNumber;
                        sendSMS(fulPhoneNum, randomNumber);
                    }

                } else {
                    tvMsg.setVisibility(View.VISIBLE);
                }

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etVerifyNumber = verifyNumber.getText().toString();

                if(etVerifyNumber.equals(randomNumber)){
                    mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE, formatedPhNumber);
                    Intent i = new Intent(getApplicationContext(), Information.class);
                    startActivity(i);
                    PhoneNumber.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                }else Toast.makeText(PhoneNumber.this, "Wrong number", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isValidMobile(String phone)
    {
        String formatedPhNumber = etPhoneNum.getText().toString();
        String newPhNumber = phone.replaceAll("[^\\.0123456789]","");
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", formatedPhNumber))
        {
            if(newPhNumber.length() < 9 || newPhNumber.length() > 10)
            {
                check = false;
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check=false;
        }
        return check;
//        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void init(){
        etPhoneNum = (EditText)findViewById(R.id.et_phone_number);
        tvMsg = (TextView) findViewById(R.id.ve_phone_num);
        btnApply = (Button)findViewById(R.id.bttn_next);
        verifyNumber = (EditText) findViewById(R.id.et_confirm);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        waitMsg = (TextView) findViewById(R.id.tv_wait_msg);
        layoutConfirm = (RelativeLayout) findViewById(R.id.layout_confirm);
    }


    //send SMS to client
    public void sendSMS(final String receiver,String verifycode){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://chat.askhmer.com/api/verify/phone_number/"+ receiver+"/"+verifycode;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("{\"STATUS\":200}")) {
                            Toast.makeText(PhoneNumber.this, "request sucessed  :" + response, Toast.LENGTH_SHORT).show();
                            Log.d("respone", response);
                        } else {
                            Toast.makeText(PhoneNumber.this, "request failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void setTextToTextView(String text) {
        verifyNumber.setText(text);
        waitMsg.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mybroadcast = new SmsBroadcastReceiver(PhoneNumber.this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mybroadcast, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mybroadcast);
    }

}
