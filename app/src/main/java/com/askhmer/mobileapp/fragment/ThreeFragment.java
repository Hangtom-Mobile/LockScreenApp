package com.askhmer.mobileapp.fragment;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askhmer.mobileapp.R;

/**
 * Created by Longdy on 6/22/2016.
 */
public class ThreeFragment extends Fragment{

    public ThreeFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View threeFragmentView = inflater.inflate(R.layout.fragment_three, container, false);

        //TODO code

        return threeFragmentView;
    }
}
