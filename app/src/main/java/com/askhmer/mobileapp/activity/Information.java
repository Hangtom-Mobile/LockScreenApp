package com.askhmer.mobileapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.askhmer.mobileapp.R;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                TextView veSlideId = (TextView) findViewById(R.id.ve_slide_id);

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

                TextView veSlideId = (TextView)findViewById(R.id.ve_pass_id);

                if(conPassword.length() < 8) {
                    veSlideId.setText("Password at least 8 characters");
                    veSlideId.setVisibility(View.VISIBLE);
                }else {
                    veSlideId.setVisibility(View.GONE);
                    if(!password.equals(conPassword)){
                        veSlideId.setText("Password and Confirm password not match");
                        veSlideId.setVisibility(View.VISIBLE);
                    }else {
                        veSlideId.setVisibility(View.GONE);
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

                TextView veSlideId = (TextView)findViewById(R.id.ve_pass_id);

                if(password.length() < 8) {
                    veSlideId.setText("Password at least 8 characters");
                    veSlideId.setVisibility(View.VISIBLE);
                }else {
                    veSlideId.setVisibility(View.GONE);
                    if(!password.equals(conPassword)){
                        veSlideId.setText("Password and Confirm password not match");
                        veSlideId.setVisibility(View.VISIBLE);
                    }else {
                        veSlideId.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

}
