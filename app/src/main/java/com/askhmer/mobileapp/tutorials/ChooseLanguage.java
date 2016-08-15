package com.askhmer.mobileapp.tutorials;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.activity.TermsOfUse;
import com.askhmer.mobileapp.utils.MutiLanguage;

/**
 * Created by Longdy on 6/30/2016.
 */
public class ChooseLanguage extends AppCompatActivity {

    private MutiLanguage mutiLanguage;
    private RadioButton radioBtnEn;
    private RadioButton radioBtnKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        radioBtnEn = (RadioButton) findViewById(R.id.radio_english);
        radioBtnKm = (RadioButton) findViewById(R.id.radio_khmer);
        final Button buttonStart = (Button) findViewById(R.id.bttn_start);

        mutiLanguage = new MutiLanguage(getApplicationContext(), this);
        String lang = mutiLanguage.getLanguageCurrent();

        if (lang.equals("en") || lang.isEmpty()) {
            radioBtnEn.setChecked(true);
        }else {
            radioBtnKm.setChecked(true);
        }

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioBtnKm.isChecked() == true) {
                    mutiLanguage.setLanguage("km");
                } else if (radioBtnEn.isChecked() == true) {
                    mutiLanguage.setLanguage("en");
                }

                Intent intent = new Intent(getApplication(), MainPage.class);
                startActivity(intent);
            }
        });

         /*select only one radio at one time lisener*/
        selectOnlyOneRadioAtOneTime();
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
}
