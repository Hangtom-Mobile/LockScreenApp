package com.askhmer.lockscreen.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.utils.MutiLanguage;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.IOException;

public class History extends AppCompatActivity {

    private WebView webView;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        webView = (WebView) findViewById(R.id.webview);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);

        /*set event on arrow back*/
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                History.this.overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_right_to_left);
            }
        });

        /*website setting*/
        webSetting(webView);

        MutiLanguage mutiLanguage = new MutiLanguage(this, this);
        String lang = mutiLanguage.getLanguageCurrent();
        String url = "";
        String cashId = new SharedPreferencesFile(this,SharedPreferencesFile.FILE_INFORMATION_TEMP)
                .getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID);


        if (lang.equals("en") || lang.isEmpty()) {
            url = "http://m.medayi.com/locknet/locknet_used_list.php?cash_slide_id=" + cashId + "&language=en";
        }else {
            url = "http://m.medayi.com/locknet/locknet_used_list.php?cash_slide_id=" + cashId + "&language=kh";
        }
        webView.loadUrl(url);


    }

    public void webSetting(WebView webview2) {

        WebSettings webSettings = webview2.getSettings();
        /*webview.addJavascriptInterface(true);*/
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);                        // javascript use flag
        webSettings.setSavePassword(false);                            // web password auto save
//        webSettings.setSupportMultipleWindows(true);                    // new window
        webSettings.setDatabaseEnabled(false);                            // html5 Web DB
        webSettings.setAppCacheEnabled(true);                            // html5 AppCache
        webSettings.setDomStorageEnabled(true);                            // html5 DOM key/value store -> not delete
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);    // javascript use -> new window open
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDefaultTextEncodingName("euc-kr");
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webview2.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                try {
                    setProgress(newProgress * 1000);
                    super.onProgressChanged(view, newProgress);
                    mProgress.setProgress(newProgress);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCloseWindow(WebView w) {
                super.onCloseWindow(w);
                finish();
            }
        });


        webview2.setWebViewClient(new WebViewClient() {      //prevent open browser

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgress.setVisibility(View.VISIBLE);
                mProgress.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.stopLoading();
                mProgress.setVisibility(View.GONE);
            }
        });
    }
}
