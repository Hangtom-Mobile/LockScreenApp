package com.askhmer.lockscreen.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by soklundy on 1/16/2017.
 */

public class DownloadImage extends AsyncTask<String, String, String> {

    private Context context;
    private String picName;

    public DownloadImage(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String imageUrl = params[0];

        /*get only file name from url*/
        Uri u = Uri.parse(imageUrl);
        File f = new File("" + u);
        picName = f.getName().replaceAll("\\..*", "");

        Bitmap theBitmap = null;
        try {
            theBitmap = Glide.
                    with(context).
                    load(imageUrl).
                    asBitmap().
                    into(-1, -1).
                    get();
            saveToInternalStorage(theBitmap, context, picName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String saveToInternalStorage(Bitmap bitmapImage, Context context, String name){

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir

        String name_="medayi_image_caching"; //Folder name in device android/data/
        File directory = cw.getDir(name_, Context.MODE_PRIVATE);

        // Create imageDir
        File mypath = new File(directory,name);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }
}
