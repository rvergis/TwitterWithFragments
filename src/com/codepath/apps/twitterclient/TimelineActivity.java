package com.codepath.apps.twitterclient;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.tasks.PostTweetTask;
import com.codepath.apps.twitterclient.tasks.RefreshTweetsTask;

public class TimelineActivity extends Activity {

	private static final int REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		List<TweetModel> tweets = new ArrayList<TweetModel>();
		ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
		TweetsAdapter adapter = new TweetsAdapter(this, tweets);
		lvTweets.setAdapter(adapter);
		
		lvTweets.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			static final int THRESHOLD_COUNT = 3;
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {				
				if (firstVisibleItem + visibleItemCount + THRESHOLD_COUNT >= totalItemCount) {
					new RefreshTweetsTask().execute();						
				}
			}
		});
		
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	public void onComposeNewPressed(MenuItem mi) {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				postTweet(requestCode, resultCode, data.getStringExtra("tweet"));
			}
		}
	}
	
	private void postTweet(int requestCode, int resultCode, final String tweet) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Twitter bird dispatched", Toast.LENGTH_SHORT).show();
				postToTwitter(tweet);
			} else {
				Toast.makeText(this, "Twitter bird shot down", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void postToTwitter(String tweet) {
		new PostTweetTask().execute(tweet, this);
	}
}
