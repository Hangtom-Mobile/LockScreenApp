package com.askhmer.mobileapp.activity;

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

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

import java.util.regex.Pattern;

public class PhoneNumber extends AppCompatActivity {

    private SharedPreferencesFile mSharedPrefrencesFile;
    private EditText etPhoneNum;
    private TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        etPhoneNum = (EditText)findViewById(R.id.et_phone_number);
        tvMsg = (TextView) findViewById(R.id.ve_phone_num);
        Button button = (Button)findViewById(R.id.bttn_next);

        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isValidMobile(etPhoneNum.getText().toString())){
                    tvMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidMobile(etPhoneNum.getText().toString())){
                    Intent i = new Intent(getApplicationContext(), Information.class);
                    mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE, etPhoneNum.getText().toString());
                    startActivity(i);
                }else {
                    tvMsg.setVisibility(View.VISIBLE);
                }
                
            }
        });
    }

    private boolean isValidMobile(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", etPhoneNum.getText().toString()))
        {
            if(phone.length() < 9 || phone.length() > 10)
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
}
