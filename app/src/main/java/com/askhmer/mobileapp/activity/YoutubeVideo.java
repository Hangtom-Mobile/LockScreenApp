package com.askhmer.mobileapp.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.network.MySingleton;
import com.askhmer.mobileapp.utils.Config;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.HashMap;
import java.util.Map;

public class YoutubeVideo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private YouTubePlayerView youTubeView;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayer youTubePlayer;
    private Thread mainThread;
    private String movieId, movieEndUrl;
    private int movieTimeSecond, moiveTimeSecondTemp;
    private TextView txtTimeSkip;
    private LinearLayout btnSkip;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        txtTimeSkip = (TextView) findViewById(R.id.time_skip);
        btnSkip = (LinearLayout) findViewById(R.id.btn_skip);

        // Initializing video player with developer key
        youTubeView.initialize(Config.DEVELOPER_KEY, this);

        Intent intent = getIntent();
        movieId = intent.getStringExtra("movie_id");
        movieTimeSecond = Integer.parseInt(intent.getStringExtra("movie_time"));
        moiveTimeSecondTemp = movieTimeSecond;
        movieEndUrl = intent.getStringExtra("movie_end_url");
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!movieEndUrl.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieEndUrl));
                    startActivity(browserIntent);
                }
                finish();
            }
        });

        mainThread = new CountDownRunner();
        mainThread.start();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            youTubePlayer.loadVideo(movieId);

            // Hiding player controls
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            this.youTubePlayer = youTubePlayer;
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    class CountDownRunner extends Thread{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    int currentTimeMillis = youTubePlayer.getCurrentTimeMillis();
                    int durationTimeMillis = youTubePlayer.getDurationMillis();

                    /*for button skip and time*/
                    if (moiveTimeSecondTemp > 0 && currentTimeMillis >= 0 ) {
                        int countDownTime = moiveTimeSecondTemp - (currentTimeMillis / 1000);
                        if (countDownTime > 0) {
                            txtTimeSkip.setVisibility(View.VISIBLE);
                            txtTimeSkip.setText("Points will be credited after " + countDownTime + " second");
                        }
                    }

                    if ((currentTimeMillis != 0 && durationTimeMillis != 0) && (currentTimeMillis != -1 && durationTimeMillis != -1)) {
                        if (movieTimeSecond == (currentTimeMillis) / 1000 && movieTimeSecond != 0) {
                            requestVideoPoint();

                            btnSkip.setVisibility(View.VISIBLE);
                        } else if (movieTimeSecond == 0) {
                            if (currentTimeMillis == durationTimeMillis) {
                                requestVideoPoint();
                            }
                        }

                        if ((currentTimeMillis == durationTimeMillis) && (!movieEndUrl.isEmpty())) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieEndUrl));
                            startActivity(browserIntent);
                            finish();
                        } else if ((currentTimeMillis == durationTimeMillis) && (movieEndUrl.isEmpty())) {
                            finish();
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    public void requestVideoPoint() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://m.medayi.com/locknet/locknet_movie_point.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("113") || response.contains("110")) {
                            txtTimeSkip.setText("Points credited.");
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
                SharedPreferencesFile sharedPreferencesFile = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
                params.put("cash_slide_id", sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                params.put("cash_password", sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD));
                params.put("token_id", sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN));
                params.put("uid", getIntent().getStringExtra("uid"));
                params.put("lock_view_price", getIntent().getStringExtra("lock_view_price"));
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onStart() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//        int orientation = display.getRotation();
//
//        if(orientation== Surface.ROTATION_180)
//        {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//        }
        super.onStart();
    }

    @Override
    protected void onResume() {

//        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//        int orientation = display.getRotation();
//
//        if(orientation== Surface.ROTATION_180)
//        {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//        }

        super.onResume();

    }
}
