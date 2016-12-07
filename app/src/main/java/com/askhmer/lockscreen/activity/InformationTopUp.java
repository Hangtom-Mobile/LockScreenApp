package com.askhmer.lockscreen.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.model.TopUpDetail;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.SharedPreferencesFile;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InformationTopUp extends AppCompatActivity {

    private TextView txtReciever, txtMyPoint, txtPurchase, txtPriceCard, txtTitleToolbarName, txtTotal;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private Button btnPurChase;
    private SharedPreferencesFile sharedPreferencesFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_top_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                InformationTopUp.this.overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_right_to_left);
            }
        });

        /*init view*/
        initView();
    }

    private void initView() {
        txtTitleToolbarName = (TextView) findViewById(R.id.toolbar_title);
        txtReciever = (TextView) findViewById(R.id.txt_reciever);
        txtMyPoint = (TextView) findViewById(R.id.txt_mypoint);
        txtPurchase = (TextView) findViewById(R.id.txt_used_point);
        txtPriceCard = (TextView) findViewById(R.id.txt_name);
        btnPurChase = (Button) findViewById(R.id.btn_purchase);
        txtTotal = (TextView) findViewById(R.id.txt_total);

        sharedPreferencesFile = new SharedPreferencesFile(this, SharedPreferencesFile.FILE_INFORMATION_TEMP);
        String numberPhone = sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_INFORMATION_TEMP_PHONE);
        final TopUpDetail upDetail = (TopUpDetail) getIntent().getSerializableExtra("upDetail");

        int myPoint = Integer.parseInt(sharedPreferencesFile.getStringSharedPreference(SharedPreferencesFile.KEY_POINT).replace(",", ""));
        int purChase = Integer.parseInt(upDetail.getTopUpPoint());

        imageView = (ImageView) findViewById(R.id.image_view);
        linearLayout = (LinearLayout) findViewById(R.id.layout_card);

        txtTitleToolbarName.setText(upDetail.getTopName());
        linearLayout.setBackgroundColor(Color.parseColor(upDetail.getTopUpColor()));
        txtPriceCard.setTextColor(Color.parseColor(upDetail.gettColor()));
        txtPriceCard.setText(upDetail.getTopUpCharge() + "$");
        Picasso.with(this).load(upDetail.getTopUpImage()).into(imageView);
        txtReciever.setText(numberPhone);
        txtMyPoint.setText(myPoint + "");
        txtPurchase.setText(upDetail.getTopUpPoint());
        txtTotal.setText(String.valueOf(myPoint - purChase));

        if (myPoint < purChase) {
            btnPurChase.setEnabled(false);
            btnPurChase.setBackgroundResource(R.color.hintColor);
        }

        btnPurChase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordDialog(upDetail);
            }
        });
    }

    private void passwordDialog(final TopUpDetail topUpDetail) {
        /*setup dialog*/
        final Dialog dialog = new Dialog(InformationTopUp.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                            messageDialog(getString(R.string.title_completed), getString(R.string.descrip_completed));
                        }else if (response.contains("112")) {
                            messageDialog(getString(R.string.title_not_com), getString(R.string.your_information_incorrect));
                        }else if (response.contains("114")) {
                            messageDialog(getString(R.string.title_not_com), getString(R.string.descrip_not_com));
                        }else {
                            messageDialog(getString(R.string.title_not_com), getString(R.string.system_error));
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*dismiss loading*/
                pDialog.dismiss();
                messageDialog(getString(R.string.title_not_com), getString(R.string.please_request_again));
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

    private void messageDialog(String title, String desricption) {
        /*setup dialog*/
        final Dialog dialog = new Dialog(InformationTopUp.this);
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
