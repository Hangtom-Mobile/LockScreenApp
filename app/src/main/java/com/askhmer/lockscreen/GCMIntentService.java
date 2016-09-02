package com.askhmer.lockscreen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.askhmer.lockscreen.activity.CustomizeWebGCM;
import com.askhmer.lockscreen.utils.GcmUtil;
import com.google.android.gcm.GCMBaseIntentService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(GcmUtil.SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        Log.e("GCM", "Your device registred with GCM");
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
//        displayMessage(context, getString(R.string.gcm_unregistered));
//        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message =  intent.getExtras().getString("msg");
        message= URLDecoder.decode(message);
        String title = intent.getExtras().getString("title");
        title = URLDecoder.decode(title);
        String page = intent.getExtras().getString("page");
        page = URLDecoder.decode(page);
        String img_url = intent.getExtras().getString("img");
        img_url = URLDecoder.decode(img_url);



        Bitmap bitmap = getBitmapFromURL(img_url);

        Intent in = new Intent(this, CustomizeWebGCM.class);
        in.putExtra("page", page);

        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        PendingIntent ipending = PendingIntent.getActivity(GCMIntentService.this, (int) System.currentTimeMillis(), in, 0);

        Uri rsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

/*
        Notification no = new Notification.Builder(GCMIntentService.this)
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo(message)
                .setTicker(message)
                .setContentIntent(ipending)
                .setSound(rsound)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setLargeIcon(bitmap)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(bitmap))
                .build();
*/

        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this);
        mBuilder.setContentText(message);
        mBuilder.setContentTitle(title);
        mBuilder.setTicker(message);
        mBuilder.setContentIntent(ipending);
        mBuilder.setSound(rsound);
        mBuilder.setSmallIcon(R.mipmap.ic_medayi);
        mBuilder.setAutoCancel(true);
//        mBuilder.setLargeIcon(R.drawable.ic_launcher);

        NotificationCompat.BigPictureStyle bigPicture = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
        bigPicture.setSummaryText(message);

        mBuilder.setStyle(bigPicture);


        mBuilder.setPriority(Notification.PRIORITY_MAX);


        int pushId = (int) System.currentTimeMillis();
        notificationManager.notify(pushId, mBuilder.build());
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
//        String message = getString(R.string.gcm_deleted, total);
//        displayMessage(context, message);
        // notifies user
//        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
//        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
//        displayMessage(context, getString(R.string.gcm_recoverable_error,errorId));
        return super.onRecoverableError(context, errorId);
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
