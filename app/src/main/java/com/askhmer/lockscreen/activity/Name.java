package com.askhmer.lockscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;

/**
 * Created by soklundy on 6/18/2016.
 */
public class Name extends AppCompatActivity {

    private SharedPreferencesFile mSharedPrefrencesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
        Button button = (Button)findViewById(R.id.bttn_next);
        final EditText editText = (EditText)findViewById(R.id.e_name);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Gender.class);
                mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NAME, editText.getText().toString());
                startActivity(i);
            }
        });
    }

}
