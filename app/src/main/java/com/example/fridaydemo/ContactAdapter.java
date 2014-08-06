package com.example.fridaydemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by a.dewan on 8/5/14.
 */
public class ContactAdapter extends BaseAdapter {
    Context context;

    Bitmap[] images;

    public ContactAdapter(Context context,Bitmap[] images){
        this.context = context;
        this.images = images;
    }


    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageView imageView;

        Log.e("Custom Image Value",i+"");
        if (view == null) {
        imageView = new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(800,800));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8,8,8,8);
        } else {
            imageView = (ImageView) view;
        }
        imageView.setImageBitmap(images[i]);
        return imageView;
    }
}
