package com.codepath.apps.twitterwithfragments.tasks;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.twitterwithfragments.TweetsAdapter;
import com.codepath.apps.twitterwithfragments.TwitterClient;
import com.codepath.apps.twitterwithfragments.models.ITweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class AbstractGetTimelineTweetsTask extends AsyncTask<Object, Void, Void> {
	
	private static final Lock lock = new ReentrantLock();
	
	@Override
	protected Void doInBackground(Object... params) {
		final TweetsAdapter adapter = (TweetsAdapter) params[0];
		Long maxUid = null;
		if (adapter.getCount() > 0) {
			ITweetModel model = adapter.getItem(adapter.getCount() - 1);
			if (model != null) {
				maxUid = model.getUid();
				// ensures we don't get back this max_id
				maxUid--;
			}
		}	
		
		List<ITweetModel> dbItems = (List<ITweetModel>) getRecentTweets(maxUid);
		if (dbItems != null) {
			if (dbItems.size() != TweetsAdapter.REFRESH_COUNT) {
				// invoke the rest service
				invokeRestService(adapter, maxUid, createRestClientInvoker());
			} else {
				updateView(adapter, dbItems);
			}			
		} else {
			Log.e(TwitterClient.LOG_NAME, "got null dbItems");
		}
		
		return null;
	}
	
	protected void updateView(TweetsAdapter adapter, List<ITweetModel> dbItems) {
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
	
	private void invokeRestService(final TweetsAdapter adapter, final Long viewMaxUid, final IRestClientInvoker invoker) {
		if (!isLoading()) {
			setLoading(true);
			final Long maxUid = ((getMaxUid() == null) ? null : (getMaxUid()));
			invoker.invoke(maxUid, TweetsAdapter.REFRESH_COUNT, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray jsonTweets) {
					lock.lock();
					ActiveAndroid.beginTransaction();
					try {
						saveTweets(jsonTweets);
						ActiveAndroid.setTransactionSuccessful();
					} catch(Throwable t) {
						Log.e(TwitterClient.LOG_NAME, "refresh tweet", t);
					} finally {
						lock.unlock();
						ActiveAndroid.endTransaction();
					}
					if (adapter != null) {
						List<ITweetModel> dbItems = getRecentTweets(viewMaxUid);
						updateView(adapter, dbItems);
					} else {
						Log.e(TwitterClient.LOG_NAME, "got a null adapter");
					}
					setLoading(false);
				}
				
				@Override
				public void onFailure(Throwable e) {
					setLoading(false);
					Log.e(TwitterClient.LOG_NAME, "Unable to get home timeline", e);
				}
				
				@Override
				public void onFailure(Throwable e, JSONArray arg1) {
					setLoading(false);
					Log.e(TwitterClient.LOG_NAME, "Unable to get home timeline", e);
				}
				
			});
		}

	}
	
	protected abstract boolean isLoading();
	
	protected abstract void setLoading(boolean value);
	
	protected abstract void saveTweets(JSONArray jsonTweets);
	
	protected abstract Long getMaxUid();
	
	protected abstract List<ITweetModel> getRecentTweets(Long maxUid);
	
	protected abstract IRestClientInvoker createRestClientInvoker();
	
	public static interface IRestClientInvoker {
		public void invoke(Long maxId, long refreshCount, JsonHttpResponseHandler handler);		
	}
	
}
