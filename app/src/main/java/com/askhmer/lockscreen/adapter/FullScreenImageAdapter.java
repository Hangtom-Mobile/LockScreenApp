package com.askhmer.lockscreen.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.model.LockScreenBackgroundDto;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;


import java.util.ArrayList;

/**
 * Created by Longdy on 6/13/2016.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<LockScreenBackgroundDto> _imagePaths;
    private LayoutInflater inflater;
    private Uri uri;

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
        /*final ImageView imgDisplay;*/
        TextView txtViewPoint;
        TextView txtBasicPoint;

        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.item_image_viewer, container, false);

        /*imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);*/

        txtBasicPoint = (TextView) viewLayout.findViewById(R.id.txt_basic_point);
        txtViewPoint = (TextView) viewLayout.findViewById(R.id.txt_view_point);

        SimpleDraweeView draweeView = (SimpleDraweeView) viewLayout.findViewById(R.id.imgDisplay);
        uri = Uri.parse(_imagePaths.get(position).getImageUrl());
        draweeView.setImageURI(uri);

        /*Picasso.with(container.getContext()).load(_imagePaths.get(position).getImageUrl()).placeholder(R.drawable.no_inte_slow).skipMemoryCache().into(imgDisplay);*/


        txtBasicPoint.setText("+ " + _imagePaths.get(position).getLockBasicPrice());
        txtViewPoint.setText("+ " + _imagePaths.get(position).getLockViewPrice());

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

    public void clearData() {
        Fresco.getImagePipelineFactory().getMainDiskStorageCache().clearAll();
        Fresco.getImagePipelineFactory().getSmallImageDiskStorageCache().clearAll();
    }

    public void delete() {
        _imagePaths.clear();
    }
}
