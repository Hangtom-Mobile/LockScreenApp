package com.askhmer.mobileapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.askhmer.mobileapp.network.API;
import com.askhmer.mobileapp.network.MySingleton;
import com.askhmer.mobileapp.utils.NetworkUtil;
import com.askhmer.mobileapp.utils.SharedPreferencesFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Longdy on 6/22/2016.
 */
public class OneFragment extends Fragment {

    private SharedPreferencesFile mSharedPreferencesFile;
    private TextView txtMyPoint, txtMyUserName;
    private LinearLayout medayiPage, medayiSharing;

    public OneFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oneFragmentView = inflater.inflate(R.layout.fragment_one, container, false);
        mSharedPreferencesFile = new SharedPreferencesFile(getContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
        txtMyPoint = (TextView) oneFragmentView.findViewById(R.id.tv_mypoint);
        txtMyUserName = (TextView) oneFragmentView.findViewById(R.id.txt_user_name);

        checkInternetCon();

        medayiPage = (LinearLayout)oneFragmentView.findViewById(R.id.medayi_news);
        medayiSharing = (LinearLayout) oneFragmentView.findViewById(R.id.medayi_sharing);

        medayiPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/medayicambodia/"));
                startActivity(browserIntent);
            }
        });

        medayiSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.askhmer.lockscreen");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        /*set shared preferencesfile of application version for update app*/


        /*request auto login*/
       /* requestAutoLogin();*/

        return oneFragmentView;
    }

    public void medayiSharing(){

        new AlertDialog.Builder(getActivity())
                .setTitle("Medayi Sharing")
                .setMessage("Coming soon...")
                .setCancelable(true)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    public void checkInternetCon() {
        int networkType = NetworkUtil.getNetworkType(getActivity());
        NetworkInfo.State networkState = NetworkUtil.getNetworkState(getActivity());

        if (networkType == -1 || networkState == NetworkInfo.State.UNKNOWN) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.app_name)
                    .setMessage("No internet Connection.\n" +
                            "Check your internet connection and click Refresh")
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
                            getActivity().finish();
                        }
                    }).show();
        } else requestMypointToServer();

    }

    public void requestMypointToServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTMYPOINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String point = jsonObj.getString("rst");
                                if (point.length() > 6) {
                                   txtMyPoint.setTextSize(35);
                                }
                                txtMyPoint.setText(point);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
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
                mSharedPreferencesFile = new SharedPreferencesFile(getContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
                params.put("cash_slide_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                return params;
            }
           /* @Override
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
            }*/
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
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
    public void onStart() {
        super.onStart();

        /*load user name*/
        txtMyUserName.setText(mSharedPreferencesFile.getStringSharedPreference
                (SharedPreferencesFile.KEY_INFORMATION_TEMP_NAME));

        /*request my point*/
        requestMypointToServer();
    }
}
