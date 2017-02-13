package com.askhmer.lockscreen.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.utils.MutiLanguage;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;

public class WebviewFun extends AppCompatActivity {

    private WebView webView;
    private ProgressBar mProgress;

    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(
                WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        this.getWindow().addFlags(
				/*WindowManager.LayoutParams.FLAG_FULLSCREEN
						|*/ WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );

        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_fun);

        webView = (WebView) findViewById(R.id.webview);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);

         /*website setting*/
        webSetting(webView);
        String url = "http://m.medayi.com/bbs/board.php?language=kh&bo_table=new_fun&wr_id=" + getIntent().getStringExtra("wr_id");
        webView.loadUrl(url);
    }

    public void webSetting(WebView webview2) {

        WebSettings webSettings = webview2.getSettings();
        /*webview2.addJavascriptInterface(true);*/
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

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                final WebSettings settings = view.getSettings();
                settings.setDomStorageEnabled(true);
                settings.setJavaScriptEnabled(true);
                settings.setAllowFileAccess(true);
                settings.setAllowContentAccess(true);
                view.setWebChromeClient(this);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);
                resultMsg.sendToTarget();
                return false;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
