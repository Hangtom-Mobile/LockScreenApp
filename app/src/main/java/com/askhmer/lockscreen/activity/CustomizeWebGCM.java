package com.askhmer.lockscreen.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.askhmer.lockscreen.R;
import com.thefinestartist.finestwebview.FinestWebView;

/**
 * Created by Longdy on 9/2/2016.
 */
public class CustomizeWebGCM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(CustomizeWebGCM.this, getIntent().getStringExtra("page"), Toast.LENGTH_SHORT).show();

        new FinestWebView.Builder(getApplicationContext())
                .statusBarColorRes(R.color.colorPrimaryDark)
                .toolbarColorRes(R.color.colorPrimary)
                .titleColorRes(R.color.finestWhite)
                .urlColorRes(R.color.finestWhite)
                .iconDefaultColorRes(R.color.finestWhite)
                .progressBarColorRes(R.color.finestWhite)
                .stringResRefresh(R.string.m_refresh)
                .stringResShareVia(R.string.m_share)
                .stringResCopyLink(R.string.m_copy)
                .stringResOpenWith(R.string.m_open)
                .menuTextGravity(Gravity.CENTER)
                .toolbarScrollFlags(0)
                .showSwipeRefreshLayout(false)
                .show(getIntent().getStringExtra("page"));
    }
}
