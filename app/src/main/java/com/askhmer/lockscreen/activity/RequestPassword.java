package com.askhmer.lockscreen.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.model.Reciver;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.SmsBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestPassword extends AppCompatActivity implements Reciver{

    private EditText etPhoneNum, etMedayiID;
    private EditText verifyNumber;
    private TextView tvMsg, txtCode;
    private Button btnApply;
    private Button btnConfirm;
    private String randomNumber, code;
    private BroadcastReceiver mybroadcast;
    private TextView waitMsg;
    private String formatedPhNumber;
    private RelativeLayout layoutConfirm;
    private Spinner countryCode;
    private CountDownTimer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        init();

        countryCode.setPrompt("Select your country");

        // Creating adapter for spinner
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.country, android.R.layout.simple_spinner_item);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        countryCode.setAdapter(dataAdapter);

        /*listener*/
        countryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = parent.getItemAtPosition(pos)+"";
                if (item.equalsIgnoreCase(getResources().getString(R.string.cam))){
                    code = "855";
                }else {
                    code = "82";
                }
                txtCode.setText("+"+code);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                    randomNumber = randomPIN + "";
                    String unformatePhNumber = etPhoneNum.getText().toString();
                    formatedPhNumber = unformatePhNumber.replaceAll("[^\\.0123456789]", "");

                    String ind = String.valueOf(formatedPhNumber.charAt(0));

                    waitMsg.setVisibility(View.VISIBLE);
                    layoutConfirm.setVisibility(View.VISIBLE);

                    if (ind.equals("0")) {
                        etMedayiID.setText(formatedPhNumber);
                        String fulPhoneNum = code + "_" + formatedPhNumber.substring(1);
                        sendSMS(fulPhoneNum, randomNumber, etMedayiID.getText().toString());
                    } else {
                        etMedayiID.setText("0" + formatedPhNumber);
                        String fulPhoneNum = code + "_" + formatedPhNumber;
                        sendSMS(fulPhoneNum, randomNumber, etMedayiID.getText().toString());
                    }

                    setButtonApply(false, R.color.hintColor);
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
                    Intent i = new Intent(getApplicationContext(), NewPasswordForFind.class);
                    i.putExtra("cash_id", etMedayiID.getText().toString());
                    i.putExtra("secret_code",etVerifyNumber);
                    startActivity(i);
                    RequestPassword.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                }else {
                    Toast.makeText(RequestPassword.this, "Wrong code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidMobile(String phone)
    {
        String formatedPhNumber = etPhoneNum.getText().toString();
        String newPhNumber = phone.replaceAll("[^\\.0123456789]", "");
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", formatedPhNumber))
        {
            if(newPhNumber.length() < 8 || newPhNumber.length() > 10)
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
        countryCode = (Spinner) findViewById(R.id.sp_location);
        txtCode = (TextView) findViewById(R.id.tv_code);
        etMedayiID = (EditText) findViewById(R.id.e_medayi_id);
    }


    //send SMS to client
    public void sendSMS(final String receiver, final String verifycode, final String cashId){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        /*count down message */
        waitingMessage(R.string.msg_wait);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTPASSWORDSTEP1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("result", response);
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String result = jsonObj.getString("rst");
                                if (result.equals("112")) {
                                    displayMessage(R.string.msg_id_no_reg);
                                }else if (result.equals("113")){
                                    displayMessage(R.string.msg_num_not_sam_id);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error_testing", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.e("view_testing2",receiver + "   " + verifycode);
                params.put("cash_slide_id", cashId);
                params.put("verifyCode", verifycode);
                params.put("phone", receiver);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void setTextToTextView(String text) {
        verifyNumber.setText(text);
        waitMsg.setVisibility(View.GONE);
        cancelCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mybroadcast = new SmsBroadcastReceiver(RequestPassword.this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mybroadcast, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mybroadcast);
        cancelCount();
    }

    private void waitingMessage(final int resouId) {
        if (resouId == R.string.registered) {
            waitMsg.setText(getResources().getString(resouId));
            waitMsg.setVisibility(View.VISIBLE);
        }else {
            count = new CountDownTimer(120000, 1000) {
                public void onTick(long millisUntilFinished) {
                    waitMsg.setText(getResources().getString(resouId) +" "+ millisUntilFinished / 1000 + " seconds");
                }

                public void onFinish() {
                    waitMsg.setVisibility(View.GONE);
                    setButtonApply(true, R.drawable.btn_selector);
                }
            };

            count.start();
        }
    }

    /*this method use to set button disable or not*/
    private void setButtonApply(boolean isEnabled, int color) {
        btnApply.setEnabled(isEnabled);
        btnApply.setBackgroundResource(color);
    }

    private void displayMessage(int stringMessage) {
        cancelCount();
        waitMsg.setText(getResources().getString(stringMessage));
        waitMsg.setVisibility(View.VISIBLE);
        setButtonApply(true, R.drawable.btn_selector);
    }

    private void cancelCount() {
        if (count != null) {
            count.cancel();
        }
    }
}
