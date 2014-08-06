package com.example.fridaydemo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by a.dewan on 8/5/14.
 */
public class PeopleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.peoplefragment, container, false);

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.background);

        imageView.setImageResource(R.drawable.people_pre);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(),"Shared via SMS",Toast.LENGTH_SHORT).show();
                imageView.setImageResource(R.drawable.people_mid);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity().getApplicationContext(),"Shared via Whatsapp",Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.people);
                    }
                });
            }
        });



        return rootView;
    }
}
