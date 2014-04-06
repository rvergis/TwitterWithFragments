package com.codepath.apps.twitterwithfragments.tasks;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.codepath.apps.twitterwithfragments.IUserModelAdapter;
import com.codepath.apps.twitterwithfragments.TwitterClient;
import com.codepath.apps.twitterwithfragments.TwitterClientApp;
import com.codepath.apps.twitterwithfragments.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GetMyInfoTask extends AsyncTask<Object, Void, Void> {

	@Override
	protected Void doInBackground(final Object... params) {
		final Activity activity = (Activity) params[0];
		TwitterClientApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, JSONObject jsonObject) {
				try {
					final UserModel userModel = UserModel.fromJSON(jsonObject);
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							try  {
								activity.getActionBar().setTitle("@" + userModel.getScreenName());
								if (activity instanceof IUserModelAdapter) {
									((IUserModelAdapter) activity).adapt(userModel);
								}
							} catch(Throwable t) {
								Log.e(TwitterClient.LOG_NAME, "cannot set title", t);
							}
						}
						
					});
				} catch(Throwable t) {
					Log.e(TwitterClient.LOG_NAME, "error getting my info", t);
				}
			}
			
			@Override
			public void onFailure(Throwable e) {
				Log.e(TwitterClient.LOG_NAME, "Unable to post", e);
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject arg1) {
				Log.e(TwitterClient.LOG_NAME, "Unable to post", e);
			}
			
		});
		
		return null;
	}

}
