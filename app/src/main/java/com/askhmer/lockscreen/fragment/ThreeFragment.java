package com.askhmer.lockscreen.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.activity.PasswordFindMoney;

/**
 * Created by Longdy on 6/22/2016.
 */
public class ThreeFragment extends Fragment{

    private LinearLayout hangtom, findMoney;

    public ThreeFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View threeFragmentView = inflater.inflate(R.layout.fragment_three, container, false);

        hangtom = (LinearLayout) threeFragmentView.findViewById(R.id.li_hangtom);
        hangtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangtom();
            }
        });

        findMoney = (LinearLayout) threeFragmentView.findViewById(R.id.find_money);
        findMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), PasswordFindMoney.class);
                in.putExtra("class","MoneyBalance");
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });
        //TODO code

        return threeFragmentView;
    }

    public void hangtom(){

        new AlertDialog.Builder(getActivity())
                .setTitle("HANGTOM")
                .setMessage("Coming soon...")
                .setCancelable(true)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
