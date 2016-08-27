package com.askhmer.mobileapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.tutorials.ChooseLanguage;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;


/**
 * Created by soklundy on 4/20/2016.
 */
public class SplashScreen extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        System.gc();
        StartAnimations();

        SharedPreferencesFile mSharedPref  = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        // Restore preferences_n
        final boolean registerOrLoginScreen = mSharedPref.getBooleanSharedPreference(SharedPreferencesFile.REGISTERNLOGIN);
        final boolean isIntroFinish = mSharedPref.getBooleanSharedPreference(SharedPreferencesFile.IS_KEY_INTRO_FINISH);


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = null;

                if(isIntroFinish == true && registerOrLoginScreen == true) {
                    mainIntent = new Intent(SplashScreen.this, MainActivityTab.class);
                }else if (isIntroFinish == false && registerOrLoginScreen == false) {
                    mainIntent = new Intent(SplashScreen.this, ChooseLanguage.class);
                }else if (isIntroFinish == true && registerOrLoginScreen == false) {
                    mainIntent = new Intent(SplashScreen.this, RegisterOrLogin.class);
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
