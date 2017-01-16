package com.askhmer.lockscreen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.activity.YoutubePlayerNewFeed;
import com.askhmer.lockscreen.model.VideoNewFeed;
import com.askhmer.lockscreen.utils.Config;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by medayi01 on 1/12/2017.
 */

public class AdpterNewFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoNewFeed> videoNewFeeds;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;
    private Context context;

    public class MyViewHolderloading extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public MyViewHolderloading(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
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
            myViewHolderloading.progressBar.setIndeterminate(true);
        }

    }

    private VideoNewFeed getItem(int position) {
        return videoNewFeeds.get(position);
    }

    @Override
    public int getItemCount() {
        return videoNewFeeds.size();
    }

    @Override
    public int getItemViewType(int position) {
        return videoNewFeeds.get(position) == null ? TYPE_LOADING : TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
