package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    // pass in the context and list of tweets through a constructor
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // for each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the data at position
        Tweet tweet = tweets.get(position);
        //sets the tag to get the tweet later in detail view
        holder.rootView.setTag(tweet);
        //bind the data that we get (the tweet) with the view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivContentImage;
        TextView tvTimestamp;
        View rootView;
        TextView tvName;


        // a tweet
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivContentImage = itemView.findViewById(R.id.ivContentImage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvName = itemView.findViewById(R.id.tvName);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Tweet tweet = (Tweet)view.getTag();
                    Log.i("TweetsAdapter", "tweet clicked!" + tweet);
                    if (tweet != null) {
                        Intent i = new Intent(context, DetailTweetActivity.class);
                        i.putExtra("TweetDetails", Parcels.wrap(tweet));
                        context.startActivity(i);
                    }
                }
            });
        }

        public void bind (Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvTimestamp.setText(tweet.timeAgo);
            tvName.setText(tweet.user.name);
            Glide.with(context)
                        .load(tweet.user.profileImageUrl)
                        .circleCrop()
                        .into(ivProfileImage);
                if (tweet.contentImageUrl != null) {
                    ivContentImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(tweet.contentImageUrl)
                        .into(ivContentImage);
            } else {
                // must set this to gone or certain views will be reused
                ivContentImage.setVisibility(View.GONE);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

}
