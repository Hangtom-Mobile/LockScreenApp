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
public class OneFragment extends Fragment {
    public OneFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oneFragmentView = inflater.inflate(R.layout.fragment_one, container, false);

        //TODO code

        return oneFragmentView;
    }
}
