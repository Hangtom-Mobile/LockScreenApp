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
import android.widget.Toast;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

public class Location extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner location;
    private SharedPreferencesFile mSharedPreferencesFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPreferencesFile = SharedPreferencesFile.newInstance(getApplicationContext(), SharedPreferencesFile.FILE_INFORMATION_TEMP);
        Button button =  (Button)findViewById(R.id.bttn_next);
        location = (Spinner) findViewById(R.id.sp_location);
        location.setPrompt("Select year location!!!");


        // Creating adapter for spinner
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.list_province, android.R.layout.simple_spinner_item);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        location.setAdapter(dataAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                mSharedPreferencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_LOCATION, location.getSelectedItem().toString());
                mSharedPreferencesFile.putBooleanSharedPreference(SharedPreferencesFile.IS_OPEN_INFORMATION_SCREEN_KEY, true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
