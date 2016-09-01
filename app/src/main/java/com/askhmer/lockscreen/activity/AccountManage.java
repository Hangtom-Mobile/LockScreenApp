package com.askhmer.lockscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.askhmer.lockscreen.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

public class AccountManage extends SwipeBackActivity {

    private LinearLayout myInfo, changePwd, forgetPwd /*withdrawal*/;
    private Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);

//swipe back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

// init
        myInfo = (LinearLayout) findViewById(R.id.li_my_info);
        changePwd = (LinearLayout) findViewById(R.id.li_change_pwd);
        forgetPwd = (LinearLayout) findViewById(R.id.li_forget_pwd);
        /*withdrawal = (LinearLayout) findViewById(R.id.li_withdrawal);*/

// listener
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(AccountManage.this, PasswordFindMoney.class);
                in.putExtra("class","MyInfo");
                startActivity(in);
                finish();
                AccountManage.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });

        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(AccountManage.this, PasswordFindMoney.class);
                in.putExtra("class","ChangPwd");
                startActivity(in);
                finish();
                AccountManage.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });

        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(AccountManage.this, ForgetPwd.class);
                startActivity(in);
                finish();
                AccountManage.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });

        /*withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(AccountManage.this, Withdrawal.class);
                startActivity(in);
                AccountManage.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });*/
    }
}
