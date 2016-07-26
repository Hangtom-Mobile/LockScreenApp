package com.askhmer.mobileapp.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.youtube.player.YouTubePlayer;

/**
 * Created by soklundy on 7/25/2016.
 */
public class YoutubeService extends Service {

    private YouTubePlayer player;

    public YoutubeService(YouTubePlayer player){
        this.player = player;
    }

    public YoutubeService(){
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        work();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void work() {
        Log.e("Duration", player.getCurrentTimeMillis()+"");
    }

}
