package com.nguyen.myinstagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
   private static final String   CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
   private List<Photo>  mPhotos = new ArrayList<>();
   private Adapter      mAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      // create an adapter linking it to the source
      mAdapter = new Adapter(this, mPhotos);
      // find the ListView from the layout
      ListView listView = (ListView)findViewById(R.id.lvPhotos);
      // bind the Adapter to the ListView
      listView.setAdapter(mAdapter);
      // fetch the popular photos
      fetchPopularPhotos();
   }

   // this method triggers API requests
   private void fetchPopularPhotos() {
      String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
      // create the network client
      AsyncHttpClient client = new AsyncHttpClient();
      // trigger the GET request
      client.get(url, null, new JsonHttpResponseHandler() {
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            // expecting a JSON object
            // Log.i("NGUYEN", response.toString());
            // iterate each photo item and decode the item into a Photo object
            JSONArray dataJsonArray = null;
            try {
               // "data" JSON array
               dataJsonArray = response.getJSONArray("data");
               // iterate thru "data" JSON array
               for (int i = 0; i < dataJsonArray.length(); i++) {
                  // get the "data" JSON object at that position
                  JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                  // decode the attributes of the JSON into a data model
                  Photo photo = new Photo();
                  photo.username = dataJsonObject.getJSONObject("user").getString("username");
                  photo.caption = dataJsonObject.getJSONObject("caption").getString("text");
                  photo.imageUrl = dataJsonObject.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                  photo.imageHeight = dataJsonObject.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                  photo.likesCount = dataJsonObject.getJSONObject("likes").getInt("count");
                  // add decoded object to the list of Photo's
                  mPhotos.add(photo);
               }
            }
            catch (JSONException e) {
               e.printStackTrace();
            }

            mAdapter.notifyDataSetChanged();
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
         }
      });
   }
}
