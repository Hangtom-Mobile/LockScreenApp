package com.askhmer.lockscreen.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Recommend extends SwipeBackActivity {

    private Button btnOk;
    private EditText editTextRecom;
    private SharedPreferencesFile mSharedPreferencesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
//swipe back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        editTextRecom = (EditText) findViewById(R.id.et_recommend_id);
        btnOk = (Button) findViewById(R.id.btn_ok);
        mSharedPreferencesFile = new SharedPreferencesFile(getApplicationContext(),SharedPreferencesFile.FILE_INFORMATION_TEMP);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRecommendId();
            }
        });

        /*check recommend id have or not*/
        checkRecommendId();
    }

    public void checkRecommendId() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CHECKRECOMMEND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("112")) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);

                                editTextRecom.setText(jsonObj.getString("mb_recommend"));
                                editTextRecom.setEnabled(false);
                                findViewById(R.id.ve_password).setVisibility(View.VISIBLE);

                                btnOk.setEnabled(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Recommend.this, "No internet connection!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cash_slide_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void requestRecommendId() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTRECOMMENDID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("110")) {
                            messageDialog(getString(R.string.title_not_com), "Recommend id has been submit");
                        }else if(response.contains("111")) {
                            messageDialog(getString(R.string.title_not_com), "No Data");
                        }else if (response.contains("112")){
                            messageDialog(getString(R.string.title_not_com), "No have User Id");
                        }else if (response.contains("113")){
                            messageDialog(getString(R.string.title_not_com), "Your recommend id already submit for this user");
                        }else if (response.contains("114")){
                            messageDialog(getString(R.string.title_not_com), "No have recommend id");
                        }else if (response.contains("115")){
                            messageDialog(getString(R.string.title_not_com), "Can't submit your own id");
                        }else if (response.contains("116")){
                            messageDialog(getString(R.string.title_not_com), "One recommend id can apply only 10time per day");
                        }else if (response.contains("117")){
                            messageDialog(getString(R.string.title_not_com), "Your recommend id already submit for this user or can't recommend each other");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Recommend.this, "No internet connection!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cash_slide_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                params.put("cash_password", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PASSWORD));
                params.put("token_id", mSharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN));
                params.put("recommend_id", editTextRecom.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void messageDialog(String title, String desricption) {
        /*setup dialog*/
        final Dialog dialog = new Dialog(Recommend.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.message_dialog);

        /*blind view*/
        Button button = (Button)  dialog.findViewById(R.id.bttn_buy);
        TextView textView = (TextView) dialog.findViewById(R.id.txt_description);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txt_title);

        txtTitle.setText(title);
        textView.setText(desricption);

        /*listener*/
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
