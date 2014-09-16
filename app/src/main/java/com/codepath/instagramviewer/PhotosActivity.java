package com.codepath.instagramviewer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class PhotosActivity extends Activity {
    public static final String CLIENT_ID = "0dc5d7db20c747fab6f288c437055c40";

    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        client = new AsyncHttpClient();

        setupSwipeToRefresh();
        setupPopularPhotosListView();
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        client.get(popularUrl, getPopularResponseHandler());
    }

    private void setupSwipeToRefresh() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setupPopularPhotosListView() {
        photos = new ArrayList<InstagramPhoto>();
        aPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
    }

    private JsonHttpResponseHandler getPopularResponseHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    photos.clear();
                    JSONArray photosJSON = response.getJSONArray("data");

                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        InstagramPhoto photo = new InstagramPhoto();

                        photo.createdTime = photoJSON.getLong("created_time");

                        if (!photoJSON.isNull("caption")) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }

                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");

                        JSONObject image = photoJSON.getJSONObject("images").getJSONObject("standard_resolution");
                        photo.imageUrl = image.getString("url");
                        photo.imageHeight = image.getInt("height");
                        photo.imageWidth = image.getInt("width");

                        JSONObject user = photoJSON.getJSONObject("user");
                        photo.userName = user.getString("username");
                        photo.profileImageUrl = user.getString("profile_picture");

                        photo.comments = photoJSON.getJSONObject("comments").getJSONArray("data");

                        photos.add(photo);
                    }

                    swipeContainer.setRefreshing(false);
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
