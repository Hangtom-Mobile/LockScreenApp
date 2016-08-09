package com.askhmer.mobileapp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.adapter.FullScreenImageAdapter;
import com.askhmer.mobileapp.model.LockScreenBackgroundDto;
import com.askhmer.mobileapp.model.ScreenListener;
import com.askhmer.mobileapp.network.API;
import com.askhmer.mobileapp.network.CheckInternet;
import com.askhmer.mobileapp.network.MySingleton;
import com.askhmer.mobileapp.utils.LockscreenService;
import com.askhmer.mobileapp.utils.LockscreenUtils;
import com.askhmer.mobileapp.utils.MyBroadCastReciever;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;
import com.askhmer.mobileapp.utils.ToggleSwitchButtonByDy;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LockScreenActivity extends Activity implements
		LockscreenUtils.OnLockStatusChangedListener,ScreenListener {

	// User-interface
	private Button btnUnlock;

	// Member variables
	private LockscreenUtils mLockscreenUtils;
	private ViewPager imageViewPager;
	private FullScreenImageAdapter fullScreenImageAdapter;
	private MyBroadCastReciever mReceiver;
	private RelativeLayout relativeLayout;
	private int position = 1;
	private SharedPreferencesFile mSharedPref;
	private ToggleSwitchButtonByDy toggleWebsite, toggleVideo, toggleCall;

//	private ArrayList<CompanyDto> arrList;

	private ArrayList<LockScreenBackgroundDto> pathFile;
	// Set appropriate flags to make the screen appear over the keyguard
	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(
				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
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

		mSharedPref = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
		/*mSharedPref.putBooleanSharedPreference(SharedPreferencesFile.PREFER_KEY, true);*/
		relativeLayout = (RelativeLayout) findViewById(R.id.relative_main);

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

				if (mSharedPref.getBooleanSharedPreference(SharedPreferencesFile.SERVICELOCK) == false) {
					// start service for observing intents
					startService(new Intent(this, LockscreenService.class));
				}

				// listen the events get fired during the call
				StateListener phoneStateListener = new StateListener();
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
				telephonyManager.listen(phoneStateListener,
						PhoneStateListener.LISTEN_CALL_STATE);

			} catch (Exception e) {
			}

		}

		toggleVideo = (ToggleSwitchButtonByDy) findViewById(R.id.toggle_video);
		toggleCall = (ToggleSwitchButtonByDy) findViewById(R.id.toggle_call);
		toggleWebsite = (ToggleSwitchButtonByDy) findViewById(R.id.toggle_website);

		/*request to server*/
		if (new CheckInternet().isConnect(getApplicationContext()) != true) {
			relativeLayout.setBackgroundResource(R.drawable.temp);
		}else {
			/*pathFile = new ArrayList<LockScreenBackgroundDto>();*/
		}

		Thread myThread = null;

		Runnable runnable = new CountDownRunner();
		myThread= new Thread(runnable);
		myThread.start();

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		mReceiver = new MyBroadCastReciever(this);
		registerReceiver(mReceiver, filter);
	}

	private void init() {
		mLockscreenUtils = new LockscreenUtils();
		imageViewPager = (ViewPager) findViewById(R.id.view_pager);
	}

	@Override
	public void displayMessage() {
		System.gc();
		lockScreenRequestServer("message");
		imageViewPager.setCurrentItem(position++, false);
	}

	// Handle events of calls and unlock screen if necessary
	private class StateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				unlockHomeButton();
				wakeUp();
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

			return false;
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

			return false;
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
	public String getShiftOfDate(Calendar cal) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a");
		String fullDateFormat = dateFormat.format(cal.getTime());
		return fullDateFormat.substring(fullDateFormat.length() - 2);
	}

	public void doWork() {
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					TextView txtCurrentTime = (TextView) findViewById(R.id.time);
					TextView txtCurrentShift = (TextView) findViewById(R.id.shift);
					TextView txtCurrentDate = (TextView) findViewById(R.id.date);

					SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
					Calendar cal = Calendar.getInstance();

					String time = dateFormat.format(cal.getTime());

					txtCurrentTime.setText(time.substring(0, time.length() - 3));
					txtCurrentShift.setText(getShiftOfDate(cal).toUpperCase());
					txtCurrentDate.setText(new SimpleDateFormat("dd/MM/yy").format(cal.getTime()));

				} catch (Exception e) {
				}
			}
		});
	}

	class CountDownRunner implements Runnable{
		// @Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				try {
					doWork();
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}catch(Exception e){
				}
			}
		}
	}

	public void lockScreenRequestServer(final String note) {
		if (pathFile == null){
			pathFile = new ArrayList<LockScreenBackgroundDto>();
		}
		final String cashId = mSharedPref.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID);
		if (cashId != null) {
			StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.medayi.com/locknet/locknet_api.php",
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							Log.e("response", response);
							if (!response.isEmpty()) {
								try {
									JSONObject jsonObj = new JSONObject(response);
									LockScreenBackgroundDto dto = new LockScreenBackgroundDto();
									dto.setuId(jsonObj.getString("uid"));
									dto.setLockBasicPrice(jsonObj.getString("lock_basic_price"));
									dto.setLockViewPrice(jsonObj.getString("lock_view_price"));
									dto.setImageUrl(jsonObj.getString("image"));
									dto.setWebUrl(jsonObj.getString("url"));
									dto.setType(jsonObj.getString("left_type"));
									pathFile.add(dto);
									fullScreenImageAdapter.notifyDataSetChanged();
									selectSwitchButton();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}else {
								Toast.makeText(LockScreenActivity.this, "No data", Toast.LENGTH_SHORT).show();
							}
						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {

				}
			}){
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<>();
					params.put("cash_slide_id", cashId);
					return params;
				}
				@Override
				public void deliverError(VolleyError error) {
					if (error instanceof NoConnectionError) {
						Cache.Entry entry = this.getCacheEntry();
						if(entry != null) {
							Response<String> response = parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
							deliverResponse(response.result);
							return;
						}
					}
					super.deliverError(error);
				}
			};
			MySingleton.getInstance(this).addToRequestQueue(stringRequest);
			if (fullScreenImageAdapter == null) {
				fullScreenImageAdapter = new FullScreenImageAdapter(this, pathFile);
				imageViewPager.setAdapter(fullScreenImageAdapter);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		for (int i = 1; i <= 2 ; i++) {
			lockScreenRequestServer("On start");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		System.gc();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}



	public void requestPointToServer(final String sliding, final String keyOfPoint, final String point, final String uId){
		Log.e("uid ", uId);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTPOINT,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("cash_slide_id", mSharedPref.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
				params.put("cash_password", mSharedPref.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD));
				params.put("token_id", mSharedPref.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN));
				params.put("sliding", sliding);
				params.put("uid", uId);
				params.put(keyOfPoint, point);
				return params;
			}
		};
		MySingleton.getInstance(this).addToRequestQueue(stringRequest);
	}

	public void wakeUp() {

		/*Delay 1sescond*/
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
				PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
				wakeLock.acquire();
				wakeLock.release();
			}
		}, 500);
	}

	private void requestVideo(String url) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObj = new JSONObject(response);
							if (jsonObj.getString("rst").equals("110")) {
								Intent intent = new Intent(getApplicationContext(),YoutubeVideo.class);
								intent.putExtra("movie_id", jsonObj.getString("movie_id"));
								intent.putExtra("movie_time", jsonObj.getString("movie_time"));
								intent.putExtra("movie_end_url", jsonObj.getString("movie_end_url"));
								intent.putExtra("uid", pathFile.get(imageViewPager.getCurrentItem()).getuId());
								intent.putExtra("lock_view_price", pathFile.get(imageViewPager.getCurrentItem()).getLockViewPrice());
								startActivity(intent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(LockScreenActivity.this, "No internet!", Toast.LENGTH_SHORT).show();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("uid", pathFile.get(imageViewPager.getCurrentItem()).getuId());
				return params;
			}
		};
		MySingleton.getInstance(this).addToRequestQueue(stringRequest);

	}

	public void switchButtonWebsite() {
		toggleCall.setVisibility(View.GONE);
		toggleVideo.setVisibility(View.GONE);
		toggleWebsite.setVisibility(View.VISIBLE);

		toggleWebsite.setOnTriggerListener(new ToggleSwitchButtonByDy.OnTriggerListener() {
			@Override
			public void toggledUp() {
				if (new CheckInternet().isConnect(getApplicationContext()) == true) {
					if (pathFile.size() > 0) {
						String unlockPrice = pathFile.get(imageViewPager.getCurrentItem()).getLockBasicPrice();
						String uId = pathFile.get(imageViewPager.getCurrentItem()).getuId();
						requestPointToServer("right", "lock_basic_price", unlockPrice, uId);
					}
				}
				unlockHomeButton();
			}

			@Override
			public void toggledDown() {
				if (pathFile.size() > 0) {
					String url = pathFile.get(imageViewPager.getCurrentItem()).getWebUrl();
					String uId = pathFile.get(imageViewPager.getCurrentItem()).getuId();
					String urlPrice = pathFile.get(imageViewPager.getCurrentItem()).getLockViewPrice();

					if (!url.isEmpty()) {
						if (new CheckInternet().isConnect(getApplicationContext()) == true) {
							requestPointToServer("left", "lock_view_price", urlPrice, uId);
						}
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(browserIntent);
					} else {
						new SweetAlertDialog(LockScreenActivity.this)
								.setTitleText("Sorry this banner no link!")
								.show();
					}
				} else {
					new SweetAlertDialog(LockScreenActivity.this)
							.setTitleText("Sorry your phone no internet!")
							.show();
				}
			}
		});

		toggleWebsite.setRotation(90.0f);
	}

	public void switchButtonVideo() {
		toggleCall.setVisibility(View.GONE);
		toggleWebsite.setVisibility(View.GONE);
		toggleVideo.setVisibility(View.VISIBLE);

		toggleVideo.setOnTriggerListener(new ToggleSwitchButtonByDy.OnTriggerListener() {
			@Override
			public void toggledUp() {
				if (new CheckInternet().isConnect(getApplicationContext()) == true) {
					if (pathFile.size() > 0) {
						String unlockPrice = pathFile.get(imageViewPager.getCurrentItem()).getLockBasicPrice();
						String uId = pathFile.get(imageViewPager.getCurrentItem()).getuId();
						requestPointToServer("right", "lock_basic_price", unlockPrice, uId);
					}
				}
				unlockHomeButton();
			}

			@Override
			public void toggledDown() {
				if (pathFile.size() > 0) {
					String url = pathFile.get(imageViewPager.getCurrentItem()).getWebUrl();

					if (!url.isEmpty()) {
						requestVideo(url);
					} else {
						new SweetAlertDialog(LockScreenActivity.this)
								.setTitleText("Sorry this banner no link!")
								.show();
					}
				} else {
					new SweetAlertDialog(LockScreenActivity.this)
							.setTitleText("Sorry your phone no internet!")
							.show();
				}
			}
		});

		toggleVideo.setRotation(90.0f);
	}

	public void switchButtonCall() {
		toggleWebsite.setVisibility(View.GONE);
		toggleVideo.setVisibility(View.GONE);
		toggleCall.setVisibility(View.VISIBLE);

		toggleCall.setOnTriggerListener(new ToggleSwitchButtonByDy.OnTriggerListener() {
			@Override
			public void toggledUp() {
				if (new CheckInternet().isConnect(getApplicationContext()) == true) {
					if (pathFile.size() > 0) {
						String unlockPrice = pathFile.get(imageViewPager.getCurrentItem()).getLockBasicPrice();
						String uId = pathFile.get(imageViewPager.getCurrentItem()).getuId();
						requestPointToServer("right", "lock_basic_price", unlockPrice, uId);
					}
				}
				unlockHomeButton();
			}

			@Override
			public void toggledDown() {
				if (pathFile.size() > 0) {
					String url = pathFile.get(imageViewPager.getCurrentItem()).getWebUrl();
					String uId = pathFile.get(imageViewPager.getCurrentItem()).getuId();
					String urlPrice = pathFile.get(imageViewPager.getCurrentItem()).getLockViewPrice();

					if (!url.isEmpty()) {
						if (new CheckInternet().isConnect(getApplicationContext()) == true) {
							requestPointToServer("left", "lock_view_price", urlPrice, uId);
						}
						Uri number = Uri.parse(url);
						Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
						startActivity(callIntent);
					} else {
						new SweetAlertDialog(LockScreenActivity.this)
								.setTitleText("Sorry this banner no link!")
								.show();
					}
				} else {
					new SweetAlertDialog(LockScreenActivity.this)
							.setTitleText("Sorry your phone no internet!")
							.show();
				}
			}
		});

		toggleCall.setRotation(90.0f);
	}

	public void selectSwitchButton() {
		String type = pathFile.get(imageViewPager.getCurrentItem()).getType();
		if (type.equals("1")) {
			switchButtonWebsite();
		}else if (type.equals("2")) {
			switchButtonVideo();
		}else {
			switchButtonCall();
		}
	}
}