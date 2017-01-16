package com.askhmer.lockscreen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.model.LockScreenBackgroundDto;
import com.askhmer.lockscreen.utils.DownloadImage;
import com.bumptech.glide.Glide;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Longdy on 6/13/2016.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<LockScreenBackgroundDto> _imagePaths;
    private LayoutInflater inflater;
    /*private Uri uri;*/

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
        ImageView imageView;
        View viewLayout = null;

        try{
            inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewLayout = inflater.inflate(R.layout.item_image_viewer, container, false);
        }catch (Exception e) {

        }

        /*imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);*/

        txtBasicPoint = (TextView) viewLayout.findViewById(R.id.txt_basic_point);
        txtViewPoint = (TextView) viewLayout.findViewById(R.id.txt_view_point);
        imageView = (ImageView) viewLayout.findViewById(R.id.imgDisplay);

        /*try {
            File f = new File("/data/data/com.askhmer.lockscreen/app_medayi_image_caching") ;
            File list[] = f.listFiles();

            for( int i=0; i < list.length; i++) {
                Log.e("show_list", list[i].getName());
            }
        }catch (Exception e) {

        }*/

        if (isHasImageInDirectory("/data/data/com.askhmer.lockscreen/app_medayi_image_caching",
                getFileNameFromUrl(_imagePaths.get(position).getImageUrl()))) {
            Bitmap bitmap = loadImageFromStorage("/data/data/com.askhmer.lockscreen/app_medayi_image_caching",
                    getFileNameFromUrl(_imagePaths.get(position).getImageUrl()));
            imageView.setImageBitmap(bitmap);
        }else {
            Glide.with(container.getContext())
                    .load(_imagePaths.get(position).getImageUrl())
                    .into(imageView);
            new DownloadImage(container.getContext()).execute(_imagePaths.get(position).getImageUrl());
        }

        /*SimpleDraweeView draweeView = (SimpleDraweeView) viewLayout.findViewById(R.id.imgDisplay);
        uri = Uri.parse(_imagePaths.get(position).getImageUrl());
        draweeView.setImageURI(uri);*/

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
        /*Fresco.getImagePipelineFactory().getMainDiskStorageCache().clearAll();
        Fresco.getImagePipelineFactory().getSmallImageDiskStorageCache().clearAll();*/
    }

    public void delete() {
        _imagePaths.clear();
        /*Fresco.getImagePipelineFactory().getMainDiskStorageCache().clearAll();
        Fresco.getImagePipelineFactory().getSmallImageDiskStorageCache().clearAll();*/
    }

    private Bitmap loadImageFromStorage(String path, String name) {
        Bitmap b;
        String name_="foldername";
        try {
            File f=new File(path, name);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            Log.e("testing_view", "load image from phone");
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isHasImageInDirectory(String path, String fileName) {
        File file = new File(path, fileName);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    private String getFileNameFromUrl(String url) {
        Uri u = Uri.parse(url);
        File f = new File("" + u);
        return f.getName().replaceAll("\\..*", "");
    }
}
