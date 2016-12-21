package com.askhmer.lockscreen.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.askhmer.lockscreen.model.TopUpDetail;
import com.askhmer.lockscreen.network.API;
import com.askhmer.lockscreen.network.JsonConverter;
import com.askhmer.lockscreen.network.MySingleton;
import com.askhmer.lockscreen.utils.RecyclerItemClickListener;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.util.ArrayList;
import java.util.List;

public class ListTopUpCompany extends SwipeBackActivity {

    private RecyclerView recyclerViewGrid;
    private List<TopUpCompany> topUpCompanies = new ArrayList<TopUpCompany>();
    private TopUpCompanyAdpter adpter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_top_up_company);

        //swipe back
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerViewGrid = (RecyclerView) findViewById(R.id.recycler);

        /*request data from server and set up recyler*/
        setupDataFromServer();

        GridLayoutManager gridLayout = new GridLayoutManager(this, 2);
        recyclerViewGrid.setLayoutManager(gridLayout);
        recyclerViewGrid.setItemAnimator(new DefaultItemAnimator());

        recyclerViewGrid.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerViewGrid, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ListTopUpCompany.this, DetailTopUp.class);
                intent.putExtra("category_id", topUpCompanies.get(position).getCategoryId());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void setupDataFromServer() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

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
                                    pDialog.dismiss();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
