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
import android.widget.AdapterView;
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
import java.util.ArrayList;

/**
 * Created by a.dewan on 8/5/14.
 */
public class MyShares extends Fragment {

    String USER_ID = Build.MODEL+Build.SERIAL;
    String SHARE_MANAGER ="http://64d5993e.ngrok.com/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.myshares, container, false);
        setPhotos(rootView);
        Toast.makeText(getActivity().getApplicationContext(), "Downloading files. Please be paitent!", Toast.LENGTH_SHORT).show();
        return rootView;
    }

    private void setPhotos(final View rootView){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SHARE_MANAGER+"images/sharefrom?from="+ URLEncoder.encode(USER_ID));
                Log.e("URL ASKED OF SERVER",SHARE_MANAGER+"images/sharefrom?from="+ URLEncoder.encode(USER_ID));
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
                    String [] to = new String[jsonArray.length()];
                    String [] imageids = new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("image");
                        imageURLs[i] = jsonObject.getString("url");
                        to[i] = jsonArray.getJSONObject(i).getString("to");
                        imageids[i] = jsonArray.getJSONObject(i).getString("uuid");
                    }

                    getNames(rootView, imageURLs, to,imageids);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void getNames(View rootView, String[] imageURLs, String[] to,String[] imageids) {
                String names[] = new String[to.length];
                for(int i=0;i<names.length;i++){
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(SHARE_MANAGER+"users/present?userid="+ URLEncoder.encode(to[i]));
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
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        names[i] = jsonObject.getString("username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                getPhotos(rootView,imageURLs,names,imageids);
            }

            private void getPhotos(View rootView, final String[] imageURLs,final String[] to,final String[] imageids) {
                Bitmap[] images = new Bitmap[imageURLs.length];

                for(int i=0;i<imageURLs.length;i++){
                    final int counter = i+1;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(imageURLs != null) {
                                Toast.makeText(getActivity().getApplicationContext(), "Downloading " + counter + " out of " + imageURLs.length + " images. Please be paitent!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    images[i] = getBitmapFromURL(imageURLs[i]);

                };
                populateGridView(rootView,images,to,imageids);
            }

            private void populateGridView(final View rootView, final Bitmap[] images,final String[]to,final String[]imageids) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final GridView gridView = (GridView)  rootView.findViewById(R.id.mysharelist);

                        gridView.setAdapter(new ShareAdapter(getActivity().getApplicationContext(),images,to,imageids));

//                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
//                                deletePhoto(rootView,imageids[i]);
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        gridView.setAdapter(new UpdateAdapter(rootView.getContext(),getPhotoAfterDelete(images, i),getAfterDelete(to, i)));
//                                        synchronized (gridView) {
//                                            gridView.notify();
//                                        }
//                                    }
//                                });
//                            }
//
//
//                            private Object[] getPhotoAfterDelete(Bitmap[]array,int position){
//                                ArrayList<Bitmap> charList = new ArrayList<Bitmap>(0);
//
//                                for (int i= 0; i < array.length; i++) {
//                                    if (i != position) {
//                                        charList.add(array[i]);
//                                    }
//                                }
//
//                                return charList.toArray();
//                            }
//
//                            private Object[] getAfterDelete(String[]array,int position){
//                                ArrayList<String> charList = new ArrayList<String>(0);
//
//                                for (int i= 0; i < array.length; i++) {
//                                    if (i != position) {
//                                        charList.add(array[i]);
//                                    }
//                                }
//
//                               return charList.toArray();
//                            }
//                            private void deletePhoto(final View rootView,final String imageid) {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        HttpClient client = new DefaultHttpClient();
//                                        HttpGet httpGet = new HttpGet(SHARE_MANAGER+"images/delete?uuid="+ URLEncoder.encode(imageid));
//                                        try {
//                                            HttpResponse response = client.execute(httpGet);
//                                            StatusLine statusLine = response.getStatusLine();
//                                            int statusCode = statusLine.getStatusCode();
//                                            if (statusCode == 200) {
//
//                                            } else {
//                                                Log.e("ERROR IN READING DATA", "FAILED TO GET ANY DATA");
//                                            }
//                                        } catch (ClientProtocolException e) {
//                                            e.printStackTrace();
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }).start();
//                            }
//                        });
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
