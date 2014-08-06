package com.example.fridaydemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by a.dewan on 8/5/14.
 */
public class ImageAdapter extends BaseAdapter {

    Context context;

    Object[] numbers;
    public ImageAdapter(Context context,Object[] numbers){
        this.context = context;
        this.numbers = numbers;
    }


    @Override
    public int getCount() {
        return numbers.length;
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

        View gridView;

        if (view == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.customcontact, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.contactname);
            textView.setText(numbers[i].toString());

//            final ImageView imageView = (ImageView)gridView.findViewById(R.id.imageshared);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    imageView.setImageResource(R.drawable.device2);
//                    imageView.setEnabled(false);
//                }
//            });

        } else {
            gridView = (View) view;
        }

        return gridView;
    }
}
