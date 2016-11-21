package com.askhmer.lockscreen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.askhmer.lockscreen.R;

import java.util.zip.Inflater;


/**
 * Created by soklundy on 11/17/2016.
 */

public class SpinnerAdpt extends ArrayAdapter {

    private Context context;
    private String[] data;
    private int[] image;
    private LayoutInflater inflater;

    public SpinnerAdpt(Context context, int textViewResourceId ,String[] text, int[] image) {
        super(context, textViewResourceId, text);

        this.context = context;
        data = text;
        this.image = image;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        View layout = inflater.inflate(R.layout.layout_spinner, parent, false);

        TextView textView = (TextView) layout.findViewById(R.id.text_view);
        ImageView imageView = (ImageView)layout.findViewById(R.id.image_view);

        textView.setText(data[position]);
        imageView.setImageResource(image[position]);

        return layout;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

}
