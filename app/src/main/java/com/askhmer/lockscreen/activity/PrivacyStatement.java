package com.askhmer.lockscreen.activity;

import android.os.Bundle;

import com.askhmer.lockscreen.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;

public class PrivacyStatement extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_statement);

        //swipe back
        /*setDragEdge(SwipeBackLayout.DragEdge.LEFT);*/
    }
}
