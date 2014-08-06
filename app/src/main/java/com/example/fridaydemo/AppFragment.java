package com.example.fridaydemo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by a.dewan on 8/5/14.
 */
public class AppFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.appfragment, container, false);

            ((ImageView)rootView.findViewById(R.id.appbackground)).setImageResource(R.drawable.back);

            return rootView;
        }
}
