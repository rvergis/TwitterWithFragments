package com.codepath.apps.twitterwithfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.twitterwithfragments.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterwithfragments.tasks.PostTweetTask;

public class TweetsActivity extends FragmentActivity {

	private static final int REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
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
		HomeTimelineFragment fragment = (HomeTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentTweets);
		new PostTweetTask().execute(tweet, this, fragment.getAdapter());
	}
}
