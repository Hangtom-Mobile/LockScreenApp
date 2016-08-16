package com.askhmer.mobileapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.model.LockScreenBackgroundDto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by Longdy on 6/13/2016.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<LockScreenBackgroundDto> _imagePaths;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<LockScreenBackgroundDto> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final ImageView imgDisplay;
        TextView txtViewPoint;
        TextView txtBasicPoint;

        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.item_image_viewer, container, false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        txtBasicPoint = (TextView) viewLayout.findViewById(R.id.txt_basic_point);
        txtViewPoint = (TextView) viewLayout.findViewById(R.id.txt_view_point);

        Picasso.with(container.getContext()).load(_imagePaths.get(position).getImageUrl()).placeholder(R.drawable.no_inte_slow).into(imgDisplay, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                Picasso.with(container.getContext()).load(_imagePaths.get(position).getImageUrl()).placeholder(R.drawable.no_inte_slow).into(imgDisplay);
            }
        });

        txtBasicPoint.setText("+ " + _imagePaths.get(position).getLockBasicPrice());
        txtViewPoint.setText("+ " + _imagePaths.get(position).getLockViewPrice());

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
