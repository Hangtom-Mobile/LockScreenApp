package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.mobileapp.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

public class PasswordFindMoney extends SwipeBackActivity {

    private EditText password;
    private TextView validatePWD;
    private Button btnNext;

    private String inPutPassword;
    private Intent in;

    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_find_money);
//swipe back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

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

        Toast.makeText(PasswordFindMoney.this, className , Toast.LENGTH_SHORT).show();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inPutPassword = password.getText().toString();
                Toast.makeText(PasswordFindMoney.this, inPutPassword, Toast.LENGTH_SHORT).show();
                if (inPutPassword.equals("123")) {
                    if (className.equals("MyInfo")) {
                        in = new Intent(PasswordFindMoney.this, MyInfo.class);
                        startActivity(in);
                    }else if(className.equals("MoneyBalance")){
                        in = new Intent(PasswordFindMoney.this, MoneyBalance.class);
                        startActivity(in);
                    }else if(className.equals("ChangPwd")){
                        in = new Intent(PasswordFindMoney.this, ChangePwd.class);
                        startActivity(in);
                    }

                    PasswordFindMoney.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                    finish();
                } else validatePWD.setVisibility(View.VISIBLE);
            }
        });

    }
}
