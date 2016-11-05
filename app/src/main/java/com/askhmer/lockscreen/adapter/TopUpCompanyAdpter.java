package com.askhmer.lockscreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.askhmer.lockscreen.R;
import com.askhmer.lockscreen.model.TopUpCompany;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by soklundy on 10/8/2016.
 */

public class TopUpCompanyAdpter extends RecyclerView.Adapter<TopUpCompanyAdpter.MyViewHolder>{


    private List<TopUpCompany> topUpCompanies;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            textView = (TextView) view.findViewById(R.id.txt_name);
        }
    }

    public TopUpCompanyAdpter(List<TopUpCompany> topUpCompanies, Context context) {
        this.topUpCompanies = topUpCompanies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_up_item_each_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TopUpCompany topUpCompany = topUpCompanies.get(position);
        holder.textView.setText(topUpCompany.getCategoryName());
        Picasso.with(holder.imageView.getContext()).load(topUpCompany.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return topUpCompanies.size();
    }

}
