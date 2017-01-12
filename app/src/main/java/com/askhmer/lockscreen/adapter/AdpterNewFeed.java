package com.askhmer.lockscreen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.model.VideoNewFeed;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by medayi01 on 1/12/2017.
 */

public class AdpterNewFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<VideoNewFeed> videoNewFeeds;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;

    public class MyViewHolderloading extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public MyViewHolderloading(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    public class MyViewHolderItem extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView title;

        public MyViewHolderItem(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_thumnail);
            title = (TextView) view.findViewById(R.id.txt_des);
        }
    }

    public AdpterNewFeed(List<VideoNewFeed> videoNewFeeds) {
        this.videoNewFeeds = videoNewFeeds;
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
            Picasso.with(myViewHolderItem.imageView.getContext()).load(videoNewFeed.getThumnail()).into(myViewHolderItem.imageView);
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
        if (isPositionHeader(position)) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
