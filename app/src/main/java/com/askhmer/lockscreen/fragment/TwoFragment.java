package com.askhmer.lockscreen.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.activity.LiveStream;
import com.askhmer.lockscreen.constant.Constant;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.MutiLanguage;
import com.askhmer.lockscreen.utils.RealPathUtil;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private static final String TYPE_IMAGE = "image/*";
    private static final int INPUT_FILE_REQUEST_CODE = 1;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;


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

        webSetting(webview);

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
                    getActivity().setProgress(newProgress * 1000);
                    super.onProgressChanged(view, newProgress);
                    mProgress.setProgress(newProgress);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCloseWindow(WebView w) {
                super.onCloseWindow(w);
                getActivity().finish();
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

            // For Android Version < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                //System.out.println("WebViewActivity OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU), n=1");
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(TYPE_IMAGE);
                startActivityForResult(intent, INPUT_FILE_REQUEST_CODE);
            }

            // For 3.0 <= Android Version < 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                //System.out.println("WebViewActivity 3<A<4.1, OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU,aT), n=2");
                openFileChooser(uploadMsg, acceptType, "");
            }

            // For 4.1 <= Android Version < 5.0
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                Log.d(getClass().getName(), "openFileChooser : "+acceptType+"/"+capture);
                mUploadMessage = uploadFile;
                imageChooser();
            }

            // For Android Version 5.0+
            // Ref: https://github.com/GoogleChrome/chromium-webview-samples/blob/master/input-file-example/app/src/main/java/inputfilesample/android/chrome/google/com/inputfilesample/MainFragment.java
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                System.out.println("WebViewActivity A>5, OS Version : " + Build.VERSION.SDK_INT + "\t onSFC(WV,VCUB,FCP), n=3");
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;
                imageChooser();
                return true;
            }

            private void imageChooser() {
                Intent takePictureIntent =  new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(getClass().getName(), "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:"+photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType(TYPE_IMAGE);

                Intent[] intentArray;
                if(takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
            }
        });


        webview.setWebViewClient(new WebViewClient() {      //prevent open browser
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                mProgress.setVisibility(View.GONE);
                if (url.contains(".m3u8")) {
                    Intent intent = new Intent(getContext(), LiveStream.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
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

    /**
     * More info this method can be found at
     * http://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     *
     *
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mFilePathCallback == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri[] results = new Uri[]{getResultUri(data)};

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
            } else {
                if (mUploadMessage == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri result = getResultUri(data);

                Log.d(getClass().getName(), "openFileChooser : "+result);
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } else {
            if (mFilePathCallback != null) mFilePathCallback.onReceiveValue(null);
            if (mUploadMessage != null) mUploadMessage.onReceiveValue(null);
            mFilePathCallback = null;
            mUploadMessage = null;
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Uri getResultUri(Intent data) {
        Uri result = null;
        if(data == null || TextUtils.isEmpty(data.getDataString())) {
            // If there is not data, then we may have taken a photo
            if(mCameraPhotoPath != null) {
                result = Uri.parse(mCameraPhotoPath);
            }
        } else {
            String filePath = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                filePath = data.getDataString();
            } else {
                filePath = "file:" + RealPathUtil.getRealPath(getContext(), data.getData());
            }
            result = Uri.parse(filePath);
        }

        return result;
    }
}
