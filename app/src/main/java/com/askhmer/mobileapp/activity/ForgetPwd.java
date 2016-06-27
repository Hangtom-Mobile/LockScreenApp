package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.askhmer.mobileapp.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

public class ForgetPwd extends SwipeBackActivity {

    private Button ok;
    private Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
//swipe back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);


        ok = (Button) findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(ForgetPwd.this, NewPassword.class);
                startActivity(in);
                ForgetPwd.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });
    }
}
