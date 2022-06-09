package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class DetailTweetActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvBody;
    TextView tvScreenName;
    ImageView ivContentImage;
    TextView tvCreatedAt;
    Tweet tweet;
    TextView tvRetweetCount;
    TextView tvLikeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        ivContentImage = findViewById(R.id.ivContentImage);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvRetweetCount = findViewById(R.id.tvRetweetCount);
        tvLikeCount = findViewById(R.id.tvLikeCount);



        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("TweetDetails"));
        Log.i("DetailTweetActivity", "showing detail for tweet: " + tweet.body);
        tvName.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvCreatedAt.setText( tweet.createdAt.substring(11,19) + " • " + tweet.createdAt.substring(0, 10));
        tvRetweetCount.setText(tweet.retweets + " Retweets");
        tvLikeCount.setText(tweet.likes + " Likes");

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .circleCrop()
                .into(ivProfileImage);
        if (tweet.contentImageUrl != null) {
            ivContentImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(tweet.contentImageUrl)
                    .into(ivContentImage);
        } else {
            // must set this to gone or certain views will be reused
            ivContentImage.setVisibility(View.GONE);
        }


    }
}