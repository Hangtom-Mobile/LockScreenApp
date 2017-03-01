package com.askhmer.lockscreen.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.activity.LiveStream;
import com.askhmer.lockscreen.fragment.ThreeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by soklundy on 2/24/2017.
 */

public class WebAppInterface {
    private Context mContext;
    private Activity mActivity;

    public WebAppInterface(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    @JavascriptInterface
    public void loadUrl(final String url) {
        new AsyncTask<String, String, String>(){
            @Override
            protected String doInBackground(String... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(String s) {
                alertWebview(s);
            }
        }.execute(url);
    }

    private void alertWebview(final String url) {
        /*repair data*/

        /*setup dialog*/
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));*/
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.alert_webview);

        /*blind view*/
        final WebView webView = (WebView) dialog.findViewById(R.id.webview);
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);

        webSetting(webView, progressBar);
        webView.loadUrl(url);

        dialog.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(url);
            }
        });

        dialog.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        /*listener*/
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void webSetting(WebView webview2, final ProgressBar progressBar) {

        WebSettings webSettings = webview2.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptEnabled(true);                        // javascript use flag
        webSettings.setSavePassword(false);                            // web password auto save
        //webSettings.setSupportMultipleWindows(true);                    // new window
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
                    mActivity.setProgress(newProgress * 1000);
                    super.onProgressChanged(view, newProgress);
                    progressBar.setProgress(newProgress);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCloseWindow(WebView w) {
                super.onCloseWindow(w);
                mActivity.finish();
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
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    progressBar.setVisibility(View.GONE);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.stopLoading();
                progressBar.setVisibility(View.GONE);

                //Debug mode
               /* Log.e("onErrorCode :", "" + errorCode);
                Log.e("onDescription :", description);
                Log.e("onFailingUrl :", failingUrl);*/

                switch (errorCode) {
                    case ERROR_AUTHENTICATION:
                        break;//서버에서 사용자 인증 실패
                    case ERROR_BAD_URL:
                        break;//잘못된 url
                    case ERROR_CONNECT:
                        break;//서버로 연결 실패
                    case ERROR_FAILED_SSL_HANDSHAKE:
                        break;//SSL handshake 수행 실패
                    case ERROR_FILE:
                        break;//일반 파일 오류
                    case ERROR_FILE_NOT_FOUND:
                        break;//파일을 찾을 수 업습니다.
                    case ERROR_HOST_LOOKUP:
                        break;//서버 또는 프록시 호스트 이름 조회 실패
                    case ERROR_IO:
                        break;//서버에서 읽거나 서버로 쓰기 실패
                    case ERROR_PROXY_AUTHENTICATION:
                        break;//프록시 사용자 인증 실패
                    case ERROR_REDIRECT_LOOP:
                        break;//너무 많은 리다이렉션
                    case ERROR_TIMEOUT:
                        break;//연결 시간 초과
                    case ERROR_TOO_MANY_REQUESTS:
                        break;//페이지 로드중 너무 많은 요청 발생
                    case ERROR_UNKNOWN:
                        break;//일반 오류
                    case ERROR_UNSUPPORTED_AUTH_SCHEME:
                        break;//지원되지 않은 인증 체제
                    case ERROR_UNSUPPORTED_SCHEME:
                        break;//URI가 지원되지 않는 방식
                }
            }
        });
    }
}
