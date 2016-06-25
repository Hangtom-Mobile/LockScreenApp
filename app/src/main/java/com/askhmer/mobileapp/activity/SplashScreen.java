package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;


/**
 * Created by soklundy on 4/20/2016.
 */
public class SplashScreen extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private SharedPreferencesFile mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StartAnimations();

        mSharedPref  = SharedPreferencesFile.newInstance(this, SharedPreferencesFile.FILE_INFORMATION_TEMP);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = null;

                // Restore preferences_n
                boolean informationScreen = mSharedPref.getBooleanSharedPreference(SharedPreferencesFile.IS_OPEN_INFORMATION_SCREEN_KEY);

                if(informationScreen == true) {
                    mainIntent = new Intent(SplashScreen.this, WebviewActivity.class);
                }else {
                    mainIntent = new Intent(SplashScreen.this, PhoneNumber.class);
                }

                /* Create an Intent that will start the Menu-Activity. */
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.splash_screen_layout);
        l.clearAnimation();
        l.startAnimation(anim);
    }
}
