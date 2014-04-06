package com.codepath.apps.twitterwithfragments.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.activeandroid.Model;
import com.codepath.apps.twitterwithfragments.TweetsAdapter;
import com.codepath.apps.twitterwithfragments.TwitterClientApp;
import com.codepath.apps.twitterwithfragments.models.ITweetModel;
import com.codepath.apps.twitterwithfragments.models.UserModel;
import com.codepath.apps.twitterwithfragments.models.UserTimelineModel;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GetUserTimelineTweetsTask extends AbstractGetTimelineTweetsTask {

	protected static boolean loading = false;
		
	@Override
	protected IRestClientInvoker createRestClientInvoker() {
		return new IRestClientInvoker() {
			
			@Override
			public void invoke(Long maxId, long refreshCount,
					JsonHttpResponseHandler handler) {
				TwitterClientApp.getRestClient().getUserTimeline(maxId, refreshCount, handler);
				
			}
		};
	}

	@Override
	protected void saveTweets(JSONArray jsonTweets) {
		UserModel.save(jsonTweets);
		UserTimelineModel.save(jsonTweets);		
	}

	@Override
	protected Long getMaxUid() {
		return UserTimelineModel.maxUid();	}

	@Override
	protected List<ITweetModel> getRecentTweets(Long maxUid) {
		List<ITweetModel> tweetModels = new ArrayList<ITweetModel>();
		List<Model> models =  (List<Model>) UserTimelineModel.recentItems(maxUid, TweetsAdapter.REFRESH_COUNT);
		for (Model model : models) {
			if (model instanceof ITweetModel) {
				tweetModels.add((ITweetModel) model);
			}
		}		
		return tweetModels;		
	}
	
	@Override
	protected boolean isLoading() {
		return loading;
	}

	@Override
	protected void setLoading(boolean value) {
		loading = value;
	}		
	
}
