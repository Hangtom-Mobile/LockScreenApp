package com.askhmer.lockscreen.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.lockscreen.R;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.HashMap;

/**
 * Created by soklundy on 9/27/2016.
 */
public class ImageSliderWithFragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mDemoSlider;
    private Context context;

    public ImageSliderWithFragment(SliderLayout mDemoSlider, Context context){
        this.mDemoSlider = mDemoSlider;
        this.context = context;

        /*set image*/
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("home2",R.drawable.home_2);
        file_maps.put("home3",R.drawable.home_3);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(context);
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation(){
                /**
                 * When next item show in ViewPagerEx, let's make an animation to show the
                 * description layout.
                 * @param view
                 */
                @Override
                public void onNextItemAppear(View view) {

                    View descriptionLayout = view.findViewById(com.daimajia.slider.library.R.id.description_layout);
                    if(descriptionLayout!=null){
                        float layoutY = ViewHelper.getY(descriptionLayout);
                        view.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.INVISIBLE);
                        ValueAnimator animator = ObjectAnimator.ofFloat(
                                descriptionLayout, "y", layoutY + descriptionLayout.getHeight(),
                                layoutY).setDuration(500);
                        animator.start();
                    }

                }
            });
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(context, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }
}
