package com.askhmer.mobileapp.tutorials;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;

/**
 * Created by Longdy on 6/30/2016.
 */
public class MainPage extends AppIntro2 {
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new ScreenOne());
        addSlide(new ScreenTwo());
        addSlide(new ScreenThree());
        addSlide(new ScreenFour());
        addSlide(new ScreenFive());
        setDepthAnimation();

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        // addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        /*showSkipButton(false);*/
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(50);
    }

    @Override
    public void onDonePressed() {
        this.finish();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}
