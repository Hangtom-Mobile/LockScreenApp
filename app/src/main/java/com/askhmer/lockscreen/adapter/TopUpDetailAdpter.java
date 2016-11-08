package com.askhmer.lockscreen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.model.TopUpCompany;
import com.askhmer.lockscreen.model.TopUpDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by soklundy on 10/8/2016.
 */

public class TopUpDetailAdpter extends RecyclerView.Adapter<TopUpDetailAdpter.MyViewHolder>{


    private List<TopUpDetail> topUpDetails;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            textView = (TextView) view.findViewById(R.id.txt_name);
            linearLayout = (LinearLayout) view.findViewById(R.id.li_back_color);
        }
    }

    public TopUpDetailAdpter(List<TopUpDetail> topUpDetails, Context context) {
        this.topUpDetails = topUpDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_item_each_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TopUpDetail upDetail = topUpDetails.get(position);
        holder.textView.setText(upDetail.getTopUpCharge() + " $");
        Picasso.with(holder.imageView.getContext()).load(upDetail.getTopUpImage()).into(holder.imageView);
        holder.linearLayout.setBackgroundColor(Color.parseColor(upDetail.getTopUpColor()));

    }

    @Override
    public int getItemCount() {
        return topUpDetails.size();
    }

}
