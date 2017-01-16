package com.askhmer.lockscreen.utils;

import android.app.Application;
import android.util.Log;

import com.thefinestartist.wip.FileUtil;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by soklundy on 1/16/2017.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        File f = new File("/data/data/com.askhmer.lockscreen/app_medayi_image_caching") ;
        if (folderSize(f) > 7000000) {
            try {
                FileUtils.deleteDirectory(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onCreate();
    }

    private long folderSize(File directory) {
        long length = 0;
        try {
            for (File file : directory.listFiles()) {
                length += file.length();
            }
        }catch (NullPointerException e) {

        }
        return length;
    }
}
