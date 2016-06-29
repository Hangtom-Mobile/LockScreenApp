package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

public class Gender extends AppCompatActivity {

    private SharedPreferencesFile mSharedPrefrencesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
        final Intent intent = new Intent(getApplicationContext(),Age.class);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioMale:
                        mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_GENDER, "M");
                        startActivity(intent);
                        Gender.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                        break;

                    case R.id.radioFemale:
                        mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_GENDER, "F");
                        startActivity(intent);
                        Gender.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                        break;
                }
            }
        });
    }
}
