package com.askhmer.lockscreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.model.CompanyDto;

import java.util.List;

/**
 * Created by Longdy on 6/11/2016.
 */
public class LockScreenAdapter extends RecyclerView.Adapter<LockScreenAdapter.SimpleItemViewHolder> {

    private List<CompanyDto> items;
    private Context mContext;

    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;
        public SimpleItemViewHolder(View itemVew){
            super(itemVew);
            bannerImage = (ImageView) itemVew.findViewById(R.id.banner_image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LockScreenAdapter(List<CompanyDto> items) {
        this.items = items;
    }

    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new SimpleItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleItemViewHolder holder, int position) {
        holder.bannerImage.setImageResource(items.get(position).getImgTest());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
