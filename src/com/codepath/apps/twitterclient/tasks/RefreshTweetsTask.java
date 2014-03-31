package com.codepath.apps.twitterclient.tasks;

import java.util.List;

import org.json.JSONArray;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.twitterclient.TweetsAdapter;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.TwitterClientApp;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.AsyncTask;
import android.util.Log;

public class RefreshTweetsTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				List<TweetModel> tweets = TweetModel.fromJson(jsonTweets);
				if (tweets != null && !tweets.isEmpty()) {
					ActiveAndroid.beginTransaction();
					try {
						for (TweetModel tweet : tweets) {
							tweet.getUser().save();
							tweet.save();							
						}	
						ActiveAndroid.setTransactionSuccessful();
					} catch(Throwable t) {
						Log.e(TwitterClient.LOG_NAME, "refresh tweet", t);
					} finally {
						ActiveAndroid.endTransaction();
					}
					TweetsAdapter.instance.updateView();
				}
			}
			
			@Override
			public void onFailure(Throwable e) {
				Log.e(TwitterClient.LOG_NAME, "Unable to get home timeline", e);
			}
			
			@Override
			public void onFailure(Throwable e, JSONArray arg1) {
				Log.e(TwitterClient.LOG_NAME, "Unable to get home timeline", e);
			}
			
		});
		return null;
	}

}
