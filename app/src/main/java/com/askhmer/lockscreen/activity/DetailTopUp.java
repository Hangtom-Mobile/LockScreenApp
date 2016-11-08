package com.askhmer.lockscreen.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.adapter.TopUpCompanyAdpter;
import com.askhmer.lockscreen.adapter.TopUpDetailAdpter;
import com.askhmer.lockscreen.model.TopUpCompany;
import com.askhmer.lockscreen.model.TopUpDetail;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.JsonConverter;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.LockscreenService;
import com.askhmer.lockscreen.utils.MutiLanguage;
import com.askhmer.lockscreen.utils.RecyclerItemClickListener;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;
import com.google.android.gcm.GCMRegistrar;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailTopUp extends SwipeBackActivity {

    private MutiLanguage mutiLanguage;
    private RecyclerView recyclerView;
    private List<TopUpDetail> topUpDetails = new ArrayList<TopUpDetail>();
    private TopUpDetailAdpter topUpDetailAdpter;
    private SharedPreferencesFile sharedPreferencesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_top_up);

        //swipe back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        /*request data to server*/
        mutiLanguage = new MutiLanguage(this, this);
        setupDataFromServer();

        /*set up recyler*/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        /*recyler listener*/
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                TopUpDetail upDetail = topUpDetails.get(position);
                                alertDialogBuy(upDetail);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
        );
    }

    private void setupDataFromServer() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTDETAILTOPUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("show_123", response);
                        if (!response.isEmpty()) {
                            try {
                                topUpDetails = new JsonConverter().toList(response, TopUpDetail.class);
                            }catch (Exception e) {

                            }finally {
                                if (topUpDetails != null) {
                                    topUpDetailAdpter = new TopUpDetailAdpter(topUpDetails, DetailTopUp.this);
                                    topUpDetailAdpter.notifyDataSetChanged();
                                    recyclerView.setAdapter(topUpDetailAdpter);
                                    pDialog.dismiss();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setupDataFromServer();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String lang = null;

                if (mutiLanguage.getLanguageCurrent().isEmpty() || mutiLanguage.getLanguageCurrent().equals("en")) {
                    lang = "en";
                }else {
                    lang = "kh";
                }
                params.put("language", lang);
                params.put("category_id", getIntent().getStringExtra("category_id"));
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void alertDialogBuy(final TopUpDetail topUpDetail) {
        /*setup dialog*/
        final Dialog dialog = new Dialog(DetailTopUp.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.alert_dialog_topup);

        /*call shared referance file */
        sharedPreferencesFile = new SharedPreferencesFile(this, SharedPreferencesFile.FILE_INFORMATION_TEMP);
        int myPoint = Integer.parseInt(sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_POINT).replace(",", ""));
        int topUpPoint = Integer.parseInt(topUpDetail.getTopUpPoint());
        String numberPhone = sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE);

        /*blind view*/
        TextView txtCardPrice = (TextView) dialog.findViewById(R.id.txt_card_price);
        TextView txtMyPoint = (TextView) dialog.findViewById(R.id.txt_mypoint);
        TextView txtUsed = (TextView) dialog.findViewById(R.id.txt_used);
        TextView txtTotal = (TextView) dialog.findViewById(R.id.txt_total);
        LinearLayout totalLayout = (LinearLayout) dialog.findViewById(R.id.li_total);
        TextView viTextView = (TextView) dialog.findViewById(R.id.vi_txt);
        TextView txtNumber = (TextView) dialog.findViewById(R.id.txt_numb);
        LinearLayout headerLayout = (LinearLayout) dialog.findViewById(R.id.li_header);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.image_head);
        Button button = (Button)  dialog.findViewById(R.id.bttn_buy);

        /*blind view with data*/
        Picasso.with(this).load(topUpDetail.getTopUpImage()).into(imageView);
        txtCardPrice.setText(topUpDetail.getTopUpCharge() + " $");
        txtNumber.setText(numberPhone);
        txtMyPoint.setText(myPoint + "");
        txtUsed.setText(topUpPoint + "");
        if (myPoint > topUpPoint) {
            myPoint = myPoint - topUpPoint;
            txtTotal.setText(myPoint + "");
        }else {
            totalLayout.setVisibility(View.GONE);
            viTextView.setVisibility(View.VISIBLE);
            button.setEnabled(false);
            button.setBackgroundResource(R.color.hintColor);

        }

        /*listener*/
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.bttn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*ask password to buy*/
                passwordDialog(topUpDetail);
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void passwordDialog(final TopUpDetail topUpDetail) {
        /*setup dialog*/
        final Dialog dialog = new Dialog(DetailTopUp.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.ask_password_alert);

        /*blind view*/
        Button button = (Button)  dialog.findViewById(R.id.bttn_buy);
        final EditText editTxtPassword = (EditText) dialog.findViewById(R.id.ed_password);

        /*listener*/
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.bttn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBuyTopUp(topUpDetail, editTxtPassword.getText().toString());
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void requestBuyTopUp(final TopUpDetail topUpDetail, final String password) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTBUYTOPUPCARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("show_123", response);
                        if (response.contains("110")) {
                            new SweetAlertDialog(DetailTopUp.this)
                                    .setTitleText("Top up success!")
                                    .show();
                            pDialog.dismiss();
                        }else if (response.contains("112")) {
                            new SweetAlertDialog(DetailTopUp.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("")
                                    .setContentText("Your information incorrect!")
                                    .show();
                            pDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*dismiss loading*/
                pDialog.dismiss();

                new SweetAlertDialog(DetailTopUp.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("")
                        .setContentText("Sorry! Please request again!")
                        .show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("topup_id", topUpDetail.getTopUpId());
                params.put("cash_slide_id", sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_CASHID));
                params.put("token_id", sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_TOKEN));
                params.put("cash_password", password);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
