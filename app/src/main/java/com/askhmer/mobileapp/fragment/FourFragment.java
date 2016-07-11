package com.askhmer.mobileapp.fragment;


import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.activity.AccountManage;
import com.askhmer.mobileapp.activity.PrivacyStatement;
import com.askhmer.mobileapp.activity.SendMail;
import com.askhmer.mobileapp.activity.TermsOfUse;
import com.askhmer.mobileapp.tutorials.MainPage;
import com.askhmer.mobileapp.utils.CheckVersionCode;
import com.askhmer.mobileapp.utils.LockscreenService;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

/**
 * Created by Longdy on 6/22/2016.
 */
public class FourFragment extends Fragment {

    private LinearLayout accountManage, contactUs, advertising, howToUse, privacy, terms;
    private Intent in;
    private SwitchCompat unlock;
    private SharedPreferencesFile mSharedPreferencesFile;
    private TextView verName;

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
        verName = (TextView) fourFragmentView.findViewById(R.id.ver_name);
        mSharedPreferencesFile = SharedPreferencesFile.newInstance(getContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        /*display version name*/
        verName.setText("Version " + new CheckVersionCode().checkVersionCode(getContext()));

        /*check service lock screen work or not*/
        if (isMyServiceRunning(LockscreenService.class) == true){
            unlock.setChecked(true);
        }else {
            unlock.setChecked(false);
        }
//Click listener

        unlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getActivity(), "Unlock on", Toast.LENGTH_SHORT).show();
                    mSharedPreferencesFile.putBooleanSharedPreference(SharedPreferencesFile.SERVICELOCK, false);
                    getContext().startService(new Intent(getActivity(), LockscreenService.class));
                }else {
                    Toast.makeText(getActivity(), "Unlock off", Toast.LENGTH_SHORT).show();
                    mSharedPreferencesFile.putBooleanSharedPreference(SharedPreferencesFile.SERVICELOCK, true);
                    getContext().stopService(new Intent(getContext().getApplicationContext(), LockscreenService.class));
                }
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
                contactUs();
            }
        });


        advertising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                in = new Intent(getActivity(), SendMail.class);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);

//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/html");
//                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
//
//                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });


        howToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(getActivity(), MainPage.class);
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

    public void contactUs(){

        new AlertDialog.Builder(getActivity())
                .setTitle("CONTACT US")
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
