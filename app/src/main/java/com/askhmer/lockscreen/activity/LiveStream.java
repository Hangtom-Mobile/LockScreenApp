package com.askhmer.lockscreen.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.WindowManager;
import android.widget.Toast;

import com.askhmer.lockscreen.R;


public class LiveStream extends Activity {

    private android.widget.VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);

        final ProgressDialog pDialog = new ProgressDialog(LiveStream.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        mVideoView = (android.widget.VideoView) findViewById(R.id.vitamio_videoView);
        Uri vidUri = Uri.parse(getIntent().getStringExtra("url"));
        mVideoView.setVideoURI(vidUri);
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pDialog.hide();
            }
        });
    }

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
}
