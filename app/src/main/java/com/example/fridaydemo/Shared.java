package com.example.fridaydemo;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by a.dewan on 8/5/14.
 */
public class Shared extends Fragment {

    String USER_ID = Build.MODEL+Build.SERIAL;
    String SHARE_MANAGER ="http://4607d262.ngrok.com/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.shared, container, false);
        setPhotos(rootView);
        Toast.makeText(getActivity().getApplicationContext(),"Downloading files. Please be paitent!",Toast.LENGTH_SHORT).show();
        return rootView;
    }

    private void setPhotos(final View rootView){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SHARE_MANAGER+"images/shareto?to="+ URLEncoder.encode(USER_ID));
                StringBuilder builder =new StringBuilder();
                try {
                    HttpResponse response = client.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        InputStream content = entity.getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } else {
                        Log.e("ERROR IN READING DATA", "FAILED TO GET ANY DATA");
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray jsonArray = new JSONArray(builder.toString());
                    String [] imageURLs = new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("image");
                        imageURLs[i] = jsonObject.getString("url");
                    }

                    getPhotos(rootView, imageURLs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void getPhotos(View rootView, final String[] imageURLs) {
                Bitmap[] images = new Bitmap[imageURLs.length];

                for(int i=0;i<imageURLs.length;i++){
                   final int counter = i+1;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),"Downloading "+counter+" out of "+imageURLs.length+" images. Please be paitent!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    images[i] = getBitmapFromURL(imageURLs[i]);

                };
                populateGridView(rootView,images);
            }

            private void populateGridView(final View rootView, final Bitmap[] images) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final GridView gridView = (GridView)  rootView.findViewById(R.id.sharedlist);

                        gridView.setAdapter(new ContactAdapter(rootView.getContext(),images));
                    }
                });

            }

        }).start();
    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(SHARE_MANAGER+imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Bitmap.createScaledBitmap(myBitmap,800,800,true);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
