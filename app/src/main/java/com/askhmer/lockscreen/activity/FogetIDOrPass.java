package com.askhmer.lockscreen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.askhmer.lockscreen.R;

public class FogetIDOrPass extends Activity {

    private LinearLayout btnFindId;
    private Button btnFindPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_id_pass);

        btnFindId = (LinearLayout) findViewById(R.id.find_id);
        btnFindPass = (Button) findViewById(R.id.find_pass);

        /*btn sign up*/
        btnFindId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RequestID.class));
            }
        });

        /*btn login*/
        btnFindPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RequestPassword.class));
            }
        });
    }
}
