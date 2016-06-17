package com.askhmer.mobileapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.constant.Constant;
import com.askhmer.mobileapp.utils.NetworkUtil;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

public class WebviewActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private WebView webview;
    private final String URL_ASKHMER = Constant.MAIN_URL;
    private SharedPreferencesFile mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webview = (WebView)findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);   //allow javascript to run
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                WebviewActivity.this.setProgress(newProgress * 1000);
            }
        });

        checkInternetCon();
        mSharedPref  = SharedPreferencesFile.newInstance(this, SharedPreferencesFile.PREFER_KEY);

        // Restore preferences
        boolean lockScreen = mSharedPref.getBooleanSharedPreference(SharedPreferencesFile.PREFER_KEY);

        if(!lockScreen==true){
            Intent i = new Intent(WebviewActivity.this, LockScreenActivity.class);
            startActivity(i);
        }


        webview.setWebViewClient(new WebViewClient() {      //prevent open browser
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if(errorCode != 0){
                    Toast.makeText(getApplicationContext(),
                            "Please, make sure your internet connection work well.",
                            Toast.LENGTH_LONG).show();
                }
                //Debug mode
                Log.e("onErrorCode :", ""+errorCode);
                Log.e("onDescription :", description);
                Log.e("onFailingUrl :", failingUrl);
            }
        });
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        webview.loadUrl(URL_ASKHMER);
    }

    public void checkInternetCon(){
        int networkType =  NetworkUtil.getNetworkType(this);
        NetworkInfo.State networkState = NetworkUtil.getNetworkState(this);

        if(networkType == -1 || networkState == NetworkInfo.State.UNKNOWN) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(Constant.NETWORK_ERROR)
                    .setCancelable(true)
                    .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkInternetCon();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }else webview.loadUrl(URL_ASKHMER);
    }

    //Enable backward
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack())
        {
            webview.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void onRefresh() {
        webview.loadUrl(URL_ASKHMER);
    }
}
