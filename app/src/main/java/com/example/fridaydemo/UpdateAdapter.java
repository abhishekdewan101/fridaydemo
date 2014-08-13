package com.example.fridaydemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by a.dewan on 8/5/14.
 */
public class UpdateAdapter extends BaseAdapter {
    Context context;

    Object[] images;

    String SHARE_MANAGER ="http://64d5993e.ngrok.com/";

    Object[] to;

    public UpdateAdapter(Context context,Object[] images,Object[] to){
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
            textView.setText("Shared with "+to[i].toString());

            ImageView imageView = (ImageView) gridview.findViewById(R.id.imageshared);
            imageView.setImageBitmap((Bitmap)images[i]);
        } else {
            gridview = (View) view;
        }
        return gridview;
    }

    private void deletePhoto(final View rootView,final String imageid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SHARE_MANAGER+"images/delete?uuid="+ URLEncoder.encode(imageid));
                try {
                    HttpResponse response = client.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if (statusCode == 200) {

                    } else {
                        Log.e("ERROR IN READING DATA", "FAILED TO GET ANY DATA");
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
