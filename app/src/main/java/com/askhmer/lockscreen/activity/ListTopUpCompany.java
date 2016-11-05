package com.askhmer.lockscreen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.adapter.TopUpCompanyAdpter;
import com.askhmer.lockscreen.model.TopUpCompany;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.JsonConverter;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ListTopUpCompany extends AppCompatActivity {

    private RecyclerView recyclerViewGrid;
    private List<TopUpCompany> topUpCompanies = new ArrayList<TopUpCompany>();
    private TopUpCompanyAdpter adpter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_top_up_company);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerViewGrid = (RecyclerView) findViewById(R.id.recycler);

        /*request data from server and set up recyler*/
        setupDataFromServer();

        GridLayoutManager gridLayout = new GridLayoutManager(this, 2);
        recyclerViewGrid.setLayoutManager(gridLayout);
        recyclerViewGrid.setItemAnimator(new DefaultItemAnimator());

        recyclerViewGrid.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerViewGrid,
                        new RecyclerItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Log.e("show_123", position+"");
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void setupDataFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.REQUESTCOMPANYTOPUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {
                            try {
                                topUpCompanies = new JsonConverter().toList(response, TopUpCompany.class);
                            }catch (Exception e) {

                            }finally {
                                if (topUpCompanies != null) {
                                    adpter = new TopUpCompanyAdpter(topUpCompanies, ListTopUpCompany.this);
                                    adpter.notifyDataSetChanged();
                                    recyclerViewGrid.setAdapter(adpter);
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
