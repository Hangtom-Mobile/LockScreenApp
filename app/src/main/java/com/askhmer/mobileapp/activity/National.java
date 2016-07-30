package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

public class National extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner national;
    private SharedPreferencesFile mSharedPreferencesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPreferencesFile = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.FILE_INFORMATION_TEMP);
        Button button =  (Button)findViewById(R.id.bttn_next);
        national = (Spinner) findViewById(R.id.sp_national);
        national.setPrompt("Select your National!!!");

        // Creating adapter for spinner
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.list_national, android.R.layout.simple_spinner_item);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        national.setAdapter(dataAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_NATIONAL, national.getSelectedItem().toString());
                Intent i = new Intent(getApplicationContext(), Location.class);
                startActivity(i);
                National.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Showing selected spinner item
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
