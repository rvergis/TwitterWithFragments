package com.codepath.apps.twitterclient.tasks;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONArray;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.twitterclient.TweetsAdapter;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.TwitterClientApp;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.AsyncTask;
import android.util.Log;

public class RefreshTweetsTask extends AsyncTask<Void, Void, Void> {
	
	public static long REFRESH_COUNT = 25L;

	private static final Lock lock = new ReentrantLock();
	
	private static boolean loading = false;
	
	@Override
	protected Void doInBackground(Void... params) {
		Long max_id = TweetModel.maxUid();
		if (!loading) {
			loading = true;
			TwitterClientApp.getRestClient().getHomeTimeline(max_id, REFRESH_COUNT, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray jsonTweets) {
					lock.lock();
					ActiveAndroid.beginTransaction();
					try {
						Map<Long, UserModel> users = UserModel.fromJson(jsonTweets);
						if (users != null && !users.isEmpty()) {
							for (UserModel user : users.values()) {
								if (UserModel.byUid(user.getUid()) == null) {
									user.save();
								}
							}
						}
						Map<Long, TweetModel> tweets = TweetModel.fromJson(jsonTweets);
						if (tweets != null && !tweets.isEmpty()) {
							for (TweetModel tweet : tweets.values()) {
								if (TweetModel.byUid(tweet.getUid()) == null) {
									tweet.save();
									
								}
							}	
						}
						ActiveAndroid.setTransactionSuccessful();
					} catch(Throwable t) {
						Log.e(TwitterClient.LOG_NAME, "refresh tweet", t);
					} finally {
						loading = false;
						lock.unlock();
						ActiveAndroid.endTransaction();
					}						
					TweetsAdapter.instance.updateView();
				}
				
				@Override
				public void onFailure(Throwable e) {
					loading = false;
					Log.e(TwitterClient.LOG_NAME, "Unable to get home timeline", e);
				}
				
				@Override
				public void onFailure(Throwable e, JSONArray arg1) {
					loading = false;
					Log.e(TwitterClient.LOG_NAME, "Unable to get home timeline", e);
				}
				
			});
		}
		return null;
	}

}
