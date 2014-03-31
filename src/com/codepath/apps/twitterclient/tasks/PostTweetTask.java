package com.codepath.apps.twitterclient.tasks;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.twitterclient.TwitterClientApp;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.AsyncTask;

public class PostTweetTask extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... params) {
		String tweet = params[0];
		TwitterClientApp.getRestClient().postTweet(tweet, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, JSONArray arg1) {
			}
			
			@Override
			public void onFailure(Throwable arg0) {
			}
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
			}
			
		});
		
		return null;
	}

}
