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
public class PeopleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.peoplefragment, container, false);

        ((ImageView)rootView.findViewById(R.id.background)).setImageResource(R.drawable.back);

        return rootView;
    }
}
