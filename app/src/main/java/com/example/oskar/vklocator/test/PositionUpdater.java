package com.example.oskar.vklocator.test;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.oskar.vklocator.MapPane;
import com.example.oskar.vklocator.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by Oskar on 26.04.15.
 */
public class PositionUpdater {
    Map pictures = new TreeMap<String, Bitmap>();
    String pictureQuerryRezult;
    Context context;

    public PositionUpdater(Context ctx) {
        context = ctx;
    }


    public void initPictures()
    {
        try {
//            String pictureInfoQuerry = "http://api.vkontakte.ru/method/users.get?uids=" + vkId + "&fields=photo_50";

//            new RequestImage().execute(pictureInfoQuerry);
  //          SystemClock.sleep(10);

            JSONObject pictureInfo = new JSONObject(pictureQuerryRezult);
            String pictureUrl = pictureInfo.getString("photo_50");


    //        bmp = BitmapFactory.decodeFile(pictureUrl);
        } catch (Exception e) {
      //      bmp = BitmapFactory.decodeResource(, R.drawable.online);
        }
       // pictures.put(vkId, bmp);
    }




    public void showMe() {
        //Toast.makeText(this, "jsonTest", Toast.LENGTH_LONG);

        //new RequestTask().execute("http://friendtracker.esy.es/update_user_info.php?VKID=555666777&Name=test&PointX=1234&PointY=431");//
        //new RequestTask().execute("http://friendtracker.esy.es/add_new_user.php?VKID=555666777&Name=test&PointX=1234&PointY=431");//здесь надыбал
        new RequestTask().execute("http://friendtracker.esy.es/read_users.php");
    }

    /// THE SHITTA FUCKAAH
    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("435678", "Response: " + result);

            parseUsers(result);


        }



        void parseUsers(String result) {

            try {
                JSONObject jObject = new JSONObject(result.substring(result.indexOf("{")));

                JSONArray jArray = jObject.getJSONArray("Users");

                for (int i = 0; i < jArray.length(); i++) {
                    try {
                        JSONObject oneUser = jArray.getJSONObject(i);
                        // Pulling items from the array

                        String vkId = oneUser.getString("VKID");
                        String name = oneUser.getString("Name");
                        String px = oneUser.getString("PointX");
                        String py = oneUser.getString("PointY");

                        Log.d("Parsed ", "User: " + name + " with id: " + vkId + "  at " + px + " " + py);


                        BitmapDescriptor bmp;
                        if (pictures.containsKey(vkId)) {
                            bmp = BitmapDescriptorFactory.fromBitmap((Bitmap) pictures.get(vkId));
                        } else {
                            bmp = BitmapDescriptorFactory.fromResource(R.drawable.online);
                        }

                        MapPane.mMap.addMarker(new MarkerOptions()
                                .icon(bmp)
                                .anchor(0.0f, 1.0f)// Anchors the marker on the bottom left
                                .title(name)
                                .snippet(name + "  " + vkId)
                                .position(new LatLng(Double.parseDouble(px), Double.parseDouble(py))));

                    } catch (JSONException e) {
                        // Oops
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


}
