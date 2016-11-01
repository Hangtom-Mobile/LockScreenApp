package com.askhmer.lockscreen.fragment;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.activity.AccountManage;
import com.askhmer.lockscreen.activity.PrivacyStatement;
import com.askhmer.lockscreen.activity.Recommend;
import com.askhmer.lockscreen.activity.TermsOfUse;
import com.askhmer.lockscreen.tutorials.MainPage;
import com.askhmer.lockscreen.utils.AlarmReceiver;
import com.askhmer.lockscreen.utils.CheckVersionCode;
import com.askhmer.lockscreen.utils.LockscreenService;
import com.askhmer.lockscreen.utils.MutiLanguage;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;

/**
 * Created by Longdy on 6/22/2016.
 */
public class FourFragment extends Fragment {

    private LinearLayout accountManage, contactUs, recommend, advertising, howToUse, privacy, terms, changeLang;
    private Intent in;
    private SwitchCompat unlock;
    private SharedPreferencesFile mSharedPreferencesFile;
    private TextView verName;
    RadioButton radioBtnEn;
    RadioButton radioBtnKm;
    private MutiLanguage mutiLanguage;
    private ImageView flatEng, flatKhmer;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private int hour;

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
        changeLang = (LinearLayout) fourFragmentView.findViewById(R.id.li_change_lan);
        recommend = (LinearLayout) fourFragmentView.findViewById(R.id.li_recommend);
        advertising = (LinearLayout) fourFragmentView.findViewById(R.id.li_advertising);
        howToUse = (LinearLayout) fourFragmentView.findViewById(R.id.li_how_to_use);
        privacy = (LinearLayout) fourFragmentView.findViewById(R.id.li_privacy);
        terms = (LinearLayout) fourFragmentView.findViewById(R.id.li_term_of_use);
        verName = (TextView) fourFragmentView.findViewById(R.id.ver_name);
        mSharedPreferencesFile = new SharedPreferencesFile(getContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        /*display version name*/
        verName.setText("Version " + new CheckVersionCode().checkVersionCode(getContext()));

        /*check service lock screen work or not*/
        if (isMyServiceRunning(LockscreenService.class) == true){
            unlock.setChecked(true);

            /*stop alarm when lock screen on*/
            stopAlarm();
        }else {
            unlock.setChecked(false);
        }
//Click listener

        unlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    /*Toast.makeText(getActivity(), "Unlock on", Toast.LENGTH_SHORT).show();*/
                    mSharedPreferencesFile.putBooleanSharedPreference(SharedPreferencesFile.SERVICELOCK, false);
                    getContext().startService(new Intent(getActivity(), LockscreenService.class));

                    /*stop alarm when lock screen on*/
                    stopAlarm();
                }else {
                    /*Toast.makeText(getActivity(), "Unlock off", Toast.LENGTH_SHORT).show();*/
                    dialogSchedule(getContext());
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

        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDiolag(getContext());
            }
        });

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(getActivity(), Recommend.class);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });

        advertising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contactUs();
//                in = new Intent(getActivity(), SendMail.class);
//                startActivity(in);
//                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_in);

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
                .setMessage(getResources().getString(R.string.contact_us))
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

    private void alertDiolag(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.alert_dialog_activity_choose_lan);

        radioBtnEn = (RadioButton)dialog.findViewById(R.id.radio_english);
        radioBtnKm = (RadioButton)dialog.findViewById(R.id.radio_khmer);
        flatEng = (ImageView) dialog.findViewById(R.id.flat_eng);
        flatKhmer = (ImageView) dialog.findViewById(R.id.flat_khmer);

        mutiLanguage = new MutiLanguage(getContext(),getActivity());
        String lang = mutiLanguage.getLanguageCurrent();

        /*check langauge at first time*/
        selectOnlyOneRadioAtOneTime();

        /*select on flag event*/
        eventSelectOnFlag();

        if (lang.equals("en") || lang.isEmpty()) {
            radioBtnEn.setChecked(true);
        }else {
            radioBtnKm.setChecked(true);
        }

        dialog.findViewById(R.id.bttn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (radioBtnKm.isChecked() == true) {
                    mutiLanguage.setLanguage("km");
                } else if (radioBtnEn.isChecked() == true) {
                    mutiLanguage.setLanguage("en");
                }
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void selectOnlyOneRadioAtOneTime() {
        radioBtnKm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    radioBtnEn.setChecked(false);
                }
            }
        });

        radioBtnEn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    radioBtnKm.setChecked(false);
                }
            }
        });
    }

    private void eventSelectOnFlag() {
        flatEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioBtnEn.setChecked(true);
                radioBtnKm.setChecked(false);
            }
        });

        flatKhmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioBtnKm.setChecked(true);
                radioBtnEn.setChecked(false);
            }
        });
    }

    private void startAlarm(int minute) {
        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (minute * 60000), pendingIntent);
    }

    private void stopAlarm() {
        if (manager != null) {
            manager.cancel(pendingIntent);
        }
    }

    private void dialogSchedule(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                unlock.setChecked(true);
            }
        });
        dialog.setContentView(R.layout.alert_dialog_schedule);


        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);

        // Creating adapter for spinner
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(context, R.array.hour, android.R.layout.simple_spinner_item);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        dialog.findViewById(R.id.bttn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (spinner.getSelectedItemPosition()) {
                    case 0:
                        startAlarm(60);
                        break;
                    case 1:
                        startAlarm(120);
                        break;
                    case 2:
                        startAlarm(300);
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
                mSharedPreferencesFile.putBooleanSharedPreference(SharedPreferencesFile.SERVICELOCK, true);
                getContext().stopService(new Intent(getContext().getApplicationContext(), LockscreenService.class));
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
