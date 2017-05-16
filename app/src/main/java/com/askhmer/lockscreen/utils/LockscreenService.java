package com.askhmer.lockscreen.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.activity.SplashScreen;

public class LockscreenService extends Service {

	private BroadcastReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	// Register for Lockscreen event intents
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new LockscreenIntentReceiver();
		try{
			registerReceiver(mReceiver, filter);
		}catch (Exception e) {

		}
		startForeground();
		return START_STICKY;
	}

	// Run service in foreground so it is less likely to be killed by system
	private void startForeground() {
		Intent myIntent = new Intent(getApplicationContext(), SplashScreen.class);
		PendingIntent intent2 = PendingIntent.getActivity(getApplicationContext(), 1,
				myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_medayi);

		Notification notification = new NotificationCompat.Builder(this)
				.setContentTitle(getResources().getString(R.string.app_name))
				.setTicker(getResources().getString(R.string.app_name))
				.setContentText("Slide to get money")
				.setSmallIcon(R.drawable.ic_medayi_noti)
				.setLargeIcon(bm)
				.setOngoing(true)
				.setPriority(Notification.PRIORITY_MIN)
				.setContentIntent(intent2)
				.build();
		 startForeground(1,notification);
	}

	// Unregister receiver
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
}
