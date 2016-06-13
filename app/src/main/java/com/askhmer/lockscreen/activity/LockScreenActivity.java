package com.askhmer.lockscreen.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.adapter.FullScreenImageAdapter;
import com.askhmer.lockscreen.utils.LockscreenService;
import com.askhmer.lockscreen.utils.LockscreenUtils;
import com.askhmer.lockscreen.utils.ToggleSwitchButtonByDy;

import java.util.ArrayList;

public class LockScreenActivity extends Activity implements
		LockscreenUtils.OnLockStatusChangedListener {

	// User-interface
	private Button btnUnlock;

	// Member variables
	private LockscreenUtils mLockscreenUtils;

//	private int imgTest = R.drawable.ic_launcher;
//	private RecyclerView mRecyclerView;
//	private LockScreenAdapter adapter;

	private ViewPager imageViewPager;
	private FullScreenImageAdapter fullScreenImageAdapter;

//	private ArrayList<CompanyDto> arrList;

	private ArrayList<String> pathFile;
	// Set appropriate flags to make the screen appear over the keyguard
	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(
				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						);

		super.onAttachedToWindow();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lockscreen);

		init();

		// unlock screen in case of app get killed by system
		if (getIntent() != null && getIntent().hasExtra("kill")
				&& getIntent().getExtras().getInt("kill") == 1) {
			enableKeyguard();
			unlockHomeButton();
		} else {

			try {
				// disable keyguard
				disableKeyguard();

				// lock home button
				lockHomeButton();

				// start service for observing intents
				startService(new Intent(this, LockscreenService.class));

				// listen the events get fired during the call
				StateListener phoneStateListener = new StateListener();
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
				telephonyManager.listen(phoneStateListener,
						PhoneStateListener.LISTEN_CALL_STATE);

			} catch (Exception e) {
			}

		}
		ToggleSwitchButtonByDy toggle = (ToggleSwitchButtonByDy) findViewById(R.id.toggle);
		toggle.setOnTriggerListener(new ToggleSwitchButtonByDy.OnTriggerListener() {
			@Override
			public void toggledUp() {
				unlockHomeButton();
			}

			@Override
			public void toggledDown() {
				Toast.makeText(LockScreenActivity.this, "2", Toast.LENGTH_SHORT).show();
			}
		});

		toggle.setRotation(90.0f);

//****************************
// 		did by Longdy
//****************************

//		arrList = new ArrayList<>();

		// Setup layout manager for mBlogList and column count
//		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

		// Control orientation of the mBlogList
//		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//		layoutManager.scrollToPosition(0);

		// Attach layout manager
//		mRecyclerView.setLayoutManager(layoutManager);
//
//		adapter = new LockScreenAdapter(arrList);
//		mRecyclerView.setAdapter(adapter);

		pathFile = new ArrayList<>();
		pathFile.add("https://www.nasa.gov/sites/default/files/styles/image_card_4x3_ratio/public/thumbnails/image/leisa_christmas_false_color.png?itok=Jxf0IlS4");
		pathFile.add("http://science-all.com/images/wallpapers/image/image-6.jpg");
		pathFile.add("https://support.files.wordpress.com/2009/07/pigeony.jpg?w=688");

		fullScreenImageAdapter = new FullScreenImageAdapter(this,pathFile);
		imageViewPager.setAdapter(fullScreenImageAdapter);

	}

	private void init() {
		mLockscreenUtils = new LockscreenUtils();
//		btnUnlock = (Button) findViewById(R.id.btnUnlock);
//		btnUnlock.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// unlock home button and then screen on button press
//				unlockHomeButton();
//			}
//		});

//		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

		imageViewPager = (ViewPager) findViewById(R.id.view_pager);

	}

	// Handle events of calls and unlock screen if necessary
	private class StateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				unlockHomeButton();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			}
		}
	};

	// Don't finish Activity on Back press
	@Override
	public void onBackPressed() {
		return;
	}

	// Handle button clicks
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (keyCode == KeyEvent.KEYCODE_POWER)
				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				|| (keyCode == KeyEvent.KEYCODE_CAMERA)) {
			return true;
		}
		if ((keyCode == KeyEvent.KEYCODE_HOME)) {

			return true;
		}

		return false;

	}

	// handle the key press events here itself
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
			return false;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {

			return true;
		}
		return false;
	}

	// Lock home button
	public void lockHomeButton() {
		mLockscreenUtils.lock(LockScreenActivity.this);
	}

	// Unlock home button and wait for its callback
	public void unlockHomeButton() {
		mLockscreenUtils.unlock();
	}

	// Simply unlock device when home button is successfully unlocked
	@Override
	public void onLockStatusChanged(boolean isLocked) {
		if (!isLocked) {
			unlockDevice();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		unlockHomeButton();
	}

	@SuppressWarnings("deprecation")
	private void disableKeyguard() {
		KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
		mKL.disableKeyguard();
	}

	@SuppressWarnings("deprecation")
	private void enableKeyguard() {
		KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
		mKL.reenableKeyguard();
	}
	
	//Simply unlock device by finishing the activity
	private void unlockDevice()
	{
		finish();
	}


/*
	public void listItem(){
		for (int i = 0; i<15; i++){
			CompanyDto item = new CompanyDto();
			item.setImgTest(imgTest);
			arrList.add(item);
		}
	}
*/

}