package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

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
    ImageButton btnLike;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        client = TwitterApp.getRestClient(this);


        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        ivContentImage = findViewById(R.id.ivContentImage);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvRetweetCount = findViewById(R.id.tvRetweetCount);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        btnLike = findViewById(R.id.btnLike);



        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("TweetDetails"));
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

        if (tweet.favorited) {
            btnLike.setColorFilter(Color.argb(255,255,0,0));
        } else {
            btnLike.setColorFilter(Color.argb(0,255,0,0));
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweet.favorited) {
                    client.unlikeTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            btnLike.setColorFilter(Color.argb(0,255,0,0));
                            tweet.favorited = false;
                            tweet.likes = String.valueOf(((Integer.parseInt(tweet.likes)) - 1));
                            tvLikeCount.setText(tweet.likes + " Likes");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("DetailTweetActivity", new Throwable(throwable).toString());
                        }
                    });
                } else {
                    client.likeTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            btnLike.setColorFilter(Color.argb(255,255,0,0));
                            tweet.favorited = true;
                            tweet.likes = String.valueOf(((Integer.parseInt(tweet.likes)) + 1));
                            tvLikeCount.setText(tweet.likes + " Likes");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("DetailTweetActivity", new Throwable(throwable) + response);
                        }
                    });
                }

            }
        });

    }
}