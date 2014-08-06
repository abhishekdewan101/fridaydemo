package com.example.fridaydemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by a.dewan on 8/5/14.
 */
public class ShareAdapter extends BaseAdapter {
    Context context;

    Bitmap[] images;

    String[] to;

    public ShareAdapter(Context context,Bitmap[] images,String[] to){
        this.context = context;
        this.images = images;
        this.to = to;
        Log.e("Image Array Length:", images.length + "");
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



        View gridview;
        if (view == null) {
            gridview = new View(context);

            gridview = inflater.inflate(R.layout.customshare,null);

            TextView textView = (TextView) gridview.findViewById(R.id.imagevalues);
            textView.setText("Shared with "+to[i]);

            ImageView imageView = (ImageView) gridview.findViewById(R.id.imageshared);
            imageView.setImageBitmap(images[i]);
        } else {
            gridview = (View) view;
        }
        return gridview;
    }
}
