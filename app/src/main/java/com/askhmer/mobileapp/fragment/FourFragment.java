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
public class FourFragment extends Fragment {

    public FourFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fourFragmentView = inflater.inflate(R.layout.fragment_four, container, false);

        //TODO code

        return fourFragmentView;
    }
}
