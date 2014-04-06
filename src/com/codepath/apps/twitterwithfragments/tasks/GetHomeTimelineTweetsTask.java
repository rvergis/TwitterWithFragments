package com.codepath.apps.twitterwithfragments.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.activeandroid.Model;
import com.codepath.apps.twitterwithfragments.TweetsAdapter;
import com.codepath.apps.twitterwithfragments.TwitterClientApp;
import com.codepath.apps.twitterwithfragments.models.HomeTimelineModel;
import com.codepath.apps.twitterwithfragments.models.ITweetModel;
import com.codepath.apps.twitterwithfragments.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GetHomeTimelineTweetsTask extends AbstractGetTimelineTweetsTask {

	protected static boolean loading = false;
	
	@Override
	protected IRestClientInvoker createRestClientInvoker() {
		return new IRestClientInvoker() {
			
			@Override
			public void invoke(Long maxId, long refreshCount, Long userId,
					JsonHttpResponseHandler handler) {
				TwitterClientApp.getRestClient().getHomeTimeline(maxId, refreshCount, handler);
				
			}
		};
	}

	@Override
	protected List<ITweetModel> getRecentTweets(Long maxUid) {
		List<ITweetModel> tweetModels = new ArrayList<ITweetModel>();
		List<Model> models =  (List<Model>) HomeTimelineModel.recentItems(maxUid, TweetsAdapter.REFRESH_COUNT);
		for (Model model : models) {
			if (model instanceof ITweetModel) {
				tweetModels.add((ITweetModel) model);
			}
		}		
		return tweetModels;		
	}

	@Override
	protected Long getMaxUid() {
		return HomeTimelineModel.maxUid();
	}

	@Override
	protected void saveTweets(JSONArray jsonTweets) {
		UserModel.save(jsonTweets);
		HomeTimelineModel.save(jsonTweets);		
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
