package com.codepath.apps.TwitterEmulator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.TwitterEmulator.models.Tweet;
import com.codepath.apps.TwitterEmulator.models.TweetsAdapter;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    public static final String TAG = "TimelineActivity";
    public static final int LAUNCH_COMPOSE = 234;
    TwitterClient client;
    List<Tweet> timeline;
    RecyclerView rvTimeline;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.twitterlogowhite_small);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Initializing variables
        rvTimeline = findViewById(R.id.rvTimeline);
        swipeContainer = findViewById(R.id.swipeContainer);
        client = TwitterApp.getRestClient(this);
        timeline = new ArrayList<>();
        adapter = new TweetsAdapter(this, timeline);
        rvTimeline.setAdapter(adapter);

        //Setting up the refreshing of the tweet timeline
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                //Use same method as for regular fetching
                populateHomeTimeline();
                adapter.addAll(timeline);
                swipeContainer.setRefreshing(false);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTimeline.setLayoutManager(linearLayoutManager);

        //Setting up listener for infinite pagination
        rvTimeline.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData();
            }
        });

        //Grab tweets JSON for timeline
        populateHomeTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    //Requests JSON and populates a List<Tweet> with JSON data
    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "JSON HTTP response success");

                try {
                    //Load JSON data into List<Tweet>
                    timeline.addAll(Tweet.fromJSONArray(json.jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException initializing timeline", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "JSON HTTP response error");
            }
        });
    }

    //Helper method to help with infinite pagination HTTP request
    private void loadMoreData() {
        long lastID = Tweet.getLastID(timeline);
        client.getNextPageTweets(lastID - 1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                int size = timeline.size();
                try {
                    timeline.addAll(Tweet.fromJSONArray(json.jsonArray));
                    adapter.notifyItemRangeInserted(size - 1, json.jsonArray.length());
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException adding new tweets to timeline", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "JSON GET error");
            }
        });
    }

    //When user clicks on the compose icon on the ActionBar
    public void onComposeAction(MenuItem mi) {
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivityForResult(intent, LAUNCH_COMPOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_COMPOSE && resultCode == RESULT_OK) {
            String status = data.getExtras().getString("tweet");
            client.postNewTweet(status, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    try {
                        Log.i(TAG, "Fulfilled POST request for composed tweet");
                        timeline.add(0, Tweet.fromJSON(json.jsonObject));
                        adapter.notifyItemInserted(0);
                        rvTimeline.smoothScrollToPosition(0);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Exception adding composed tweet to timeline");
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "JSON POST Response Error");
                }
            });
        }
    }
}