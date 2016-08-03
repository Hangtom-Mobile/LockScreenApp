package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

public class Login extends AppCompatActivity {

    private EditText editTextId, editTextPassword;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferencesFile mSharedPrefer = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        editTextId = (EditText) findViewById(R.id.e_cash_id);
        editTextPassword = (EditText) findViewById(R.id.e_password);

        btnNext = (Button) findViewById(R.id.bttn_next);

        /*next listener*/
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID, editTextId.getText().toString());
                mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD, editTextPassword.getText().toString());
                startActivity(new Intent(getApplicationContext(),LoginDob.class));
            }
        });
    }
}
