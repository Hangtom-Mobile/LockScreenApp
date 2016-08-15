package com.askhmer.mobileapp.tutorials;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
    private ImageView flatKhmer, flatEng;

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
        flatEng = (ImageView) findViewById(R.id.flat_eng);
        flatKhmer = (ImageView) findViewById(R.id.flat_khmer);

        mutiLanguage = new MutiLanguage(getApplicationContext(), this);
        String lang = mutiLanguage.getLanguageCurrent();

        if (lang.equals("en") || lang.isEmpty()) {
            radioBtnEn.setChecked(true);
        }else {
            radioBtnKm.setChecked(true);
        }

        /*change color in message on choose langauge*/
        changeColor();

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

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (radioBtnKm.isChecked() == true) {
                    mutiLanguage.setLanguage("km");
                } else if (radioBtnEn.isChecked() == true) {
                    mutiLanguage.setLanguage("en");
                }*/

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
                    mutiLanguage.setLanguage("km");
                }
            }
        });

        radioBtnEn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    radioBtnKm.setChecked(false);
                    mutiLanguage.setLanguage("en");
                }
            }
        });
    }

    private void changeColor(){
        String first = getResources().getString(R.string.choose_lang);
        TextView textView = (TextView)findViewById(R.id.tv_message);
        String next = "<font color='#03A9F4'> medayi</font>";
        textView.setText(Html.fromHtml(first + next));
    }
}
