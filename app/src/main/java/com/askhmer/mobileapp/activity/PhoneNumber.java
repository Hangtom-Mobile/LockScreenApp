package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

public class PhoneNumber extends AppCompatActivity {

    private SharedPreferencesFile mSharedPrefrencesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        final EditText editText = (EditText)findViewById(R.id.et_phone_number);
        Button button = (Button)findViewById(R.id.bttn_next);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Information.class);
                mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE, editText.getText().toString());
                startActivity(i);
            }
        });
    }
}
