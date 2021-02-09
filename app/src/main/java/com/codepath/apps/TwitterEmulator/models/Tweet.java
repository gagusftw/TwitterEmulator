package com.codepath.apps.TwitterEmulator.models;

import com.codepath.apps.TwitterEmulator.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    public String body; //Text of tweet
    public String timestamp;    //Time that tweet was posted
    public long id;
    public User user;

    //Load data into member variables from JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.timestamp = jsonObject.getString("created_at");
        tweet.id = jsonObject.getLong("id");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        return tweet;
    }

    //Initialize list of Tweets through JSONArray
    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweetList = new ArrayList<Tweet>();
        for(int i = 0; i < jsonArray.length(); i++) {
            tweetList.add(Tweet.fromJSON(jsonArray.getJSONObject(i)));
        }

        return tweetList;
    }

    public static long getLastID(List<Tweet> tweetsList) {
        return tweetsList.get(tweetsList.size() - 1).getID();
    }

    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return TimeFormatter.getTimeDifference(timestamp);
    }

    public User getUser() {
        return user;
    }

    public long getID() { return id; }
}
