package com.nguyen.myinstagram;

import android.support.v4.widget.SwipeRefreshLayout;
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
   private PhotosAdapter      mAdapter;
   private SwipeRefreshLayout mSwipeContainer;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // look up the swipe container view
      mSwipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
      // create an adapter linking it to the source
      mAdapter = new PhotosAdapter(this, new ArrayList<Photo>());
      // find the ListView from the layout
      ListView listView = (ListView)findViewById(R.id.photos_list_view);
      // bind the PhotosAdapter to the ListView
      listView.setAdapter(mAdapter);
      // fetch popular photos upon app startup
      fetchPopularPhotos();
      // setup refresh listener which triggers new data loading
      mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
         @Override
         public void onRefresh() {
            // fetch popular photos upon swipe refresh
            fetchPopularPhotos();
         }
      });
      // configure the refreshing colors
      mSwipeContainer.setColorSchemeColors(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
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
            List<Photo> photos = new ArrayList<>();
            try {
               // "data" JSON array
               dataJsonArray = response.getJSONArray("data");
               // iterate thru "data" JSON array
               for (int i = 0; i < dataJsonArray.length(); i++) {
                  // get the "data" JSON object at that position
                  JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                  // decode the attributes of the JSON into a data model
                  Photo photo = new Photo();
                  photo.mUsername = dataJsonObject.getJSONObject("user").getString("username");
                  photo.mCaption = dataJsonObject.getJSONObject("caption").getString("text");
                  photo.mImageUrl = dataJsonObject.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                  photo.mImageHeight = dataJsonObject.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                  photo.mLikesCount = dataJsonObject.getJSONObject("likes").getInt("count");
                  // add decoded object to the list of Photo's
                  photos.add(photo);
               }
            } catch (JSONException e) {
               e.printStackTrace();
            }

            // clear out old items
            mAdapter.clear();
            // add new items to adapter
            mAdapter.addAll(photos);
            // there seems no need to call notifyDataSetChanged()
            // mAdapter.notifyDataSetChanged();
            // signal refresh has finished
            mSwipeContainer.setRefreshing(false);
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("NGUYEN", "Fetch timeline error: " + throwable.toString());
         }
      });
   }
}
