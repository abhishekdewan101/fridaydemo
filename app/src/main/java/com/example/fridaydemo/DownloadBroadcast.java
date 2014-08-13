package com.example.fridaydemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
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
import java.net.URLEncoder;

/**
 * Created by a.dewan on 8/6/14.
 */
public class DownloadBroadcast extends BroadcastReceiver{
    String SHARE_MANAGER ="http://64d5993e.ngrok.com/";
    String USER_ID = Build.MODEL+Build.SERIAL;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("Broadcast Running","Booyah");
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SHARE_MANAGER+"images/new?to="+ URLEncoder.encode(USER_ID));
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
                    if(jsonArray.length() > 0){
                        int notificationId = 001;
                        // Build intent for default notification content
                        Intent viewIntent = new Intent(context,main.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,viewIntent,0);

                        NotificationCompat.Builder notificationBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.logo_small)
                                        .setContentTitle("New Photos Available")
                                        .setContentText(jsonArray.length() + " new photos are available to view")
                                        .setContentIntent(pendingIntent);

                        notificationBuilder.setVibrate((new long[]{100, 100, 100}));

                        NotificationManager notificationManager =
                                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                        notificationManager.notify(notificationId, notificationBuilder.build());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
