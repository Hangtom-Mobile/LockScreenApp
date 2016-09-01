package com.askhmer.lockscreen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.askhmer.lockscreen.R;

public class RegisterOrLogin extends Activity {

    private LinearLayout btnSignUp;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_login);

        btnSignUp = (LinearLayout) findViewById(R.id.btn_sign_up);
        btnLogin = (Button) findViewById(R.id.btn_login);

        /*btn sign up*/
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PhoneNumber.class));
            }
        });

        /*btn login*/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}
