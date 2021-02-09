package com.codepath.apps.TwitterEmulator.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.TwitterEmulator.R;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweetsList;

    public TweetsAdapter(Context context, List<Tweet> tweetsList) {
        this.context = context;
        this.tweetsList = tweetsList;
    }

    //Inflating the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //Binding all of our data from each tweet to each view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweetsList.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweetsList.size();
    }

    //Used to help handle refreshing by clearing current list
    public void clear() {
        this.tweetsList.clear();
        notifyDataSetChanged();
    }

    //Used to help refreshing by adding new timeline of tweets
    public void addAll(List<Tweet> tweetsList) {
        this.tweetsList.addAll(tweetsList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTimelineProfile;
        TextView tvTimelineName;
        TextView tvTimelineUsername;
        TextView tvTimelineBody;
        TextView tvTimelineTimestamp;

        //Intitializing our references
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTimelineProfile = itemView.findViewById(R.id.ivTimelineProfile);
            tvTimelineName = itemView.findViewById(R.id.tvTimelineName);
            tvTimelineUsername = itemView.findViewById(R.id.tvTimelineUsername);
            tvTimelineBody = itemView.findViewById(R.id.tvTimelineBody);
            tvTimelineTimestamp = itemView.findViewById(R.id.tvTimelineTimestamp);
        }

        //Binding data from each Tweet into a View
        public void bind(Tweet tweet) {
            tvTimelineName.setText(tweet.getUser().getName());
            tvTimelineUsername.setText("@" + tweet.getUser().getUsername());
            tvTimelineBody.setText(tweet.getBody());
            tvTimelineTimestamp.setText(tweet.getTimestamp());
            //Using Glide library to render images
            Glide.with(context).load(tweet.getUser().getProfileImageURL()).into(ivTimelineProfile);
        }
    }
}
