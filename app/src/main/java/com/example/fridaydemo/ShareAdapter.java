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
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by a.dewan on 8/5/14.
 */
public class ShareAdapter extends BaseAdapter {
    Context context;

    String SHARE_MANAGER ="http://64d5993e.ngrok.com/";

    ArrayList<Bitmap> images = new ArrayList<Bitmap>();
    ArrayList<String> to = new ArrayList<String>();
    ArrayList<String> imageids = new ArrayList<String>();

    ShareAdapter adapter = this;

    public ShareAdapter(Context context,Bitmap[] images,String[] to,String[] imageids){
        this.context = context;
        for(int i =0;i<images.length;i++) {
            this.images.add(i,images[i]);
            this.to.add(i,to[i]);
            this.imageids.add(i,imageids[i]);
        }

        Log.e("Image Array Length:",this.to.toString());
    }


    @Override
    public int getCount() {
        return images.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        View gridview;
        if (view == null) {
            gridview = new View(context);

            gridview = inflater.inflate(R.layout.customshare,null);

            TextView textView = (TextView) gridview.findViewById(R.id.imagevalues);
            textView.setText("Shared with "+to.get(i));

            ImageView imageView = (ImageView) gridview.findViewById(R.id.imageshared);
            imageView.setImageBitmap(images.get(i));

        } else {
            gridview = (View) view;
        }

        gridview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePhoto(imageids.get(i));
                Toast.makeText(context, "Stopping Share", Toast.LENGTH_LONG).show();
                images.remove(i);
                to.remove(i);
                imageids.remove(i);
                synchronized (adapter) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return gridview;
    }

    private void deletePhoto(final String imageid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                Log.e("Image Id Sent",imageid);
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
