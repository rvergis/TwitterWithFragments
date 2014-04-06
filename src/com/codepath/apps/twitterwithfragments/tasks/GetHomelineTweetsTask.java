package com.codepath.apps.twitterwithfragments.tasks;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONArray;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.twitterwithfragments.TweetsAdapter;
import com.codepath.apps.twitterwithfragments.TwitterClient;
import com.codepath.apps.twitterwithfragments.TwitterClientApp;
import com.codepath.apps.twitterwithfragments.models.TweetModel;
import com.codepath.apps.twitterwithfragments.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.AsyncTask;
import android.util.Log;

public class GetHomelineTweetsTask extends AsyncTask<Object, Void, Void> {
	
	private static final Lock lock = new ReentrantLock();
	
	private static boolean loading = false;
	
	@Override
	protected Void doInBackground(Object... params) {
		final TweetsAdapter adapter = (TweetsAdapter) params[0];		
		Long maxUid = null;
		if (adapter.getCount() > 0) {
			TweetModel model = adapter.getItem(adapter.getCount() - 1);
			if (model != null) {
				maxUid = model.getUid();
				// ensures we don't get back this max_id
				maxUid--;
			}
		}	
		
		List<TweetModel> dbItems = TweetModel.recentItems(maxUid, TweetsAdapter.REFRESH_COUNT);
		if (dbItems != null) {
			if (dbItems.size() != TweetsAdapter.REFRESH_COUNT) {
				// invoke the rest service
				invokeRestService(adapter, maxUid);
			} else {
				updateView(adapter, dbItems);
			}			
		} else {
			Log.e(TwitterClient.LOG_NAME, "got null dbItems");
		}
		
		return null;
	}
	
	protected void updateView(TweetsAdapter adapter, List<TweetModel> dbItems) {
		if (dbItems != null) {
			if (adapter != null) {
				adapter.addTweetsToView(dbItems);																	
			} else {
				Log.e(TwitterClient.LOG_NAME, "got a null adapter");
			}
		} else {
			Log.e(TwitterClient.LOG_NAME, "got a null dbItems");
		}
	}
	
	private void invokeRestService(final TweetsAdapter adapter, final Long viewMaxUid) {
		if (!loading) {
			loading = true;
			final Long maxUid = ((TweetModel.maxUid() == null) ? null : (TweetModel.maxUid()));
			TwitterClientApp.getRestClient().getHomeTimeline(maxUid, TweetsAdapter.REFRESH_COUNT, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray jsonTweets) {
					lock.lock();
					ActiveAndroid.beginTransaction();
					try {
						UserModel.save(jsonTweets);
						TweetModel.save(jsonTweets);
						ActiveAndroid.setTransactionSuccessful();
					} catch(Throwable t) {
						Log.e(TwitterClient.LOG_NAME, "refresh tweet", t);
					} finally {
						lock.unlock();
						ActiveAndroid.endTransaction();
					}
					if (adapter != null) {
						List<TweetModel> dbItems = TweetModel.recentItems(viewMaxUid, TweetsAdapter.REFRESH_COUNT);
						updateView(adapter, dbItems);
					} else {
						Log.e(TwitterClient.LOG_NAME, "got a null adapter");
					}
					loading = false;
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

	}

}
