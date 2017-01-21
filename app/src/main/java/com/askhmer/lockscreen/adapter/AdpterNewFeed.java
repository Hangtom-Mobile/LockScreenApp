package com.askhmer.lockscreen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.activity.YoutubePlayerNewFeed;
import com.askhmer.lockscreen.model.VideoNewFeed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by medayi01 on 1/12/2017.
 */

public class AdpterNewFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoNewFeed> videoNewFeeds;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;
    private Context context;
    private ProgressBar progressBar;
    private TextView textView;


    public class MyViewHolderloading extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;
        public TextView textView;

        public MyViewHolderloading(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
            textView = (TextView) view.findViewById(R.id.txt_no_more);
        }
    }

    public class MyViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public ImageView btnPlay, imageThumnail;

        public MyViewHolderItem(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_des);
            btnPlay = (ImageView) view.findViewById(R.id.play_btn);
            imageThumnail = (ImageView) view.findViewById(R.id.image_view);
            btnPlay.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String position = v.getTag().toString();
            Intent intent = new Intent(context, YoutubePlayerNewFeed.class);
            intent.putExtra("video_id", position);
            context.startActivity(intent);
        }
    }

    public AdpterNewFeed(List<VideoNewFeed> videoNewFeeds, Context context) {
        this.videoNewFeeds = videoNewFeeds;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyler_slide_right_lockscreen, parent, false);
            return  new MyViewHolderItem(v);
        }
        else if(viewType == TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
            return  new MyViewHolderloading(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolderItem) {
            MyViewHolderItem myViewHolderItem = (MyViewHolderItem)holder;
            VideoNewFeed videoNewFeed = getItem(position);

            myViewHolderItem.title.setText(videoNewFeed.getTitle());
            myViewHolderItem.btnPlay.setTag(videoNewFeed.getVideoId());
            Picasso.with(myViewHolderItem.imageThumnail.getContext()).load("https://img.youtube.com/vi/" + videoNewFeed.getVideoId() + "/0.jpg").into(myViewHolderItem.imageThumnail);
        }
        else if(holder instanceof MyViewHolderloading) {
            MyViewHolderloading myViewHolderloading = (MyViewHolderloading) holder;
            this.progressBar = myViewHolderloading.progressBar;
            this.textView = myViewHolderloading.textView;
        }

    }

    private VideoNewFeed getItem(int position) {
        return videoNewFeeds.get(position);
    }

    @Override
    public int getItemCount() {
        return videoNewFeeds.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    private boolean isFooter(int position) {
        return position==getItemCount()-1;
    }

    public void setData (ArrayList<VideoNewFeed> data) {
        videoNewFeeds.addAll(data);
        notifyDataSetChanged();
    }

    public void isDisplayLoading(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }
}
