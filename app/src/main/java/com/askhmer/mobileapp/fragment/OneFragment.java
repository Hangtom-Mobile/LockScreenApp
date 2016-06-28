package com.askhmer.mobileapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView txtMyPoint;

    public OneFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oneFragmentView = inflater.inflate(R.layout.fragment_one, container, false);
        mSharedPreferencesFile = SharedPreferencesFile.newInstance(getContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
        txtMyPoint = (TextView) oneFragmentView.findViewById(R.id.tv_mypoint);

        /*request my point*/
        requestMypointToServer();

        /*request auto login*/
       /* requestAutoLogin();*/

        return oneFragmentView;
    }

    public void requestMypointToServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTMYPOINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mypointrespone",response);
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
                Log.d("ErroVelloy", error.toString());
                Toast.makeText(getContext(), "One f my point"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cash_slide_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
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

    public void requestAutoLogin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTAUTOLOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("login",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErroVelloy", error.toString());
                Toast.makeText(getContext(), "one f login"+error.toString(), Toast.LENGTH_LONG).show();
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
}
