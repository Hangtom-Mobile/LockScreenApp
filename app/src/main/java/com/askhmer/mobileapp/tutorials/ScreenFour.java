package com.askhmer.mobileapp.tutorials;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askhmer.mobileapp.R;

/**
 * Created by Longdy on 6/30/2016.
 */
public class ScreenFour extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_screen_four, container, false);
        return v;
    }
}