package com.askhmer.lockscreen.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.adapter.SpinnerAdpt;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.CheckInternet;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MoneyBalance extends SwipeBackActivity {

    private EditText etPoint, etWingAccount;
    private TextView txtVeri, txtYourBalance;
    private Button btnConfirm;
    private SharedPreferencesFile mSharedPreferencesFile;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_balance);
//swap back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        mSharedPreferencesFile = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);
        etPoint = (EditText) findViewById(R.id.et_point);
        etWingAccount = (EditText) findViewById(R.id.et_wing_account);
        txtVeri = (TextView) findViewById(R.id.ve_password);
        txtYourBalance = (TextView) findViewById(R.id.tv_your_balance);
        btnConfirm = (Button) findViewById(R.id.btn_next_money);
        spinner = (Spinner) findViewById(R.id.sp_type_acc);

        /*set up spinner account type*/
        String[] text = {"Wing", "True Money"};
        int[] image = {R.drawable.ic_wing, R.drawable.icon_true_money};
        spinner.setAdapter(new SpinnerAdpt(this, R.layout.layout_spinner, text, image));

        /*set number phone*/
        /*call shared referance file */
        SharedPreferencesFile sharedPreferencesFile = new SharedPreferencesFile(this, SharedPreferencesFile.FILE_INFORMATION_TEMP);
        String numberPhone = sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE);
        etWingAccount.setText(numberPhone);

        /*request my point to server*/
        requestMypointToServer();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestExChangPointToServer();
            }
        });
    }

    public void requestExChangPointToServer() {
        if (new CheckInternet().isConnect(getApplicationContext()) == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTEXCHANGPOINT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("change", response);
                            try {
                                if (!response.isEmpty()) {
                                    JSONObject jsonObj = new JSONObject(response);
                                    String result =jsonObj.getString("rst");
                                    if (result.equals("110")) {
                                        message();
                                    }else if (result.equals("115")) {
                                        txtVeri.setText("Your request point change is bigger than your current point");
                                        txtVeri.setVisibility(View.VISIBLE);
                                    }else if (result.equals("114")) {
                                        txtVeri.setText("Your point make be bigger or equal 80000");
                                        txtVeri.setVisibility(View.VISIBLE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    params.put("cash_slide_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                    params.put("cash_password", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD));
                    params.put("token_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN));
                    params.put("withdraw", etPoint.getText().toString());
                    params.put("wdr_company", spinner.getSelectedItem().toString().toLowerCase().replace(" ",""));
                    params.put("wing_account", etWingAccount.getText().toString());
                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        } else {
            txtVeri.setText("Please check your internet connection!");
            txtVeri.setVisibility(View.VISIBLE);
        }
    }

    private void message() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // Setting Dialog Title
        alertDialogBuilder.setTitle("Confirmation");

        // set dialog message
        alertDialogBuilder
                .setMessage("Your request exchange money is under our review. We'll send message and notification to your after our review!")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        MoneyBalance.this.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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
                                    txtYourBalance.setTextSize(35);
                                }
                                txtYourBalance.setText(point);
                                mSharedPreferencesFile.putStringSharedPreference(SharedPreferencesFile.KEY_POINT, point);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
