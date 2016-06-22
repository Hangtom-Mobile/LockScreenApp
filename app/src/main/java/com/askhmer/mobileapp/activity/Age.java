package com.askhmer.mobileapp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.utils.MyDatePickerDialog;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

import java.util.Calendar;

public class Age extends AppCompatActivity{

    private SharedPreferencesFile mSharedPrefrencesFile;
    private EditText etDOB;
    private int year, month, day;
    private Calendar calendar;
    private String sDOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSharedPrefrencesFile = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        etDOB = (EditText) findViewById(R.id.et_age);
        etDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    setDate(v);
                }else {
                    //Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });

        final Button button = (Button)findViewById(R.id.bttn_next);
               button.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Toast.makeText(Age.this, ""+sDOB, Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getApplicationContext(), Location.class);
                               mSharedPrefrencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_AGE, sDOB);
                               startActivity(intent);
                           }
                   });

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            MyDatePickerDialog mDatePicker = new MyDatePickerDialog(this,myDateListener,year -18, month, day);
            mDatePicker.setPermanentTitle("Your date of birth");
//            return new DatePickerDialog(this, myDateListener, year, month, day);
            return mDatePicker;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        String sMonth = month+"";
        String sDay = day+"";
        if(sMonth.length()== 1){
            sMonth = "0"+sMonth;
        }
        if(sDay.length()== 1){
            sDay = "0"+sDay;
        }
        sDOB = new StringBuilder().append(year).append(sMonth).append(sDay)+"";
        etDOB.setText("" + new StringBuilder().append(year).append("/").append(sMonth).append("/").append(sDay), TextView.BufferType.NORMAL);
    }
    
}

