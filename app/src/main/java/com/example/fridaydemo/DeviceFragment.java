package com.example.fridaydemo;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by a.dewan on 8/5/14.
 */
public class DeviceFragment extends Fragment {

    String USER_ID = Build.MODEL+Build.SERIAL;
    String SHARE_MANAGER ="http://64d5993e.ngrok.com/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();





        final View rootView = inflater.inflate(R.layout.devicefragment, container, false);

        getUserNames(rootView,intent);
        return rootView;
    }

    private void getUserNames(final View rootView,final Intent intent) {
          new Thread(new Runnable() {
              @Override
              public void run() {
                  checkUser();
                  Log.e("Error","Here too");
                  HttpClient client = new DefaultHttpClient();
                  HttpGet httpGet = new HttpGet(SHARE_MANAGER+"users/active");
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
                      Boolean uniqueUsers = false;
                      JSONArray jsonArray = new JSONArray(builder.toString());
                      ArrayList<String> username = new ArrayList<String>();
                      ArrayList<String> userid = new ArrayList<String>();
//                      String [] username = new String[jsonArray.length()];
//                      String [] userid = new String[jsonArray.length()];
                      for(int i=0;i<jsonArray.length();i++){
                          JSONObject jsonObject = jsonArray.getJSONObject(i);
                          username.add(jsonObject.getString("username"));
                          userid.add(jsonObject.getString("userid"));
                        }

                        for(int i=0;i<userid.size();i++){
                            if(userid.get(i).equals(USER_ID)){
                                userid.remove(i);
                                username.remove(i);
                            }
                        }
                       Object [] username1 = username.toArray();
                       Object [] userid1 = userid.toArray();
                       populateGridView(username1, userid1, rootView, intent);

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }

              private void checkUser() {
                  HttpClient client = new DefaultHttpClient();
                  HttpGet httpGet = new HttpGet(SHARE_MANAGER+"users/present?userid="+ URLEncoder.encode(USER_ID));
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
                      Log.e("Json Array Length",jsonArray.length()+"");
                      if(jsonArray.length() == 0){
                          HttpClient httpClient = new DefaultHttpClient();
                          HttpPost httpPost = new HttpPost(SHARE_MANAGER + "users");
                          List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                          nameValuePairList.add(new BasicNameValuePair("users[active]","true"));
                          nameValuePairList.add(new BasicNameValuePair("users[userid]",URLEncoder.encode(USER_ID)));
                          nameValuePairList.add(new BasicNameValuePair("users[username]",Build.USER));
                          httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                          httpClient.execute(httpPost);
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  } catch (IOException e){
                      e.printStackTrace();
                  }
              }

              private void populateGridView(final Object[] username, final Object[] userid,final View rootView,final Intent intent) {
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          final String action = intent.getAction();

                          final String type = intent.getType();



                          final GridView gridView = (GridView)  rootView.findViewById(R.id.gridview);

                          gridView.setAdapter(new ImageAdapter(rootView.getContext(),username));

                          gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                  if(Intent.ACTION_SEND.equals(action) && type !=null){
                                      final Uri imageUri = (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
                                      handleFile(imageUri,USER_ID,userid[i].toString());
                                      ((ImageView)((RelativeLayout)gridView.getChildAt(i)).getChildAt(0)).setImageResource(R.drawable.device2);
                                  }else if(Intent.ACTION_SEND_MULTIPLE.equals(action) && type!=null){
                                      ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                                      if(imageUris!=null){
                                          for(int counter=0;counter<imageUris.size();counter++){
                                              Uri tempUri = imageUris.get(counter);
                                              handleFile(tempUri,USER_ID,userid[i].toString());
                                              ((ImageView)((RelativeLayout)gridView.getChildAt(i)).getChildAt(0)).setImageResource(R.drawable.device2);
                                          }
                                      }
                                  }
                              }

                              private void handleFile(final Uri imageUri, final String from, final String to) {
                                 new Thread(new Runnable() {
                                     @Override
                                     public void run() {
                                         try {
                                             HttpClient httpClient = new DefaultHttpClient();
                                             HttpPost httpPost = new HttpPost(SHARE_MANAGER + "images");
                                             File file = new File(getPath(imageUri));
                                             MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                                             builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                                             FileBody fileBody = new FileBody(file);
                                             builder.addPart("images[image]", fileBody);
                                             builder.addPart("images[to]", new StringBody(to));
                                             builder.addPart("images[from]",new StringBody(from));
                                             builder.addPart("images[uuid]",new StringBody(USER_ID+" - "+file.getName()+" - "+Calendar.getInstance().getTime()));
                                             builder.addPart("images[newvalue]",new StringBody("yes"));

                                             HttpEntity entity = builder.build();

                                             httpPost.setEntity(entity);

                                             httpClient.execute(httpPost);

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                   Toast.makeText(getActivity().getApplicationContext(),"File Shared ",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                         }catch(IOException e){
                                             e.printStackTrace();
                                         }
                                     }
                                 }).start();
                              }
                          });
                      }
                  });
              }
          }).start();
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
