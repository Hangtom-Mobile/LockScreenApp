package com.askhmer.lockscreen.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NewPasswordForFind extends AppCompatActivity {

    private Button btnSub;
    private EditText editTextPass, editTextCom;
    private TextView txtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pw_find);

        btnSub = (Button) findViewById(R.id.bttn_sub);
        editTextPass = (EditText) findViewById(R.id.et_password);
        editTextCom = (EditText) findViewById(R.id.et_con_pw);
        txtV = (TextView) findViewById(R.id.ve_pass_id);

        editTextCom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = editTextPass.getText().toString();
                String conPassword = editTextCom.getText().toString();

                if(conPassword.length() < 4) {
                    txtV.setText(R.string.pwd_4_char);
                    txtV.setVisibility(View.VISIBLE);
                }else {
                    txtV.setVisibility(View.GONE);
                    if(!password.equals(conPassword)){
                        txtV.setText("Password and Confirm password not match");
                        txtV.setVisibility(View.VISIBLE);
                    }else {
                        txtV.setVisibility(View.GONE);
                    }
                }
            }
        });

        /*password*/
        editTextPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = editTextPass.getText().toString();
                String conPassword = editTextCom.getText().toString();

                if (password.length() < 4) {
                    txtV.setText("Password at least 4 characters");
                    txtV.setVisibility(View.VISIBLE);
                } else {
                    txtV.setVisibility(View.GONE);
                    if (!password.equals(conPassword)) {
                        txtV.setText("Password and Confirm password not match");
                        txtV.setVisibility(View.VISIBLE);
                    } else {
                        txtV.setVisibility(View.GONE);
                    }
                }
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtV.getVisibility() == View.GONE &&
                        editTextCom.getText().toString().equals(editTextPass.getText().toString())) {
                    if (editTextPass.getText().toString().isEmpty() || editTextCom.getText().toString().isEmpty()) {
                        txtV.setVisibility(View.VISIBLE);
                    }else {
                        changePassword();
                    }
                }
            }
        });
    }

    public void changePassword() {
         /*loading message*/
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTPASSWORDSTEP2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("result", response);
                        pDialog.hide();
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String result = jsonObj.getString("rst");
                                if (result.equals("110")) {
                                    new SweetAlertDialog(NewPasswordForFind.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("completed!")
                                            .setContentText("Find password has been success! \n Do you want to login?")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                    NewPasswordForFind.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_in);

                                                }
                                            })
                                            .show();
                                }else {
                                    Toast.makeText(NewPasswordForFind.this, "Please find your password again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Intent intent = getIntent();
                params.put("cash_slide_id", intent.getStringExtra("cash_id"));
                params.put("new_passwd", editTextPass.getText().toString());
                params.put("secret_code",intent.getStringExtra("secret_code"));
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}
