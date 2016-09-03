package com.askhmer.lockscreen.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.askhmer.lockscreen.activity.LockScreenActivity;
import com.askhmer.lockscreen.network.CheckInternet;

public class LockscreenIntentReceiver extends BroadcastReceiver {

	// Handle actions and display Lockscreen
	@Override
	public void onReceive(Context context, Intent intent) {

		// listen the events get fired during the call
		PhoneListener phoneStateListener = new PhoneListener(context,intent);
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
			Intent serviceIntent = new Intent(context, LockscreenService.class);
			context.startService(serviceIntent);
		}
	}

	// Display lock screen
	public void start_lockscreen(Context context) {
		if (new CheckInternet().isConnect(context) == true) {
			Intent mIntent = new Intent(context, LockScreenActivity.class);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mIntent);
		}
	}
}
