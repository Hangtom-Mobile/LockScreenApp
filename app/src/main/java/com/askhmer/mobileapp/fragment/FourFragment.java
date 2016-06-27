package com.askhmer.mobileapp.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.activity.AccountManage;
import com.askhmer.mobileapp.activity.HowToUseChache;
import com.askhmer.mobileapp.activity.PrivacyStatement;
import com.askhmer.mobileapp.activity.TermsOfUse;

/**
 * Created by Longdy on 6/22/2016.
 */
public class FourFragment extends Fragment {

    private LinearLayout accountManage, contactUs, advertising, howToUse, privacy, terms;
    private Intent in;
    private SwitchCompat unlock;



    public FourFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fourFragmentView = inflater.inflate(R.layout.fragment_four, container, false);

//  init

        unlock = (SwitchCompat) fourFragmentView.findViewById(R.id.switch1);
        accountManage = (LinearLayout) fourFragmentView.findViewById(R.id.li_account_manage);
        contactUs = (LinearLayout) fourFragmentView.findViewById(R.id.li_contact_us);
        advertising = (LinearLayout) fourFragmentView.findViewById(R.id.li_advertising);
        howToUse = (LinearLayout) fourFragmentView.findViewById(R.id.li_how_to_use);
        privacy = (LinearLayout) fourFragmentView.findViewById(R.id.li_privacy);
        terms = (LinearLayout) fourFragmentView.findViewById(R.id.li_term_of_use);


//Click listener
        unlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getActivity(), "Unlock on", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getActivity(), "Unlock off", Toast.LENGTH_SHORT).show();
            }
        });


        accountManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(getActivity(), AccountManage.class);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });


        advertising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });


        howToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(getActivity(), HowToUseChache.class);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(getActivity(), PrivacyStatement.class);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(getActivity(), TermsOfUse.class);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });

        return fourFragmentView;
    }
}