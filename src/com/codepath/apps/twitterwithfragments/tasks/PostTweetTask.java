package com.codepath.apps.twitterwithfragments.tasks;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.twitterwithfragments.TweetsAdapter;
import com.codepath.apps.twitterwithfragments.TwitterClient;
import com.codepath.apps.twitterwithfragments.TwitterClientApp;
import com.codepath.apps.twitterwithfragments.models.TweetModel;
import com.codepath.apps.twitterwithfragments.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class PostTweetTask extends AsyncTask<Object, Void, Void> {

	@Override
	protected Void doInBackground(final Object... params) {
		final String tweet = (String) params[0];
		final Activity activity = (Activity) params[1];
		final TweetsAdapter adapter = (TweetsAdapter) params[2];
		TwitterClientApp.getRestClient().postTweet(tweet, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, JSONObject jsonObject) {
				try {
					UserModel.save(jsonObject);
					TweetModel.save(jsonObject);				
					adapter.refreshView();		
					
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(activity, "TwitterBird reached safely", Toast.LENGTH_SHORT).show();
						}
						
					});
					
				} catch(Throwable t) {
					Log.e(TwitterClient.LOG_NAME, "error posting tweet", t);
				}
			}
			
			@Override
			public void onSuccess(int statusCode, JSONArray jsonArray) {
				try {
					UserModel.save(jsonArray);
					TweetModel.save(jsonArray);
					adapter.refreshView();
					
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(activity, "TwitterBird reached safely", Toast.LENGTH_SHORT).show();
						}
						
					});
					
				} catch(Throwable t) {
					Log.e(TwitterClient.LOG_NAME, "error posting tweet", t);					
				}
			}
			
			@Override
			public void onFailure(Throwable e) {
				Log.e(TwitterClient.LOG_NAME, "Unable to post", e);
				Toast.makeText(activity, "TwitterBird shot down. Call PETA.", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject arg1) {
				Log.e(TwitterClient.LOG_NAME, "Unable to post", e);
				Toast.makeText(activity, "TwitterBird shot down. Call PETA.", Toast.LENGTH_SHORT).show();
			}
			
		});
		
		return null;
	}

}
