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

public class Information extends AppCompatActivity {

    TextView vePassId;
    TextView veSlideId;
    private SharedPreferencesFile mSharedPrefrencesFile;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.FILE_INFORMATION_TEMP);
        Button button = (Button)findViewById(R.id.bttn_next);
        final EditText editCashId = (EditText)findViewById(R.id.e_cash_id);
        final EditText editPassword = (EditText)findViewById(R.id.e_password);
        vePassId = (TextView)findViewById(R.id.ve_pass_id);
        veSlideId = (TextView) findViewById(R.id.ve_slide_id);

        /*Cash Slide id*/
        final EditText editTextCashSlideId = (EditText)findViewById(R.id.e_cash_id);
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

                if (id.length() < 4) {
                    veSlideId.setVisibility(View.VISIBLE);
                } else {
                    veSlideId.setVisibility(View.GONE);
                }
            }
        });

         /*con password*/
        final EditText editTextPassword = (EditText)findViewById(R.id.e_password);
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

                if(conPassword.length() < 8) {
                    vePassId.setText("Password at least 8 characters");
                    vePassId.setVisibility(View.VISIBLE);
                }else {
                    vePassId.setVisibility(View.GONE);
                    if(!password.equals(conPassword)){
                        vePassId.setText("Password and Confirm password not match");
                        vePassId.setVisibility(View.VISIBLE);
                    }else {
                        vePassId.setVisibility(View.GONE);
                    }
                }
            }
        });

        /*password*/
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

                if (password.length() < 8) {
                    vePassId.setText("Password at least 8 characters");
                    vePassId.setVisibility(View.VISIBLE);
                } else {
                    vePassId.setVisibility(View.GONE);
                    if (!password.equals(conPassword)) {
                        vePassId.setText("Password and Confirm password not match");
                        vePassId.setVisibility(View.VISIBLE);
                    } else {
                        vePassId.setVisibility(View.GONE);
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((vePassId.getVisibility() == View.GONE) && (veSlideId.getVisibility() == View.GONE)) {
                    if ((editCashId.getText().toString().isEmpty()) && (editPassword.getText().toString().isEmpty()) && (editTextConPassword.getText().toString().isEmpty())) {
                        veSlideId.setVisibility(View.VISIBLE);
                        vePassId.setVisibility(View.VISIBLE);
                    }else {
                        Intent i = new Intent(getApplicationContext(), Name.class);
                        mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID, editCashId.getText().toString());
                        mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD, editPassword.getText().toString());
                        startActivity(i);
                    }
                }
            }
        });
    }

}
