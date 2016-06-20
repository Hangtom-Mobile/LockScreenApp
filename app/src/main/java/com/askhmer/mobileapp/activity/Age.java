package com.askhmer.mobileapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.askhmer.mobileapp.R;

import java.util.ArrayList;

public class Age extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        year = (Spinner) findViewById(R.id.sp_year);
        year.setPrompt("Select year you was born!!!");

        ArrayList<Integer> y = new ArrayList<>();
        for(int i = 2016; i >= 1930; i--){
            y.add(i);
        }

        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, y);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        year.setAdapter(dataAdapter);


/*
        etYear.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int y = c.get(Calendar.YEAR) -18;
                int m = 0;
                int d = 0;
                final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                DatePickerDialog dp = new DatePickerDialog(Age.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String erg = "";
                                erg = String.valueOf(dayOfMonth);
                                erg += "." + String.valueOf(monthOfYear + 1);
                                erg += "." + year;

                                ((TextView) etYear).setText(erg);

                            }

                        }, y, m, d);
                dp.setTitle("Calender");
                dp.setMessage("Select Your Graduation date Please?");

                dp.show();


            }
        });
*/

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}

