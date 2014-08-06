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
public class AppFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.appfragment, container, false);

            final ImageView imageView = (ImageView) rootView.findViewById(R.id.appbackground);

            imageView.setImageResource(R.drawable.app_pre);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity().getApplicationContext(), "Shared via Facebook", Toast.LENGTH_SHORT).show();
                    imageView.setImageResource(R.drawable.app_mid);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity().getApplicationContext(),"Shared via Twitter",Toast.LENGTH_SHORT).show();
                            imageView.setImageResource(R.drawable.app);
                        }
                    });
                }
            });

            return rootView;
        }
}
