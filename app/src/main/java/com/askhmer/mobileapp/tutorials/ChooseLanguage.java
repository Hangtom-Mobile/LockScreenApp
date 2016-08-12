package com.askhmer.mobileapp.tutorials;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.utils.MutiLanguage;

/**
 * Created by Longdy on 6/30/2016.
 */
public class ChooseLanguage extends AppCompatActivity {

    private MutiLanguage mutiLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_langauge);

        final RadioGroup toggle = (RadioGroup) findViewById(R.id.radio_language);
        final int selectedId = toggle.getCheckedRadioButtonId();
        RadioButton radioBtnEn = (RadioButton) findViewById(R.id.radio_english);
        RadioButton radioBtnKm = (RadioButton) findViewById(R.id.radio_khmer);
        Button buttonStart = (Button) findViewById(R.id.bttn_start);

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
                RadioGroup toggle = (RadioGroup) findViewById(R.id.radio_language);
                if (toggle.getCheckedRadioButtonId() == R.id.radio_khmer) {
                    mutiLanguage.setLanguage("km");
                } else {
                    mutiLanguage.setLanguage("en");
                }

                Intent intent = new Intent(getApplication(), MainPage.class);
                startActivity(intent);
            }
        });
    }
}
