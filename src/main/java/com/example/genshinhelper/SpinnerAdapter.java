package com.example.genshinhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private int[] images;

    public SpinnerAdapter(Context context, int[] images) {
        super(context, R.layout.spinner_item, new String[images.length]);
        this.context = context;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.spinner_item, parent, false);

        ImageView icon = view.findViewById(R.id.imageView);
        icon.setImageResource(images[position]);

        return view;
    }
}
