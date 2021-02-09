package com.codepath.apps.TwitterEmulator.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String name; //Full name
    public String username; //Twitter handle
    public String profileImageURL;

    //Initialize member variables with JSON
    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.username = jsonObject.getString("screen_name");
        user.profileImageURL = jsonObject.getString("profile_image_url_https");

        return user;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }
}
