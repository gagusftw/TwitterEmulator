package com.codepath.apps.TwitterEmulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.TwitterEmulator.models.Tweet;
import com.codepath.apps.TwitterEmulator.models.TweetsAdapter;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    public static final String TAG = "TimelineActivity";
    TwitterClient client;
    List<Tweet> timeline;
    RecyclerView rvTimeline;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //Initializing variables
        rvTimeline = findViewById(R.id.rvTimeline);
        swipeContainer = findViewById(R.id.swipeContainer);
        client = TwitterApp.getRestClient(this);
        timeline = new ArrayList<>();
        adapter = new TweetsAdapter(this, timeline);

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
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));
        rvTimeline.setAdapter(adapter);

        //Grab tweets JSON for timeline
        populateHomeTimeline();
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
                    Log.e(TAG, "JSONException initializing List<Tweet>", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "JSON HTTP response error");
            }
        });
    }
}