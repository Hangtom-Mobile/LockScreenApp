package com.askhmer.lockscreen.fragment;



import android.app.Fragment;
import android.os.Bundle;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.adapter.AdpterNewFeed;
import com.askhmer.lockscreen.model.VideoNewFeed;

import java.util.ArrayList;


public class NativgationDrawerFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<VideoNewFeed> videoNewFeeds = new ArrayList<>();;
    private AdpterNewFeed adpterNewFeed;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nativgation_drawer, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        setupDataFromServer();

         /*set up recyler*/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    /***
     * request new data to server
     */
    private void setupDataFromServer() {
        videoNewFeeds.add(new VideoNewFeed("oVm7FkQI4BM", "SHAMPOO PRANK PART 7! | HoomanTV", "https://img.youtube.com/vi/oVm7FkQI4BM/0.jpg", "oVm7FkQI4BM"));
        videoNewFeeds.add(new VideoNewFeed("oVm7FkQI4BM", "SHAMPOO PRANK PART 7! | HoomanTV1", "https://img.youtube.com/vi/oVm7FkQI4BM/0.jpg", "oVm7FkQI4BM"));
        videoNewFeeds.add(new VideoNewFeed("oVm7FkQI4BM", "SHAMPOO PRANK PART 7! | HoomanTV2", "https://img.youtube.com/vi/oVm7FkQI4BM/0.jpg", "oVm7FkQI4BM"));
        videoNewFeeds.add(new VideoNewFeed("oVm7FkQI4BM", "SHAMPOO PRANK PART 7! | HoomanTV3", "https://img.youtube.com/vi/oVm7FkQI4BM/0.jpg", "oVm7FkQI4BM"));

        adpterNewFeed = new AdpterNewFeed(videoNewFeeds, getActivity());
        adpterNewFeed.notifyDataSetChanged();
        recyclerView.setAdapter(adpterNewFeed);
    }
}
