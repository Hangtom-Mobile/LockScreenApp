package com.askhmer.mobileapp.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.mobileapp.R;
import com.askhmer.mobileapp.constant.Constant;
import com.askhmer.mobileapp.network.API;
import com.askhmer.mobileapp.network.MySingleton;
import com.askhmer.mobileapp.utils.MutiLanguage;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.util.EncodingUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Longdy on 6/22/2016.
 */
public class TwoFragment extends Fragment /*implements SwipeRefreshLayout.OnRefreshListener*/{

    private WebView webview;
    private final String URL_ASKHMER = "http://m.medayi.com/index.php?language=kh&medayi_app=yes";
    private final String URL_ASKHMER_EN = "http://m.medayi.com/index.php?language=en&medayi_app=yes";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar mProgress;
    private ImageButton btnBack;
    private ImageButton btnForward;
    private ImageButton btnHome;
    private ImageButton btnRefresh;
    private SharedPreferencesFile mSharedPreferencesFile;
    private String mbId,password,token,postData;
    private MutiLanguage mutiLanguage;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public TwoFragment(){}

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View twoFragmentView = inflater.inflate(R.layout.fragment_two, container, false);
        webview = (WebView) twoFragmentView.findViewById(R.id.webview);
        webSetting();
//init
        mProgress = (ProgressBar) twoFragmentView.findViewById(R.id.progressBar);
        btnBack = (ImageButton) twoFragmentView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(btnTouchListener);
        btnForward = (ImageButton) twoFragmentView.findViewById(R.id.btnForward);
        btnForward.setOnClickListener(btnTouchListener);
        btnHome = (ImageButton) twoFragmentView.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(btnTouchListener);
        btnRefresh = (ImageButton) twoFragmentView.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(btnTouchListener);
        mSharedPreferencesFile = new SharedPreferencesFile(getContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

      /*  swipeRefreshLayout = (SwipeRefreshLayout) twoFragmentView.findViewById(R.id.swipe_refresh_layout);
        // sets the colors used in the refresh animation
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_bright, R.color.green_light,
                R.color.orange_light, R.color.red_light);

        swipeRefreshLayout.setOnRefreshListener(this);*/

        mbId = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID);
        password = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD);
        token = mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN);

        postData = "cash_slide_id="+mbId+"&cash_password="+password+"&token_id="+token;

        mutiLanguage = new MutiLanguage(getContext(), getActivity());
        String lang = mutiLanguage.getLanguageCurrent();

        if (lang.equals("en") || lang.isEmpty()) {
            webview.postUrl(URL_ASKHMER_EN, EncodingUtils.getBytes(postData, "base64"));
        }else {
            webview.postUrl(URL_ASKHMER, EncodingUtils.getBytes(postData, "base64"));
        }



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(getActivity()).addApi(AppIndex.API).build();
        return twoFragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View.OnClickListener btnTouchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnBack:
                    if (webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        /*if (!webview.getOriginalUrl().contains("bbs")&&webview.getOriginalUrl().contains("m.askhmer.com")) {
                            webview.loadUrl(Constant.MAIN_URL);
                        }*/
                    }
                    break;
                case R.id.btnForward:
                    if (webview.canGoForward()) {
                        webview.goForward();
                    }
                    break;
                case R.id.btnHome:
                    if (mutiLanguage.getLanguageCurrent().equals("en") || mutiLanguage.getLanguageCurrent().isEmpty()) {
                        webview.postUrl(URL_ASKHMER_EN, EncodingUtils.getBytes(postData, "base64"));
                    }else {
                        webview.postUrl(URL_ASKHMER, EncodingUtils.getBytes(postData, "base64"));
                    }
                    break;

                case R.id.btnRefresh:
                    if (webview.getUrl().equals("") || webview.getUrl().equals(null)) {
                        webview.loadUrl(Constant.MAIN_URL);
                    } else {
                        webview.reload();
                    }
                    break;
            }
        }
    };

  /*  @Override
    public void onRefresh() {

        *//*swipeRefreshLayout.setRefreshing(true);
        webview.loadUrl(webview.getUrl());
        swipeRefreshLayout.setRefreshing(false);*//*

    }*/


    public void webSetting() {

        WebSettings webSettings = webview.getSettings();
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

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                try {
                    getActivity().setProgress(newProgress * 1000);
                    super.onProgressChanged(view, newProgress);
                    mProgress.setProgress(newProgress);
                }catch (Exception e) {

                }
            }

        });


        webview.setWebViewClient(new WebViewClient() {      //prevent open browser
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                mProgress.setVisibility(View.GONE);
                if (url.indexOf("tel:") > -1) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                    return true;
                } else {
                    return true;
                }
            }

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


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Webview Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.askhmer.mobileapp/http/host/path")
        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Webview Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.askhmer.mobileapp/http/host/path")
        );
//        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void requestAutoLogin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTAUTOLOGIN,
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
                params.put("cash_slide_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                params.put("cash_password", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD));
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
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*webview.onPause();*/
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                webview.onPause();
            }else {
                webview.onResume();
            }
        }
    }
}
